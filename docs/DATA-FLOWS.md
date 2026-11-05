# Data flows

All references point into `evidence/decompiled/`. The package runs as a platform-signed system app, so "permission granted" is the default state for most of the calls below.

## 1. Startup

```
GuardApplication.attachBaseContext()
  ├─ if user unlocked (g.b)  → f.b(context)      initialise engines / DB
  └─ else                    → register USER_UNLOCKED receiver
on USER_UNLOCKED            → f.b(context), unregister

GuardApplication.isAllowConnectNetwork(context)
  ├─ CN build:  network_status == 1  ||  persist.sys.sc_allow_conn
  └─ IN build:  com.miui.securitycenter installed && persist.sys.sc_allow_conn
                (jp_kd: also Settings.Secure.antivirus_engine_network_enable)
```

Two enforcement points use the same predicate: the HTTP helper `p146w1/b.c()` and an OkHttp interceptor `p086m1/e.a()`. When the flag is off, no first-party network request is attempted.

## 2. Scan → verdict

```
SecurityService (AIDL, Guard_Provider)
  ├─ S(...)  startScan(apkPaths, callback, cloud)
  ├─ k(...)  startScanAsScene(..., scene)
  ├─ R(str)  detectAppScan(package)
  ├─ z()     detectAppsScan()          → installed package list
  ├─ F()     detectAppsScan on server
  ├─ E(c)    updateScan(callback)
  ├─ y(c)    checkUpdate(callback)
  └─ N/U/b/e(str, callback)            Wi-Fi detect: network state / ARP / security / DNS+phishing
        │
        ▼
  g.y()  engine manager (single-letter package)
        ├─ MiEngine / MiEngineNG
        ├─ TencentEngine  → tmsdkobf / libTmsdk-2.0.16-mfr.so
        └─ AvlEngine      → libavlasys.so / libavlun*.so / assets/avlsdk
        │
        ├─ local verdict from signature/hash/behaviour
        └─ unknown package → cloud lookup: POST /virus/scan  (hashes only)
```

Local state:

| Store | Content |
|---|---|
| `EngineDatabase` (Room) | engine metadata, scan results |
| `mi_virus_white_db` | whitelist rows (`p152x1/f.java`) |
| `antivirus_db_version` | virus DB version + release time |
| `t_virus_white_list` | provider-visible whitelist table |
| app `filesDir` | AI Guard feedback artefacts (`aiguard/i.java`) |

## 3. Threat report

```
engine verdict = malicious
  → build ResultReportData(
        threatLevel, threatName, malwareCodeFeature, version,
        packageName, appName, versionCode,
        apkCertificateMD5, apkCertificateSHA256, mfFileSHA256,
        apkMd5, apkSha256, miuiVersion, androidVersion,
        device, uuid, hitFiles)
  → signGenInterceptor (UUID 62b36629-…)
  → encryptReq (AES-CBC, static key)
  → POST https://virus-engine.sec.miui.com/virus/localResult/report?timestamp=…
  → decryptResp
```

Only flagged applications are reported; the full installed inventory is not uploaded.

## 4. AI Guard

```
Security centre startService(AiGuardService, ai_guard_function_switch=true)
  → registerAudioRecordingCallback(callback)
  → on SOURCE_VOICE_CALL active:
        ASR (p098o1.b) on call audio
        → transcript
        → EncryptedData(version, session_public_key, key_id, digest, data_bytes)
        → POST /ai/audio/text/pcc/detection
        → EncryptedResponse{code,message,data,traceId}
        → if risky: AlertActivity (overlay warning)
        → optional feedback: /ai/audio/text/pcc/rbi, /aiVideo/pcc/rbi
```

Scene counters (`qqVideoDetectionTimes`, `qqVoiceDetectionTimes`, `wechatVideoDetectionTimes`, `wechatVoiceDetectionTimes`, `phoneVoiceDetectionTimes`) are maintained in the service and surfaced to the security centre. Consent and the master switch live in `com.miui.securitycenter`; this package only consumes the intent extra (default `false`) and shows the policy link in `PermissionRequestActivity`.

## 5. Telemetry

```
OneTrack.createInstance(context,
    Configuration(appId=31000401489 | 2882303761517522561,
                  channel=packageName, mode=APP,
                  autoTrackActivityAction=true,
                  exceptionCatcherEnable=true, GAIDEnable=false))
  → lifecycle / exception events
  → https://tracking.miui.com
```

`OneTrack.setAccessNetworkEnable(...)` is driven by the same `isAllowConnectNetwork()` predicate (`p065j1/a.java`, `p092n1/b.java`), so telemetry respects the region switch.

## 6. Third-party flow

```
TencentEngine → TMSDKContext
  ├─ imeiHash = ExtraInfoFetcher.getImeiHash()
  ├─ RCPC channel: rcpc-sdk-access.m.qq.com[:8002], v4/v6 variants
  └─ Wi-Fi portal: http://tools.3g.qq.com/wifi/cw.html
```

This flow is configured inside the SDK and is not gated by the Xiaomi signing scheme.

## 7. Cross-boundary summary

| Data | Leaves the device | Destination | Protection |
|---|---|---|---|
| Package name, version, APK hashes | yes, on cloud lookup | `virus-engine` | AES envelope (static key) + TLS |
| Flagged-app record with `uuid`, `hitFiles` | yes, on detection | `virus-engine` | same |
| Virus DB / whitelist | inbound | `api.sec.miui.com` | signed form, no response signature |
| Call transcript | yes, when AI Guard is on | `detection-aigc` | confidential computing + TLS |
| Lifecycle / exceptions | yes | `tracking.miui.com` | TLS |
| `imeiHash` | yes | Tencent RCPC | SDK-internal |
