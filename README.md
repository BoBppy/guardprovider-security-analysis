# System Security Components 2.5.6 — Security and Privacy Assessment of a HyperOS System Package

A reverse-engineering study of `com.miui.guardprovider` ("系统安全组件"), the anti-malware and fraud-detection component that ships inside HyperOS. The package runs with platform privileges, scans installed applications with three engines (Xiaomi, Tencent, Antiy), downloads threat intelligence from Xiaomi endpoints, and — in its AI Guard mode — records both sides of a call and 3-second screen clips during video calls, then uploads them for fraud analysis.

This repository contains the full report, decompiled evidence, reconstructed request formats, endpoint observations and the tooling needed to reproduce every claim.

## Provenance

- **Source**: the APK was supplied as an image extracted from a Xiaomi 17 running HyperOS.
- **Corroboration**: it is signed with the Xiaomi platform certificate (`CN=MIUI`, the legacy subject retained by HyperOS), references `miui.os.Build`, `com.miui.securitycenter.*` and HyperOS-only settings keys, and has no `LAUNCHER` activity — a pre-installed system component, not a user application.
- **Relationship to the companion sample**: the same signing certificate signs `SystemHelper 3.0.1` (`com.mobiletools.systemhelper`), the carrier DM client analysed in the sibling repository. That makes the two artifacts a useful pair: one is a carrier agent embedded in an OEM image, the other is the OEM's own security component.

The companion repository `../systemhelper-security-analysis` analyses the carrier DM client with the same signer.

The research value is a documented, permission-level view of **what a platform-signed security package can reach, what it sends to the cloud, and how it protects that traffic** — relevant to mobile privacy auditing, OEM pre-installation review and threat-intelligence supply-chain analysis.

## Sample identity

| Field | Value |
|---|---|
| Application label | `System security components` (系统安全组件) |
| Package name | `com.miui.guardprovider` |
| Version name | `2.5.6-20251106.0` |
| Version code | `256` |
| Build date (from version string) | 2025-11-06 |
| minSdkVersion | 32 (Android 12L) |
| targetSdkVersion | 36 (Android 16) |
| compileSdkVersion | 36 |
| APK size | 20,361,708 bytes |
| APK SHA-256 | `b56c0b72ca5b442220b1c49632b7d70112813cb76cec9b1c21f4b5f49d77af92` |
| APK MD5 | `a9774e719d6246bfbc2b6ca23793380b` |
| DEX | 1 × `classes.dex` (7,436,564 B) |
| ABI | `arm64-v8a` only |
| Native libraries | 12 (TMSDK, Antiy AVL, Flamingo, xCrash, Turing, URL detector) |
| Permissions | 37 (see below) |
| Declared permissions | 3 — `Guard_Provider` (signature\|privileged), `TMF_SHARK` (signature), dynamic-receiver permission |
| Components | 2 activities, 6 services, 9 receivers, 2 providers (19) |
| Launcher activity | **none** |
| Processes | main, `:ai` |
| Network security config | present; cleartext allowed only for `staging.api.sec.miui.com` and `tools.3g.qq.com` |
| `allowBackup` | `false` |

### Signing

| Field | Value |
|---|---|
| Subject / Issuer | `EMAILADDRESS=miui@xiaomi.com, CN=MIUI, OU=MIUI, O=Xiaomi, L=Beijing, ST=Beijing, C=CN` |
| Serial | `0xe552a8ecb9011b7c` |
| Validity | 2011-12-06 → 2039-04-23 |
| Key | RSA 2048, `sha1WithRSAEncryption` |
| SHA-1 | `7B:6D:C7:07:9C:34:73:9C:E8:11:59:71:9F:B5:EB:61:D2:A0:32:25` |
| SHA-256 | `C9:00:9D:01:EB:F9:F5:D0:30:2B:C7:1B:2F:E9:AA:9A:47:A4:32:BB:A1:73:08:A3:11:1B:75:D7:B2:14:90:25` |

The certificate subject still says MIUI because Xiaomi has kept the platform key across generations; HyperOS builds are signed with the same identity.

### High-risk permissions

```
CAPTURE_AUDIO_OUTPUT          CAPTURE_VOICE_COMMUNICATION_OUTPUT
CAPTURE_VIDEO_OUTPUT          RECORD_AUDIO
MANAGE_MEDIA_PROJECTION       FOREGROUND_SERVICE_MEDIA_PROJECTION
MANAGE_EXTERNAL_STORAGE       MODIFY_PHONE_STATE
WRITE_SECURE_SETTINGS         READ_PRIVILEGED_PHONE_STATE
INTERACT_ACROSS_USERS         QUERY_ALL_PACKAGES
SYSTEM_ALERT_WINDOW           com.miui.securitycenter.permission.READ_APP_BEHAVIORS
```

A full list is in `evidence/manifest-summary.txt`; the machine-readable profile is `metadata/app-profile.json`.

## Bundled SDKs

| SDK | Version | Footprint | Network |
|---|---|---|---|
| Tencent TMSDK | 2.0.16 | `libTmsdk-2.0.16-mfr.so`, 478 `tmsdkobf` classes, `assets/mdetector` | `rcpc-sdk-access.m.qq.com`, `tools.3g.qq.com` |
| Antiy AVL | unknown | `libavlasys.so`, `libavlm.so`, `libavlurl.so`, `assets/avlsdk`, encrypted `licence.conf` | via the engine, not separately observable |
| Xiaomi OneTrack | bundled | 178 classes, two appIds | `tracking.miui.com` |
| Flamingo / AiCapability | unknown | `libflamingo.so`, `META-INF/flamingo.dat` | cloud scan / fraud detection |
| xCrash | unknown | `libxcrash*.so` | local only |
| Kotlin / AndroidX / Room / WorkManager / OkHttp / Gson | current | see package layout | — |

## Key findings

| ID | Severity | Finding |
|---|---|---|
| F-01 | High | The AES key protecting cloud-scan and fraud-report bodies is derived from `META-INF/flamingo.dat` and is identical on every installation — decryption does not require the server. |
| F-02 | High | Request signing uses a hardcoded salt (`94e6cec2-…`) and MD5 over sorted parameters, with no nonce and no per-device credential. Anyone with the APK can mint a valid request. |
| F-03 | Medium | No certificate pinning; virus definitions and whitelists are downloaded and applied on the strength of the TLS PKI alone. |
| F-04 | Medium | The whitelist is accepted without signature verification — a stub method `b(String,String)` returns `false` and is unused, suggesting a removed integrity check. |
| F-05 | High | AI Guard records **both sides** of a call — `VOICE_COMMUNICATION` plus an `AudioPolicy` loopback — mixes them to 16 kHz mono, and sends the transcript to Xiaomi every 10 s; raw PCM is retained locally and uploaded if the user marks a false positive. |
| F-11 | High | Screen capture is taken through a reflective `MediaProjection` under `MANAGE_MEDIA_PROJECTION`, bypassing the system consent dialog and capture indicator; 3-second MP4 clips are uploaded every 5 seconds. |
| F-06 | Medium | Tencent TMSDK is bundled with its own network channel and hashed IMEI collection. |
| F-07 | Low | Cleartext is permitted for an unused staging host and for `tools.3g.qq.com`. |
| F-08 | Low | `requestLegacyExternalStorage` is stale at targetSdk 36 and sits alongside `MANAGE_EXTERNAL_STORAGE`. |
| F-09 | Info | Exported components are correctly protected by a signature\|privileged permission — the opposite of the companion DM client. |
| F-10 | Info | OneTrack analytics is initialised with two appIds (main package and AI module). |

Direct answers to the four questions:

1. **What is collected** — installed-app metadata and hashes during scans; detected-threat records (package, certificate hashes, APK hashes, hit files, device uuid); AI Guard conversation text, 3-second screen clips, and raw call audio on feedback; OneTrack lifecycle and crash events; TMSDK device signals. → [§3](REPORT.md#3-what-is-collected)
2. **How and when it is reported** — on scan events and scheduled engine updates; signed form POSTs and AES-enveloped JSON; OneTrack batches. → [§4](REPORT.md#4-when-it-collects-and-reports), [§5](REPORT.md#5-how-it-reports)
3. **Endpoints and authentication** — four Xiaomi hosts plus Tencent; static-salt MD5 signature, static AES key, confidential-computing envelope for AI data. No per-device authentication. → [§6](REPORT.md#6-endpoints), [§7](REPORT.md#7-authentication)
4. **Can data be read back or intercepted** — no read API was found; unauthenticated requests are rejected with `error_code 10012`; traffic is HTTPS but unpinned, and the envelope key is in the APK, so a TLS-terminating adversary could read and alter scan telemetry and rule responses. → [§8](REPORT.md#8-can-data-be-obtained-from-the-endpoint), [§9](REPORT.md#9-can-traffic-be-intercepted)

## Repository layout

```
README.md                  this file
REPORT.md                  full report (English)
REPORT.zh-CN.md            full report (Chinese)
CHANGELOG.md               revision history
metadata/
  app-profile.json         machine-readable sample profile
docs/
  METHOD.md                tooling, commands, reproduction
  WIRE-FORMAT.md           signature, envelope and endpoint formats
  DATA-FLOWS.md            scan / report / AI-guard data paths
  COMPONENTS.md            component surface and protection levels
evidence/
  AndroidManifest.xml      decoded manifest
  manifest-summary.txt     package, SDK levels, permissions
  components.csv           component export matrix
  data-fields.csv          field-level collection map
  hashes.txt               hashes, signer, static key fingerprint
  strings/                 DEX string table and URI list
  decompiled/              36 classes referenced by the report
  network/                 endpoint probe transcript
tools/
  sign_request.py          offline reproduction of the MD5 signature
  extract_crypto.py        demonstrates the static AES key is recoverable
```

## Reproduce

```bash
jadx -d work/jadx --no-res --show-bad-code 系统安全组件_2.5.6-20251106.0.apk
python tools/sign_request.py
python tools/extract_crypto.py --apk-dir <extracted-apk>
```

## Research use

- **Scope of the claim.** Findings describe build `2.5.6-20251106.0` signed with the Xiaomi platform key. Other builds may differ in engine selection, enabled regions and endpoint set.
- **What the repository supports.** Permission and component analysis, data-flow reconstruction, request-signature reproduction, static-key demonstration, endpoint behaviour under unauthenticated requests.
- **Ethics.** No production system was modified, no credential was used, no POST was sent. The only requests were `HEAD /` on four hosts and `GET` on three documented paths with no parameters; no path enumeration or brute force was performed.
- **Limitations.** No device was available, so no dynamic instrumentation; the native engines (TMSDK, Antiy, Flamingo) were not reversed, and their internal behaviour is inferred from Java shims, assets and strings.

## How to cite

```
System Security Components 2.5.6 — Security and Privacy Assessment of a HyperOS System Package.
Repository: guardprovider-security-analysis, 2026-11-05.
Sample SHA-256: b56c0b72ca5b442220b1c49632b7d70112813cb76cec9b1c21f4b5f49d77af92
```
