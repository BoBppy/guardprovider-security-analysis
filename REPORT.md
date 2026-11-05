# System Security Components 2.5.6 — Security and Privacy Analysis

**Sample** `系统安全组件_2.5.6-20251106.0.apk`
**SHA-256** `b56c0b72ca5b442220b1c49632b7d70112813cb76cec9b1c21f4b5f49d77af92`
**Package** `com.miui.guardprovider` · versionCode 256 · minSdk 32 · targetSdk 36
**Analysis date** 2026-11-05
**Method** static reverse engineering (jadx 1.5.6), manifest and network-config inspection, signature/envelope reconstruction, non-intrusive endpoint probing. No device was available; no dynamic instrumentation.
**Artefact** HyperOS system package: on-device antivirus, URL/Wi-Fi detection and AI fraud detection, platform-signed and pre-installed.

---

## 0. Bottom line

This is the defensive counterpart to the carrier agent analysed in the companion repository: a platform-signed security component that scans applications with three engines, maintains cloud threat intelligence, and — when enabled by the security centre — listens to calls to detect fraud.

Its component surface is, unlike the companion sample, correctly protected: every custom exported entry point requires a `signature|privileged` permission. The weaknesses are in the **cryptographic design of its own cloud protocols**, not in its access control.

Seven judgements drive the assessment:

1. **The payload envelope key is inside the APK.** `J1/i.java` derives a 16-byte AES key from `META-INF/flamingo.dat` (offset 144, XOR with a constant). Every installation of this build shares the same key; the derived key's SHA-256 is `229c6c76…`. Encryption here is obfuscation, not confidentiality against anyone who possesses the APK.
2. **Request authentication is a hardcoded salt.** `sign = MD5(Base64(sorted(params) + "&" + "94e6cec2-19dc-4bb6-823c-613f72d3ea16"))`, with `mi` derived from `ANDROID_ID`. There is no per-device credential and no nonce. Possession of the APK plus any `ANDROID_ID` is sufficient to produce accepted requests.
3. **No certificate pinning.** Virus definitions, whitelists and scan verdicts travel over ordinary TLS with the platform trust store. For a security product that consumes remote rules, this is a thin margin.
4. **The whitelist has no signature check.** `p152x1/f.java` parses the downloaded JSON and replaces the whitelist table; a stub `b(String,String)` that always returns `false` is the remains of a removed integrity check.
5. **AI Guard records both sides of a call and the screen, then uploads them.** `p098o1/h.java` opens two `AudioRecord` streams — `VOICE_COMMUNICATION` (near end) and an `AudioPolicy` loopback with `ROUTE_FLAG_RENDER|ROUTE_FLAG_LOOP_BACK` (far end) — and averages them into one 16 kHz mono stream. In the video path `p079l1/a.java` drives `MediaRecorder` (H.264, 720×1280, 24 fps) over a `MediaProjection` obtained by reflection under `MANAGE_MEDIA_PROJECTION`, records **3 seconds**, and `AiGuardService.C()` base64-encodes the MP4 into `/aiVideo/pcc/detection`; the loop then waits 2 seconds and repeats. The transcript goes to `/ai/audio/text/pcc/detection` every 10 seconds. The envelope is materially stronger than the rest of the app, but it protects a far broader data path than a text-only design would.
6. **No unauthenticated read was found.** `api.sec.miui.com` answers `{"error_code":"10012","error":"Illegal request"}` without the signature parameters; `virus-engine` exposes `405 Allow: POST`. The endpoints enforce the scheme, but the scheme's secret is public.
7. **Local data is not reachable by an ordinary app.** Every custom export is behind `signature|privileged`, there is no `sharedUserId`, no external-storage write and no `openFile`; the AI Guard artefacts are exceptional in sensitivity, not in reachability. The contrast with the companion DM client is stark — see [`docs/LOCAL-DATA.md`](docs/LOCAL-DATA.md).

---

## 1. Scope and method

| Item | Value |
|---|---|
| Target | `系统安全组件_2.5.6-20251106.0.apk` (20,361,708 bytes) |
| Decompiler | jadx 1.5.6 (bundled JRE 25); 5,712 Java files, 42 decompilation errors |
| First-party code | 159 classes under `com.miui.guardprovider.*` |
| Third-party code | Tencent TMSDK (478 classes), OneTrack (178), AndroidX/Room/WorkManager/OkHttp/Gson |
| Native code | 12 ARM64 libraries, not reversed |
| Dynamic testing | none (no device/emulator) |
| Network testing | DNS, `HEAD /` on four hosts, `GET` on three documented paths without parameters; **no POST, no credential, no path enumeration** |
| Evidence | `evidence/` — manifest, 36 decompiled classes, string table, hashes, probe log |

---

## 2. Application profile

### 2.1 Identity and placement

- No `LAUNCHER` activity. The package is invisible to the user; its UI surfaces (`AlertActivity`, `PermissionRequestActivity`) exist only to warn or to request consent on behalf of the security centre.
- Two processes: the main process and `:ai` for the fraud-detection module.
- `targetSdk 36`, `minSdk 32`, `allowBackup=false`, `largeHeap=true`, `extractNativeLibs=false`, `requestLegacyExternalStorage=true`.
- Network security config exists and is restrictive: cleartext permitted only for `staging.api.sec.miui.com` (not referenced anywhere in this build) and `tools.3g.qq.com` (Tencent SDK).

### 2.2 Signing

Same platform certificate as the companion DM client:

`CN=MIUI, OU=MIUI, O=Xiaomi` · RSA-2048 · SHA-1 `7B:6D:C7:07:9C:34:73:9C:E8:11:59:71:9F:B5:EB:61:D2:A0:32:25`

The MIUI subject is a legacy identity; HyperOS packages are signed with the same key. Because the package is platform-signed, its `signature|privileged` permission definition is meaningful: only platform-signed or privileged packages can obtain it.

### 2.3 Components

Full matrix: `evidence/components.csv`. Custom components:

| Component | Type | Exported | Permission | Function |
|---|---|---|---|---|
| `GuardProvider` | provider (`guard`) | yes | `Guard_Provider` | engine info, virus/whitelist tables, install/sync scan, `call("getRiskAppData")` |
| `manager.SecurityService` | service | yes | `Guard_Provider` | AIDL: start/cancel scan, check/update engines, URL scan, Wi-Fi ARP/DNS/phishing detection |
| `aiguard.AiGuardService` | service (`:ai`) | yes | `Guard_Provider` | AI fraud detection lifecycle, audio callback, ASR, cloud detection |
| `aiguard.PermissionRequestActivity` | activity (`:ai`) | yes | `Guard_Provider` | consent and permission prompts, privacy-policy link |
| `aiguard.AlertActivity` | activity (`:ai`) | no | — | warning dialog shown on detection |

Framework components (AndroidX WorkManager, Room, ProfileInstaller, Diagnostics) follow their standard export and permission rules.

**Assessment:** the exported surface is correctly gated. `Guard_Provider` is declared `protectionLevel=0x12` (signature|privileged), so a third-party application cannot bind, query or launch any of these components. This is the main structural difference from the companion sample, where the equivalent service was exported with no permission at all.

### 2.4 Permissions (37)

The high-risk set is consistent with an on-device security product and inconsistent with anything less:

```
CAPTURE_AUDIO_OUTPUT   CAPTURE_VOICE_COMMUNICATION_OUTPUT   CAPTURE_VIDEO_OUTPUT
RECORD_AUDIO           MANAGE_MEDIA_PROJECTION              MANAGE_EXTERNAL_STORAGE
MODIFY_PHONE_STATE     WRITE_SECURE_SETTINGS                READ_PRIVILEGED_PHONE_STATE
INTERACT_ACROSS_USERS  QUERY_ALL_PACKAGES                   SYSTEM_ALERT_WINDOW
com.miui.securitycenter.permission.READ_APP_BEHAVIORS
```

`CAPTURE_AUDIO_OUTPUT` and `CAPTURE_VOICE_COMMUNICATION_OUTPUT` are signature-level Android permissions granted to system packages; together with `READ_APP_BEHAVIORS` they place the component inside the platform's trust boundary.

---

## 3. What is collected

Field-level map: `evidence/data-fields.csv`.

### 3.1 Scan inventory (local)

For every installed package the engines compute package name, version code, APK path, signing-certificate MD5/SHA-256, manifest SHA-256, APK MD5/SHA-256. The cloud-scan request (`CloudScanApkInfo`) carries only:

`packageName, versionCode, apkCertificateMD5, apkCertificateSHA256, mfFileSHA256`

No APK content is uploaded — verification is hash-based. This is a proportionate design and worth stating explicitly, because "antivirus uploads your apps" is the common assumption.

### 3.2 Threat reports (`/virus/localResult/report`)

When an engine flags an application, `ResultReportData` is uploaded with:

`threatLevel, threatName, malwareCodeFeature, version, packageName, appName, versionCode, apkCertificateMD5, apkCertificateSHA256, mfFileSHA256, apkMd5, apkSha256, miuiVersion, androidVersion, device, uuid, hitFiles`

`hitFiles` is the list of matched paths inside the package; `uuid` links reports from the same installation over time; `device` is the model name. This is the only path on which the device's application inventory becomes visible to the backend — and only for applications that were flagged.

### 3.3 AI Guard

`AiGuardService` runs in `:ai` and is declared with `foregroundServiceType="0xA0"` — microphone (0x80) plus media projection (0x20). It is started by the security centre (`COMMAND 1` voice, `COMMAND 2` video, `COMMAND 3` toggle) and stops itself after `detectionServiceLifetimeThresholdInMinutes` (default 10).

**Voice path.** `p098o1/h.java:49` opens two recorders at once — `AudioRecord` source 7 (`VOICE_COMMUNICATION`, near end) and a far-end loopback built through the hidden `AudioPolicy` API (`p098o1/f.java:27`, route flags `RENDER|LOOP_BACK`) — and averages them sample by sample into one 16 kHz mono stream. The stream is posted to the bound Xiaomi recogniser (`p098o1/b.java`), accumulated into sentences, and flushed as text every `voiceDetectionGapInSeconds` (default 10 s). The `AudioRecordingCallback` is a collision guard, not the capture mechanism: `AiGuardService.n` aborts if another app already holds `VOICE_CALL`.

**Video path.** `p079l1/a.java` records the display with `MediaRecorder` (H.264, 720×1280, 24 fps, 1 Mbit/s) into 3-second files; `AiGuardService.C` base64-encodes each file and posts it. `p079l1/d.java:45` obtains the `MediaProjection` by reflection under `MANAGE_MEDIA_PROJECTION`, which bypasses the system consent dialog and the platform capture indicator.

Data leaving the device:

| Data | Form | Destination | When |
|---|---|---|---|
| conversation transcript | `{requestId,text}`, sealed | `/ai/audio/text/pcc/detection` | every 10 s while a call is active |
| screen clip | `{streamName:"clip.mp4",fileBase64,requestId}` | `/aiVideo/pcc/detection` | every ~5 s while a video call is active |
| raw call audio + transcript | `{requestId,audioPCM,audioText}` | `/ai/audio/text/pcc/rbi` | only when the user marks a false positive |
| screen clip (copy) | `{fileType:"MP4",fileBase64,requestId}` | `/aiVideo/pcc/rbi` | only when the user marks a false positive |

Locally, `files/feedback01.tmp` holds every PCM buffer and `files/clip01.tmp` holds a copy of a confirmed clip; both are deleted at service teardown unless a warning was raised (`AiGuardService.java:599`). Per-scene counters (WeChat/QQ/phone, voice/video) are kept for telemetry. The full chain is in [`docs/AI-GUARD.md`](docs/AI-GUARD.md); the envelope itself uses `secure_channel_session_public_key`, `key_id`, `digest` and `data_bytes`.

### 3.4 Telemetry

OneTrack is initialised twice:

| appId | Purpose | Configuration |
|---|---|---|
| `31000401489` | main package | `Mode.APP`, channel = package name, `autoTrackActivityAction=true` |
| `2882303761517522561` | AI module | plus `exceptionCatcherEnable=true`, `GAIDEnable=false` |

OneTrack collects lifecycle and exception events and writes to `tracking.miui.com`. Two identifiers for one package is a design detail worth noting: events from the AI module are attributed to a different app id than the rest of the component.

### 3.5 Third-party signals

Tencent TMSDK (`tmsdkobf`, 478 classes, `libTmsdk-2.0.16-mfr.so`) maintains its own channel to `rcpc-sdk-access.m.qq.com` (plus IPv4/IPv6 variants on port 8002) and derives an `imeiHash` through its `ExtraInfoFetcher`. The hash is not reversible in practice, but it is a persistent device-level identifier shared with a third party.

### 3.6 Local data at rest

| Data | Store | Format | Sensitivity |
|---|---|---|---|
| virus definitions, whitelist, engine metadata | `databases/flamingo.db` (Room) | SQLite | low |
| whitelist served through the provider | `t_virus_white_list` | SQLite | low |
| AI Guard daily counters | `shared_prefs/ai_guard_daily_report.xml` | XML | low |
| **raw call audio** | `files/feedback01.tmp` | PCM 16 kHz mono, plaintext | **high** |
| **screen recording** | `files/clip01.tmp`, `cache/clip_*.tmp` | MP4 H.264, plaintext | **high** |

No external storage is used, `allowBackup=false`, there is no `sharedUserId`, and
`android:debuggable` is absent. The two AI Guard artefacts are the only high-sensitivity
files; see F-12 for their retention behaviour.

### 3.7 Reachability for an ordinary app

Every custom exported component — `GuardProvider` (`content://guard`), `SecurityService`,
`AiGuardService`, `PermissionRequestActivity` — requires
`com.miui.guardprovider.permission.Guard_Provider` (`protectionLevel="0x12"`,
`signature|privileged`). The provider implements `query/insert/delete/call` but not
`openFile`, and does not set `grantUriPermissions`, so there is no descriptor or URI-grant
path. The remaining exports are AndroidX components protected by `DUMP` or
`BIND_JOB_SERVICE`.

An app with no permissions cannot read the databases, invoke a scan, or start AI Guard. The
full matrix is in [`docs/LOCAL-DATA.md`](docs/LOCAL-DATA.md).

---

## 4. When it collects and reports

| Trigger | Action | Endpoint |
|---|---|---|
| Engine update (manual or scheduled via `SecurityService`) | download virus DB and whitelist | `api.sec.miui.com` |
| Scan start (`startScan`, `startScanAsScene`, `install_scan`, `sync_scan`) | local engine run; cloud lookup for unknown packages | `virus-engine.sec.miui.com/virus/scan` |
| Threat detected | upload one `ResultReportData` per hit | `virus-engine.sec.miui.com/virus/localResult/report` |
| Fraud-flagged app | report package metadata | `fraud-detection.engine.miui.com/hyperos/fraud/app/report` |
| AI Guard enabled + call in progress | full-duplex audio capture (mic + far-end loopback); transcript flushed every 10 s | `detection-aigc.engine.miui.com/ai/audio/text/pcc/detection` |
| AI Guard enabled + video call in progress | 3 s screen recording, upload, 2 s idle, repeat for up to 10 min | `detection-aigc.engine.miui.com/aiVideo/pcc/detection` |
| User marks a detection as a false positive | full raw PCM + transcript, or the MP4 clip | `/ai/audio/text/pcc/rbi`, `/aiVideo/pcc/rbi` |
| App lifecycle / exception | OneTrack batch | `tracking.miui.com` |
| Tencent SDK internal schedule | RCPC queries | `rcpc-sdk-access.m.qq.com` |

Nothing in this package self-schedules. It declares no `BOOT_COMPLETED` receiver, no
`PACKAGE_ADDED` receiver and no periodic `WorkManager` request; the only worker is
`ReportWorker`, enqueued once per scan. Collection frequency is therefore whatever the
security centre asks for, and the one self-driven path — AI Guard — is bounded by a call and
by a 10-minute service lifetime (default). This is the structural difference from the DM
client, which wakes itself on boot and every 12/24 hours whether the user acts or not.

Network access is gated by `GuardApplication.isAllowConnectNetwork()`:

- China build: `persist.sys.sc_allow_conn` or a stored `network_status` flag set by the security centre;
- international build: requires `com.miui.securitycenter` to be installed plus the same flag, and for the `jp_kd` region additionally `Settings.Secure.antivirus_engine_network_enable`.

That is a real region-aware kill switch, and it is enforced both in the HTTP helper (`p146w1/b`) and in an OkHttp interceptor (`p086m1/e`).

---

## 5. How it reports

### 5.1 Signed form POST (threat intelligence)

`p146w1/b.java`:

```
params  = {device, carrier, region, miuiVersion, versionType, appVersion, mi}
mi      = MD5(MD5(ANDROID_ID + "-" + SALT))
sign    = MD5_hex_upper(Base64("k1=v1&k2=v2&…" sorted + "&" + SALT))
body    = application/x-www-form-urlencoded(params + sign)
```

`tools/sign_request.py` reproduces the canonical string and digest offline. The salt is `94e6cec2-19dc-4bb6-823c-613f72d3ea16`, present in the APK.

### 5.2 AES envelope (cloud scan, fraud report)

`J1/i.java` + `F1/j.java` + `F1/n.java`:

```
key    = AES-128 key from META-INF/flamingo.dat[144:160] XOR 0x4312…   (static)
iv     = 16 random bytes, prepended to the ciphertext
body   = AES/CBC/PKCS5(key, iv || plaintext)
header = EncryptVersion
```

Responses are decrypted with the same key; `F1/n.java` throws if `EncryptVersion` is missing on a 200 response. There is no MAC and no response signature.

`tools/extract_crypto.py` derives the key and prints its SHA-256 (`229c6c76…`) without emitting the key itself.

### 5.3 Confidential-computing envelope (AI Guard)

`EncryptedData` carries a version, a session public key, a `key_id`, a digest and the ciphertext. The actual sealing is delegated to `N1.a` (a confidential-computing binder service), so the key does not live in the APK. This is the only scheme in the package that is not defeated by possession of the APK.

Payloads: `{requestId,text}` for audio detection, `{streamName:"clip.mp4",fileBase64,requestId}` for video detection, `{requestId,audioPCM,audioText}` and `{fileType:"MP4",fileBase64,requestId}` for feedback. Base64 is applied before sealing, so a 3-second 1 Mbit/s clip is ≈375 KB on disk and ≈500 KB in the JSON body.

### 5.4 Response handling

- `p146w1/b.c()` returns `null` on any non-200 and logs the exception; there is no retry policy in the helper.
- `p152x1/f.c()` accepts the whitelist JSON, deletes the table and re-inserts — **without verifying a signature**.
- `F1/n` decrypts but does not authenticate the plaintext.

---

## 6. Endpoints

| Host | Paths | Purpose | Observed |
|---|---|---|---|
| `api.sec.miui.com` | `/antivirus/viruses`, `/antivirus/whitelist` | virus DB and whitelist distribution | 200 + `error_code 10012` without signature |
| `virus-engine.sec.miui.com` | `/virus/scan`, `/virus/localResult/report` | cloud lookup, threat reporting | root 404; GET on scan → 405 `Allow: POST` |
| `fraud-detection.engine.miui.com` | `/hyperos/fraud/app/report` | fake/fraud app reporting | root 404 |
| `detection-aigc.engine.miui.com` | `/ai/audio/text/pcc/detection`, `/ai/audio/text/pcc/rbi`, `/aiVideo/pcc/rbi` | AI fraud detection | root 404 |
| `rcpc-sdk-access.m.qq.com` (+ v4/v6 on :8002) | Tencent RCPC | TMSDK cloud queries | not probed |
| `http://tools.3g.qq.com` | `/wifi/cw.html`, `/j/sslstrip` | Tencent Wi-Fi portal checks | not probed (cleartext) |
| `tracking.miui.com` | OneTrack | analytics | not probed |

All four Xiaomi hosts sit behind an openresty gateway and answer JSON. `virus-engine` returns a Spring-style 405 with `Allow: POST`, which identifies the backend family; the gateway does not hide it.

Raw transcript: `evidence/network/probe-log.txt`.

---

## 7. Authentication

The package uses three different schemes, none of which authenticates the *device*:

| Scheme | Secret | Strength | Replayable |
|---|---|---|---|
| Signed form POST | hardcoded salt in APK | MD5, no nonce; forgeable by anyone with the APK | yes |
| AES envelope | static key derived from APK asset | no MAC; forgeable and decryptable offline | yes |
| Confidential computing | per-session key, key id | strongest; key not in APK | unknown (server-side session) |

`api.sec.miui.com` does enforce the signature: a request without parameters returns `error_code 10012`. That is a real check, but it verifies possession of a public constant, not the identity of a handset.

The `mi` parameter binds the request to `ANDROID_ID`, which is stable per app-signing-key/device pair and readable by the app. It is not a secret and does not prevent a party who knows a device's `ANDROID_ID` from impersonating it.

---

## 8. Can data be obtained from the endpoint?

**No read interface was found.** The client only calls scan/update/report paths; there is no query, list or export API in 159 first-party classes, and no alternate host.

**Unauthenticated access is rejected.** `GET /antivirus/viruses` and `/antivirus/whitelist` without parameters return `{"error_code":"10012","error":"Illegal request"}`. `GET /virus/scan` returns 405. No threat data, device records or scan results were obtained without credentials, and no forged signature was attempted.

**Write abuse is possible in principle.** Because the signing salt and the envelope key are both in the APK, a party who can reach the endpoints can produce requests that pass the checks. What that yields is bounded by the API surface: virus-list retrieval, cloud-scan verdicts for hashes they supply, and submission of threat records. It does not expose other devices' data, because no endpoint returns per-device records.

**Server-side storage is out of scope.** Retention, access control and internal consoles cannot be assessed from the APK.

---

## 9. Can traffic be intercepted?

### 9.1 Passive

All first-party traffic is HTTPS with the platform trust store; there is no `CertificatePinner` anywhere in the application code (only OkHttp's internal trust-manager adapters). A passive network observer sees TLS only.

Inside TLS, the signed POSTs carry `device`, `carrier`, `region`, `miuiVersion` and the `mi` hash in clear — enough to fingerprint a handset and its firmware.

### 9.2 Active

A TLS-terminating adversary (user-installed CA, corporate proxy, compromised CA) can:

- read cloud-scan requests and threat reports after extracting the static AES key from the APK;
- **alter the virus database and whitelist** before they are stored, because responses are neither signed nor MACed and the whitelist loader has a disabled integrity stub;
- drop or replay scan requests; nothing in the protocol carries a nonce.

The practical impact of rule tampering is detection suppression: a whitelist entry is exactly what makes a scanner ignore a package. This is why pinning and response signing matter more for a security component than for an ordinary app.

### 9.3 AI Guard

The confidential-computing envelope is not readable from the APK; interception there depends on the key-management service. The remaining exposure is metadata (which engine host, when, payload size) and the fact that the feature exists at all.

---

## 10. Findings

Severity is a five-band scale; CVSS v3.1 vectors are indicative.

### F-01 — Static AES key protects cloud-scan and fraud-report bodies · **High** · CVSS 7.4 `AV:N/AC:H/PR:N/UI:N/S:U/C:H/I:H/A:N`

The key is derived from `META-INF/flamingo.dat` and identical on every installation. Encryption therefore protects against nothing more than casual inspection of a capture; it does not protect data at rest on the server, and it does not survive an adversary who has the APK.
**CWE-321** Use of Hard-coded Cryptographic Key
**Fix** derive per-install keys in the TEE/Keystore, use AES-GCM with an authenticated header, or drop the envelope and rely on TLS with pinning.

### F-02 — Hardcoded signing salt, MD5, no nonce · **High** · CVSS 7.5 `AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:H/A:N`

`sign = MD5(Base64(sorted(params) + "&" + SALT))` with the salt compiled into the APK. The endpoint enforces the check but the secret is public; requests are replayable.
**CWE-798** Use of Hard-coded Credentials · **CWE-328** Weak Hash
**Fix** per-device key in secure hardware, HMAC-SHA-256, server-issued nonce and timestamp window.

### F-03 — No certificate pinning for security-critical downloads · **Medium** · CVSS 5.9

No `CertificatePinner`; virus definitions and whitelists are accepted over the platform trust store.
**CWE-295** Improper Certificate Validation
**Fix** pin the four Xiaomi hosts, with a rotation plan; fail closed on pin mismatch for rule downloads.

### F-04 — Whitelist accepted without integrity verification · **Medium** · CVSS 5.3

`p152x1/f.c(String)` parses JSON, wipes `mi_virus_white_db` and inserts the new rows. `b(String,String)` — apparently the signature check — is hardcoded to `false` and unused.
**CWE-345** Insufficient Verification of Data Authenticity
**Fix** sign the whitelist with an offline key and verify before replacing the table; refuse updates that fail.

### F-05 — Full-duplex call audio capture and upload · **High** (privacy) · CVSS 7.1 `AV:N/AC:L/PR:H/UI:N/S:C/C:H/I:N/A:N`

`p098o1/h.java:49` opens two recorders at once: `AudioRecord` source `VOICE_COMMUNICATION` (near end) and a loopback `AudioRecord` built through the hidden `AudioPolicy` API with `ROUTE_FLAG_RENDER|ROUTE_FLAG_LOOP_BACK` (`p098o1/f.java:27`). Both streams are averaged sample by sample into one 16 kHz mono channel, so **the remote party is captured as well as the device owner**. The mixed stream is pushed to the bound Xiaomi recogniser (`p098o1/b.java`, `postData`) and flushed as text to `/ai/audio/text/pcc/detection` every 10 s; every PCM buffer is also appended to `files/feedback01.tmp`, which is uploaded in full only if the user later marks a false positive (`AlertActivity.java:170`).

The pipeline depends on `CAPTURE_AUDIO_OUTPUT`, `CAPTURE_VOICE_COMMUNICATION_OUTPUT` and `MODIFY_AUDIO_ROUTING` — all `signature|privileged`. A normal app cannot construct it even with `RECORD_AUDIO`.
**CWE-359, CWE-693**
**Fix** require a per-call opt-in, show a capture indicator for the duration, delete `feedback01.tmp` unconditionally, and state which party's audio is transmitted to which service.

### F-06 — Third-party engine with its own network channel · **Medium**

Tencent TMSDK (478 classes, native library, `assets/mdetector`) talks to `rcpc-sdk-access.m.qq.com` and derives `imeiHash`. Its traffic is outside the Xiaomi signing scheme and is not routed through the intercepted OkHttp client that enforces the region switch.
**CWE-1104** Use of Unmaintained Third Party Components
**Fix** document the third-party flow in the privacy notice, confirm a data-processing agreement, and confirm that the SDK honours the region kill switch.

### F-07 — Cleartext exceptions, one of them dead · **Low**

`network_security_config` permits cleartext for `staging.api.sec.miui.com` (zero references in this build) and `tools.3g.qq.com` (Tencent portal check).
**CWE-319**
**Fix** remove the staging entry from production builds; constrain the Tencent entry to the exact host and path.

### F-08 — Stale legacy storage flag · **Low**

`requestLegacyExternalStorage=true` is ignored at targetSdk 36 and coexists with `MANAGE_EXTERNAL_STORAGE`.
**CWE-732**
**Fix** remove the flag; scope all-files access to the scan workflow.

### F-09 — Exported components are correctly protected · **Informational (positive)**

Every custom exported component requires `Guard_Provider` (`signature|privileged`). No permission-less IPC surface was found — a deliberate contrast with the companion DM client.

### F-10 — Two OneTrack appIds in one package · **Informational**

`31000401489` for the main component, `2882303761517522561` for the AI module. Events are split across two analytics identities; this should be reflected in the privacy notice and in any data-map.

### F-11 — Screen recording without the system capture indicator · **High** (privacy) · CVSS 7.1 `AV:N/AC:L/PR:H/UI:N/S:C/C:H/I:N/A:N`

`p079l1/d.java:45` obtains a `MediaProjection` by reflecting into `IMediaProjectionManager.createProjection()` instead of calling `createScreenCaptureIntent()`. That path is gated by `MANAGE_MEDIA_PROJECTION` (`signature`), so the platform consent dialog and the “screen is being recorded” indicator never appear. `p079l1/a.java` then records the whole display with `MediaRecorder` (H.264, 720×1280, 24 fps, 1 Mbit/s) in 3-second clips; `AiGuardService$p` uploads each clip to `/aiVideo/pcc/detection` and repeats every 5 seconds for up to 10 minutes (`AiGuardService.java:1466`).

The foreground notification reads “通话防护中 / Call protection is on” and does not mention capture. The one-time AI Guard notice does disclose “video frames or clips”, so the collection is disclosed at enrolment — but the user cannot tell, at any given moment, that the screen is being recorded.

**CWE-359, CWE-693**
**Fix** use the system consent flow per session, surface the platform capture indicator, and make the notification state what is being recorded.

### F-12 — Raw call audio and screen clips survive on disk · **Medium** (privacy) · CVSS 4.1 `AV:L/AC:H/PR:H/UI:N/S:U/C:H/I:N/A:N`

`aiguard/i.java` writes every PCM buffer to `files/feedback01.tmp` and copies a confirmed
screen clip to `files/clip01.tmp`, both plaintext. Deletion is conditional: the service
removes them at teardown only when no alert was raised (`AiGuardService.java:599`); if a
warning was displayed and the user does not act, they remain until the next capture truncates
them. A rooted device, a forensic image, or any process that shares the UID can recover the
last call's audio — including the remote party — and the last recorded screen content. The
`.tmp` extension does not make the file ephemeral.

**CWE-359, CWE-212** Improper Removal of Sensitive Information Before Storage
**Fix** delete both files immediately after upload or dismissal, keep audio memory-only, and
encrypt at rest with a Keystore-wrapped key.

---

## 11. Privacy and compliance observations

1. **Purpose limitation.** Scanning installed packages and reporting only flagged ones is proportionate; full-inventory reporting does not occur.
2. **Transparency.** The package is invisible. AI Guard discloses collection once at enrolment (`ai_guard_privacy_policy_message`, plus a screen-recording dialog) and posts a persistent “call protection” notification — but neither the notification nor any system indicator tells the user that capture is active on a given call, and the remote party is never informed.
3. **Third-party disclosure.** Location of the risk decision is Xiaomi, but the scanning engine and one network channel are Tencent; the SDK's `imeiHash` is a third-party identifier.
4. **Data minimisation.** Cloud scan is hash-based (good). Threat reports include `uuid`, `device`, `hitFiles` and full APK hashes (necessary for triage, but retained server-side beyond audit). AI Guard is the outlier: it uploads complete 3-second screen clips every 5 seconds, and on feedback the entire raw call audio — the least minimised data in the package.
5. **Retention.** Local databases and logs were not exhaustively mapped; server-side retention is unknown.
6. **Region gating.** `isAllowConnectNetwork()` is a genuine control, but it exists to satisfy regional policy, not user choice.

---

## 12. Comparison with the companion sample

| Dimension | `com.mobiletools.systemhelper` (DM client) | `com.miui.guardprovider` (security component) |
|---|---|---|
| Signer | same platform certificate | same platform certificate |
| Exported components | service and receivers with **no permission** | all custom exports behind `signature\|privileged` |
| Transport | plaintext HTTP, port 18080 | HTTPS, no pinning |
| Payload protection | MD5→AES-ECB, RSA-1024 public key | static AES-CBC key in APK; confidential computing for AI |
| Client authentication | none (public-key "signature") | hardcoded salt + MD5, no nonce |
| Consent | `device_provisioned` only | delegated to security centre; feature flag off by default |
| Data reported | IMEI, IMSI, ICCID, location, app inventory | scan metadata, flagged threats, call transcripts, 3 s screen clips, raw PCM on feedback |
| Endpoint read-back | blocked by WAF URL ACL | application-level 10012 |

The pair shows two different failure modes in the same OEM image: the carrier agent fails on **access control and transport**, the security component fails on **cryptographic design**. Both rely on secrets that ship inside the APK.

---

## 13. Limitations

- **No dynamic analysis.** Permissions actually granted, engine behaviour at runtime and AI Guard activation were not observed.
- **Native engines not reversed.** TMSDK, Antiy AVL and Flamingo are stripped/packed; their internal collection is inferred from Java shims, assets and strings.
- **No POST was sent.** Statements about request acceptance rest on code and on the endpoint's response to parameterless GETs.
- **Single artefact.** Findings describe `2.5.6-20251106.0`; other builds and regions may differ.
- **Obfuscation.** Business logic is split between `com.miui.guardprovider.*` and single-letter packages (`p146w1`, `p152x1`, `J1`, `F1`); mappings were reconstructed manually.

---

## 14. Disclosure path

This package is a much weaker CVE/CNVD candidate than the companion DM client, because its
access-control surface is correct. The findings are cryptographic design and privacy
engineering, not exploitable defects.

| Finding | Class | CVE | CNVD | Reasoning |
|---|---|---|---|---|
| F-12 plaintext call audio / clips | CWE-359, CWE-212 | no | possible, low | local-at-rest exposure; requires root or UID sharing |
| F-11 capture without platform indicator | CWE-359 | no | no | the capability is granted by a signature permission; closed as "by design" |
| F-05 full-duplex audio capture | CWE-359 | no | no | disclosed feature, consent collected at enrolment |
| F-01 static AES key | CWE-321 | no | borderline | key recovery is real, but there is no read endpoint to attack |
| F-02 hardcoded signing salt | CWE-798 | no | borderline | lets anyone mint requests; no user-side impact demonstrated |
| F-04 whitelist without signature | CWE-345 | unlikely | borderline | needs TLS interception to exploit |
| F-03 no pinning | CWE-295 | no | no | hardening gap |

The honest framing: **submitting these as vulnerabilities would likely fail**, and a failed
submission weakens the credibility of the privacy analysis. The better route for this package
is a **privacy complaint** (PIPL / app-store review) citing F-05, F-11 and F-12, plus a
coordinated note to the vendor PSIRT about the plaintext retention.

---

## Appendix A — Indicators

```
package      com.miui.guardprovider
sha256       b56c0b72ca5b442220b1c49632b7d70112813cb76cec9b1c21f4b5f49d77af92
signer sha1  7B:6D:C7:07:9C:34:73:9C:E8:11:59:71:9F:B5:EB:61:D2:A0:32:25
host         api.sec.miui.com                 (HTTPS)
host         virus-engine.sec.miui.com        (HTTPS)
host         fraud-detection.engine.miui.com  (HTTPS)
host         detection-aigc.engine.miui.com   (HTTPS)
host         rcpc-sdk-access.m.qq.com:8002    (Tencent, HTTPS)
host         tools.3g.qq.com                  (HTTP allowed)
host         tracking.miui.com                (OneTrack)
provider     content://guard/…
permission   com.miui.guardprovider.permission.Guard_Provider
static key   sha256 229c6c761bbca31cd8176c8727abe3f275cf4420897e709503ffbef1dbc5113f
```

## Appendix B — Evidence index

```
evidence/AndroidManifest.xml            decoded manifest
evidence/manifest-summary.txt           package, SDK levels, 37 permissions, 3 declarations
evidence/components.csv                 19 components with export and permission
evidence/data-fields.csv                field-level collection map
evidence/hashes.txt                     hashes, signer, static key fingerprint
evidence/strings/dex-strings.txt        55,409 printable strings
evidence/strings/urls.txt               20 URIs
evidence/decompiled/                    36 classes referenced by this report
evidence/network/probe-log.txt          endpoint probe transcript
tools/sign_request.py                   offline MD5 signature reproduction
tools/extract_crypto.py                 static AES key derivation (fingerprint only)
docs/WIRE-FORMAT.md                     signature, envelope, endpoint formats
docs/DATA-FLOWS.md                      scan → report → AI guard paths
docs/COMPONENTS.md                      component surface and protection levels
docs/AI-GUARD.md                       AI Guard capture chain, cadence and disclosure analysis
docs/METHOD.md                          tooling and reproduction
docs/LOCAL-DATA.md                      at-rest data, reachability, privilege surface
```

---

*Prepared from a single APK supplied for analysis. No production system was modified, no credential was used, and no payload was submitted to any endpoint.*
