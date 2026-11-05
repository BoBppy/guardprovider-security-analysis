# Changelog

## 2026-11-05 — v1.2

- Added `docs/LOCAL-DATA.md`: at-rest inventory, retention of the AI Guard artefacts, reachability matrix, and privilege-surface analysis.
- Added §3.6 (local data at rest) and §3.7 (reachability for an ordinary app) to both reports.
- **Added F-12.** `files/feedback01.tmp` (raw call audio) and `files/clip01.tmp` (screen clip) stay in plaintext when an alert is raised and the user does not act.
- Documented that the package has no self-scheduled collection: no boot receiver, no package-add receiver, no periodic work; scans are caller-driven and AI Guard is call-driven.
- Added §14 disclosure path: none of the findings is a strong CVE candidate; the recommended route is a privacy complaint plus a PSIRT note.
- Fixed an obsolete line reference (`p098o1/h.java:65` → `:49`).

## 2026-11-05 — v1.1

- Reconstructed the AI Guard capture chain end to end (`docs/AI-GUARD.md`).
- **Corrected F-05.** It is not an `AudioRecordingCallback` on `VOICE_CALL`; `p098o1/h.java:49` opens two `AudioRecord` streams (near-end `VOICE_COMMUNICATION` and a far-end `AudioPolicy` loopback with `ROUTE_FLAG_RENDER|ROUTE_FLAG_LOOP_BACK`) and averages them into one 16 kHz mono stream. Severity raised Medium → High.
- **Added F-11.** Screen capture uses a reflective `IMediaProjectionManager.createProjection()` under `MANAGE_MEDIA_PROJECTION`, so neither the system consent dialog nor the capture indicator appears. `MediaRecorder` writes 3-second H.264 clips that are uploaded and repeated every 5 seconds.
- Documented cadence and retention: transcript every 10 s, video clips every ~5 s, service lifetime 10 min, raw PCM in `files/feedback01.tmp`, clip copy in `files/clip01.tmp`.
- Established that the ongoing notification ("通话防护中") does not mention capture, while the one-time privacy notice does disclose "call audio" and "video frames or clips".
- Archived 10 additional classes (`p079l1/*`, `p098o1/*`, `p058i1/*`, `p086m1/*`); `metadata/app-profile.json` gained an `ai_guard_capture` block.
- Fixed a pre-existing 6-column row in `evidence/data-fields.csv`.

## 2026-11-05 — v1.0

- Sample triage: identity, hashes, signing certificate, 37 permissions, 19 components, 12 native libraries.
- Manifest and network-security-config analysis (`staging.api.sec.miui.com`, `tools.3g.qq.com` cleartext exceptions).
- Decompilation with jadx 1.5.6: 5,712 files, 159 first-party classes; 26 archived under `evidence/decompiled/`.
- Three network schemes reconstructed: signed form POST (MD5 + hardcoded salt), AES-CBC envelope with a static key derived from `META-INF/flamingo.dat`, confidential-computing envelope for AI Guard.
- Static key derivation reproduced and reduced to a SHA-256 fingerprint (`229c6c76…`); the raw key is not published.
- Endpoint probing: four Xiaomi hosts, parameterless GET only; `error_code 10012` confirms signature enforcement on `api.sec.miui.com`.
- Findings F-01 … F-10, English and Chinese reports.
- Comparison section against the companion DM client (`com.mobiletools.systemhelper`), which shares the same signer.
- Machine-readable profile: `metadata/app-profile.json`.
- Offline tooling: `tools/sign_request.py`, `tools/extract_crypto.py`.
