# Local data, reachability and privilege surface

This note covers what `com.miui.guardprovider` keeps on the device, who can reach it, and
whether its privileged permissions can be turned against the user by another app. It
complements `docs/AI-GUARD.md`, which reconstructs the capture chain.

Paths are relative to `evidence/decompiled/`.

---

## 1. Data at rest

| Data | Store | Format | Sensitivity |
|---|---|---|---|
| Virus definitions, whitelist, engine metadata | `databases/flamingo.db` (Room, `C0616s.a(context, EngineDatabase.class, "flamingo.db")`) | SQLite | low — vendor content |
| Whitelist served through the provider | table `t_virus_white_list`, exposed as `content://guard/...` | SQLite | low |
| AI Guard daily counters (`qqVideoDetectionTimes`, `qqVoiceDetectionTimes`, `wechat*`, `phoneVoiceDetectionTimes`) | `shared_prefs/ai_guard_daily_report.xml` | XML | low — usage counts, no content |
| **Raw call audio** | `files/feedback01.tmp` | PCM 16 kHz mono, plaintext | **high** |
| **Screen recording** | `files/clip01.tmp`, plus transient `cache/clip_*.tmp` | MP4 H.264, plaintext | **high** |
| Scan results / logs | logcat | text | low — package names, paths, verdicts |

No external storage is used: a search for `getExternalFilesDir`,
`getExternalStorageDirectory`, `/sdcard` and `MODE_WORLD_*` across `com.miui` returns
nothing. `allowBackup=false`, `sharedUserId` is absent, `android:debuggable` is absent.

### 1.1 Retention of the AI Guard artefacts

`com/miui/guardprovider/aiguard/i.java` writes to two files in `getFilesDir()`:

```java
this.f12544a = aVar.a(context, "feedback01.tmp");   // every PCM buffer, appended
this.f12546c = aVar.a(context, "clip01.tmp");       // copy of a confirmed clip
```

Deletion is conditional, not guaranteed:

| Path | Behaviour |
|---|---|
| Service destroyed, **no** alert raised | `k` calls `H().f()` → both files deleted (`AiGuardService.java:599`) |
| Alert raised (`alerted=true`) | **files are kept**; `f()` is skipped |
| User taps “标记为误报” | `Q1`/`R1` upload, then `P1` deletes both (`AlertActivity.java:171-208, 212`) |
| User taps “取消” | `P1` deletes both |
| Process killed before `onDestroy` | files remain until the next session overwrites them |

`Files.write(..., WRITE, CREATE)` truncates on the next capture, so the window is "from the
end of a call until the next capture or an explicit dismiss". On a device that is rooted, or
in a forensic image, this is a recoverable recording of the last call, including the remote
party. A `.tmp` extension does not make it ephemeral.

This is reported as **F-12**.

---

## 2. Reachability for an ordinary app

Every custom exported component is protected by `com.miui.guardprovider.permission.Guard_Provider`
(`protectionLevel="0x12"` = `signature|privileged`):

| Component | Exported | Permission |
|---|---|---|
| `GuardProvider` (`content://guard`) | yes | `Guard_Provider` |
| `manager.SecurityService` | yes | `Guard_Provider` |
| `aiguard.AiGuardService` | yes | `Guard_Provider` |
| `aiguard.PermissionRequestActivity` | yes | `Guard_Provider` |
| `androidx.work...SystemJobService` | yes | `BIND_JOB_SERVICE` (signature) |
| `androidx.work...DiagnosticsReceiver` | yes | `DUMP` (signature\|privileged) |
| `androidx.profileinstaller.ProfileInstallReceiver` | yes | `DUMP` (signature\|privileged) |

The provider implements `query/insert/delete/call/getType`, but **not** `openFile` and not
`grantUriPermissions`, so there is no descriptor or URI-grant path. `call("getRiskAppData")`
returns a `Bundle` of installed-app risk data — behind the same permission.

**Conclusion:** an app with no permissions can neither read the databases nor invoke a scan,
and cannot reach the AI Guard service. This is the opposite of the companion DM client.

### 2.1 Logcat

`GuardProvider`, `TencentEngine` and `AvlEngine` log package names and verdicts
(`Log.i("TencentEngine", "getVirusInfo packageName : " + ...)`, `Log.d(" GuardProvider",
"start parse apk : " + path)`). No audio, transcript or clip content is logged. Reading
another app's logcat requires `READ_LOGS` (`signature|privileged`), so this is not an
unprivileged path either.

---

## 3. Privilege surface

The package holds an unusually strong set: `READ_PRIVILEGED_PHONE_STATE`,
`MANAGE_MEDIA_PROJECTION`, `CAPTURE_AUDIO_OUTPUT`, `CAPTURE_VOICE_COMMUNICATION_OUTPUT`,
`MODIFY_PHONE_STATE`, `MODIFY_AUDIO_ROUTING`, `WRITE_SECURE_SETTINGS`,
`INTERACT_ACROSS_USERS`, `MANAGE_EXTERNAL_STORAGE`, `QUERY_ALL_PACKAGES`,
`READ_PRIVILEGED_PHONE_STATE`.

| Question | Answer |
|---|---|
| Can an unprivileged app invoke a privileged operation through an exported component? | **No** — every custom entry point is behind `signature\|privileged` |
| Is there a confused deputy in the provider's `call()` or `query()`? | No — the permission is enforced before dispatch, and the provider does not act on caller-supplied file paths |
| Can an app obtain a URI grant to provider data? | No — `grantUriPermissions` is absent and `openFile` is not implemented |
| Can an app make the AI Guard service start? | No — `Guard_Provider` is required; in practice only the security centre holds it |

The residual risk is not a missing check but the **value of the process**: if a
memory-safety defect exists in one of the twelve native libraries that parse attacker-supplied
input — `libflamingo.so`, `libavlasys.so`, `libavlm.so`, `libavlurl.so`,
`libtmsdk-2.0.16-mfr.so`, `libams-1.3.4-mfr.so`, `libdce-1.1.24-mfr.so`,
`libturingvd.so`, `liburldetectorsys.so` — code execution would land in a process holding
`MANAGE_MEDIA_PROJECTION`, `CAPTURE_AUDIO_OUTPUT` and `WRITE_SECURE_SETTINGS`. That is a
materially better position than a normal app.

No such defect was identified in this static review: the native libraries were not reversed,
and no Java-side memory-unsafe pattern exists. This is an **attack-surface observation**, not
a finding.

---

## 4. Collection and reporting timeline

`com.miui.guardprovider` has **no self-scheduled collection**. It declares no
`BOOT_COMPLETED` receiver, no `PACKAGE_ADDED` receiver, no periodic `WorkManager` request.
Every scan starts because the security centre binds `SecurityService` and calls a scan entry
point; every result is reported because that call produced a hit.

| Trigger | Collected | Stored | Reported | When |
|---|---|---|---|---|
| Security centre scan (manual, install, schedule — decided outside this package) | package list, signatures, APK hashes, engine verdict | `flamingo.db` | `ResultReportData` per hit via `ReportWorker` | immediately after the scan, retried by WorkManager |
| Engine update | virus DB, whitelist | `flamingo.db`, `t_virus_white_list` | none | on demand from the security centre |
| `GuardProvider.call("getRiskAppData")` | installed-app risk list | none | returned to the caller (privileged) | on demand |
| AI Guard voice session | near-end + far-end audio, transcript | `feedback01.tmp`, in-memory transcript | `/ai/audio/text/pcc/detection` every 10 s | during a call, up to 10 min |
| AI Guard video session | screen recording, 3 s clips | `clip01.tmp`, `cache/clip_*.tmp` | `/aiVideo/pcc/detection` every ~5 s | during a video call, up to 10 min |
| False-positive feedback | raw PCM + transcript, or MP4 | — | `/ai/audio/text/pcc/rbi`, `/aiVideo/pcc/rbi` | on user action |
| App process start | lifecycle, exceptions | OneTrack buffer | `tracking.miui.com` | batched |

The practical difference from the DM client: this package is **caller-driven**. Its collection
frequency is whatever the security centre asks for, and its most sensitive capture is bounded
by a call and by a 10-minute service lifetime. The DM client, by contrast, self-schedules on
boot and every 12/24 hours regardless of user activity.

---

## 5. What would change the conclusion

| Change | Effect |
|---|---|
| `Guard_Provider` downgraded to `normal`/`dangerous` | any app could query `content://guard` and call the scan APIs |
| `openFile` added to the provider | URI-based exfiltration of scan data |
| A `BOOT_COMPLETED` or `PACKAGE_ADDED` receiver added without permission | attacker-triggered scanning, timing oracle |
| AI Guard files moved to `getExternalFilesDir` | readable by storage-permission apps on API < 30 |
| `feedback01.tmp` retained unconditionally | long-lived recording of the last call |
