# Component surface

Source: `evidence/AndroidManifest.xml`, `evidence/components.csv`.
Threat model: a locally installed third-party application **without** the `com.miui.guardprovider.permission.Guard_Provider` permission — i.e. an ordinary app on the same device.

---

## 1. Permission boundary

```xml
<permission android:name="com.miui.guardprovider.permission.Guard_Provider"
            android:protectionLevel="0x00000012"/>
```

`0x12` = `signature | privileged` (0x02 + 0x10). Only packages signed with the platform key, or installed in `priv-app`, can hold it. Every custom exported component requires it.

This is the single most important structural difference from the companion DM client (`com.mobiletools.systemhelper`), whose exported service and receivers carried no permission at all.

---

## 2. Custom components

### 2.1 `GuardProvider` — content provider, authority `guard`

| Property | Value |
|---|---|
| Exported | yes |
| Permission | `Guard_Provider` |
| Authority | `guard` |

Registered URIs (`GuardProvider.<clinit>`):

```
guard/engine_info_list     1005
guard/network_set          1010
guard/sync_scan            1006
guard/virus                1007
guard/install_scan         1008
guard/support_incremental  1009
```

`call("getRiskAppData", …)` returns a Bundle built from `getInstalledPackages(0)` plus risk rows. `insert/delete/update/query` operate on `t_virus_white_list`.

**Assessment:** reachable only by platform-signed callers; the provider does not expose file paths to arbitrary apps.

### 2.2 `manager.SecurityService` — AIDL

| Property | Value |
|---|---|
| Exported | yes |
| Permission | `Guard_Provider` |
| Actions | `com.miui.guardprovider.action.antivirusservice`, `…action.urlscan` |

Methods (from `SecurityService$a`):

```
E(UpdateCallback)                 updateScan
F()                               detectAppsScan
K(int)                            cancelScan
M(List) / p(List)                 query results
N(String, DetectCallback)         wifiDetectNetworkState
O(int)                            cancelUpdate
R(String)                         detectAppScan(package)
S(String[], ScanCallback, bool)   startScan
U(String, DetectCallback, String) wifiDetectArp
Z(String)                         getEngineEnable
b/e/U/...                         wifiDetectSecurity / DnsAndPhishing / Arp
d()                               engine name list
f(String, bool)                   setEngineEnable
i0(String)                        isSupportCloudScan
k(String[], ScanCallback, bool, int) startScanAsScene
n(String)                         getNameString
y(ScanCallback)                   checkUpdate
z()                               installed package paths
```

The second binder (`h1.b`) exposes `scanURL(String,String)` and `updateURLRule(String)`.

**Assessment:** an antivirus control plane, correctly gated. A third-party app cannot start a scan, read results or toggle engines.

### 2.3 `aiguard.AiGuardService` — `:ai` process

| Property | Value |
|---|---|
| Exported | yes |
| Permission | `Guard_Provider` |
| Foreground type | `0xA0` (microphone + media projection bits) |

Responsibilities: audio-recording callback, ASR lifecycle, conversation assembly, cloud detection, alert dispatch, scene counters.

**Assessment:** the highest-risk capability in the package. The IPC gate is correct; the risk is the data itself, not the exposure to other apps.

### 2.4 `aiguard.PermissionRequestActivity` — `:ai`

Exported with `Guard_Provider`, `excludeFromRecents=true`. Displays consent/permission prompts and links to `https://privacy.mi.com/AIalert/<lang>_<region>`.

### 2.5 `aiguard.AlertActivity` — `:ai`

Not exported, `excludeFromRecents=true`. Warning dialog with cancel/confirm actions.

---

## 3. Framework components

| Component | Exported | Protection | Note |
|---|---|---|---|
| `androidx.work…SystemJobService` | yes | `BIND_JOB_SERVICE` | standard WorkManager |
| `androidx.work…DiagnosticsReceiver` | yes | `android.permission.DUMP` | standard |
| `androidx.profileinstaller.ProfileInstallReceiver` | yes | `android.permission.DUMP` | standard |
| other WorkManager receivers/services | no | — | standard |
| `androidx.startup.InitializationProvider` | no | — | standard |
| `androidx.room.MultiInstanceInvalidationService` | no | — | standard |

No component exposes an unprotected surface.

---

## 4. Attack-surface matrix

| # | Component | Reachable by ordinary app | Precondition | Residual risk |
|---|---|---|---|---|
| 1 | `GuardProvider` | no | platform signature / priv-app | low |
| 2 | `SecurityService` | no | platform signature / priv-app | low |
| 3 | `AiGuardService` | no | platform signature / priv-app | low (IPC), high (data) |
| 4 | `PermissionRequestActivity` | no | platform signature / priv-app | low |
| 5 | `AlertActivity` | no | — | none |
| 6 | framework components | as designed | `DUMP` / `BIND_JOB_SERVICE` | low |

**Conclusion:** the exported surface is not the problem. The risks in this package are in the cloud protocols and in the sensitivity of the AI Guard data path, both analysed in `REPORT.md`.

---

## 5. Defence lessons

1. `protectionLevel=signature|privileged` on every exported component is the correct pattern for an OEM security service, and it scales: none of the 19 components needed `exported=false` for safety.
2. The permission is declared, used and not over-broad; the package does not define a world-accessible provider.
3. The remaining exposure is data-plane: what the component sends and how it protects it.
