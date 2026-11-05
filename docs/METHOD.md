# Method, tooling and reproduction

## 1. Environment

| Item | Value |
|---|---|
| Host | Windows 11, Git Bash |
| Python | 3.13 (`C:\Users\hi\AppData\Local\Programs\Python\Python313`) with `cryptography`, `androguard` 4.1.4 |
| jadx | 1.5.6 at `C:\Lab\software\jadx` (GUI jar driven through `jadx.cli.JadxCLI`, bundled JRE 25) |
| adb | platform-tools present, **no device attached** |
| openssl | Git Bash build |

The jadx wrapper used on this host:

```bash
# C:\Lab\software\jadx\bin\jadx
#!/bin/sh
JADX_HOME="$(cd "$(dirname "$0")/.." && pwd)"
exec "$JADX_HOME/jre/bin/java.exe" -cp "$JADX_HOME/lib/jadx-gui-1.5.6-all.jar" jadx.cli.JadxCLI "$@"
```

`C:\Lab\software\jadx\bin` and `C:\Lab\software\jadx\jre\bin` are on the user `PATH`.

## 2. Triage

```bash
sha256sum 系统安全组件_2.5.6-20251106.0.apk
unzip -o 系统安全组件_2.5.6-20251106.0.apk -d work2/unzip
find work2/unzip -type f | sed 's/.*\.//' | sort | uniq -c | sort -rn
```

Result: one DEX (7.4 MB), 12 ARM64 libraries, 66 `META-INF/*.version` files, 904 XML resources with obfuscated names, Kotlin tooling metadata, assets for three engines.

## 3. Manifest and network configuration

```bash
python - <<'PY'
import loguru; loguru.logger.remove()
from androguard.core.apk import APK
a = APK("系统安全组件_2.5.6-20251106.0.apk")
print(a.get_package(), a.get_androidversion_name(), a.get_min_sdk_version(), a.get_target_sdk_version())
for p in sorted(a.get_permissions()): print(p)
open("work2/AndroidManifest.xml","w").write(a.get_android_manifest_axml().get_xml().decode())
PY
```

The network security config is resource `0x7F130000`; resource file names are obfuscated, so the file was located by content and decoded with `androguard.core.axml.AXMLPrinter`:

```xml
<network-security-config>
  <domain-config cleartextTrafficPermitted="true">
    <domain includeSubdomains="true">staging.api.sec.miui.com</domain>
    <domain includeSubdomains="true">tools.3g.qq.com</domain>
  </domain-config>
</network-security-config>
```

## 4. Decompilation

```bash
export PATH="/c/Lab/software/jadx/bin:$PATH"
jadx -d work2/jadx --no-res --show-bad-code 系统安全组件_2.5.6-20251106.0.apk
```

5,712 Java files, 42 decompilation errors (library internals). Navigation:

```bash
grep -rn "@K5.o(\"" F1 | sort -u            # endpoint declarations
grep -rhoE "https?://[^\"' ]+" --include=*.java . | sort -u
grep -rn "getImei\|getSubscriberId" --include=*.java tmsdkobf | head
```

## 5. Crypto reconstruction

### 5.1 Static AES key

`J1/i.c()` was transcribed into `tools/extract_crypto.py`:

```
key = flamingo.dat[144:160] XOR 4831221107097930563 (applied to both 64-bit halves)
```

The tool prints the key's SHA-256 and performs an AES/CBC round-trip; it does not emit the key.

```bash
python tools/extract_crypto.py --apk-dir <extracted-apk>
[+] key sha256  : 229c6c761bbca31cd8176c8727abe3f275cf4420897e709503ffbef1dbc5113f
```

### 5.2 Request signature

`p146w1/b.i()` was transcribed into `tools/sign_request.py`. The salt is hardcoded at the call sites (`p152x1/d.java:65,246`).

```bash
python tools/sign_request.py
```

## 6. Endpoint probing

Deliberately minimal:

```bash
for h in api.sec.miui.com virus-engine.sec.miui.com \
         fraud-detection.engine.miui.com detection-aigc.engine.miui.com; do
  curl -sS -I --max-time 10 "https://$h/"
done

curl -sS -i --max-time 10 "https://api.sec.miui.com/antivirus/viruses"
curl -sS -i --max-time 10 "https://api.sec.miui.com/antivirus/whitelist"
curl -sS -i --max-time 10 "https://virus-engine.sec.miui.com/virus/scan"
```

No POST, no parameters, no credentials, no path enumeration. Transcript: `evidence/network/probe-log.txt`.

## 7. Not performed

| Not performed | Reason |
|---|---|
| Dynamic instrumentation (Frida, runtime permission tracing) | no device or emulator; an AVD would not reproduce platform-signed privileges |
| POST to production endpoints | would write into a third party's production system |
| Path/parameter fuzzing | out of scope for a passive assessment |
| Native engine reversing (TMSDK, Antiy, Flamingo) | stripped/packed; the data flow is documented from Java shims and assets |
| Decryption of AI Guard traffic | keys live in the confidential-computing service, not in the APK |

## 8. Reproducibility caveats

- jadx output is not byte-stable across versions; line references match the archived `evidence/decompiled/`.
- The canonical string in `tools/sign_request.py` uses synthetic values; the algorithm and salt are real.
- Endpoint behaviour was measured once from a residential network on 2026-11-05 and may differ by source, method and time.
