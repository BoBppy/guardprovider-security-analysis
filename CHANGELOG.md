# Changelog

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
