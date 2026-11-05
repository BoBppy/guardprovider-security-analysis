# Reconstructed network formats

Three distinct schemes are used. All were derived from code and verified offline where a key or a digest can be reproduced from the APK.

## 1. Signed form POST — threat intelligence

Files: `p146w1/b.java`, call sites `p152x1/d.java` (virus DB, whitelist).

### Parameters

| Key | Value |
|---|---|
| `device` | `ro.product.device` |
| `carrier` | `ro.carrier.name` |
| `region` | `ro.product.mod_device` |
| `miuiVersion` | `"MIUI-" + Build.VERSION.INCREMENTAL` |
| `versionType` | `stable` / `development` / `alpha` |
| `appVersion` | constant `"1"` |
| `mi` | `MD5(MD5(ANDROID_ID + "-" + SALT))` |
| `version` | virus DB version (update calls) |
| `updateType` | `incremental` / `global` |
| `sign` | see below |

### Signature

```java
List<String> keys = new ArrayList<>(params.keySet());
Collections.sort(keys);
StringBuilder sb = new StringBuilder();
for (String k : keys) sb.append(k).append('=').append(params.get(k)).append('&');
sb.append(SALT);
sign = MD5_hex_upper(Base64_NO_WRAP(sb.toString().getBytes("UTF-8")));
```

`SALT = "94e6cec2-19dc-4bb6-823c-613f72d3ea16"`.

Offline reproduction:

```
python tools/sign_request.py
canonical string:
  appVersion=1&carrier=unknown&device=dagu&mi=B0C32CBF5DFFC6FA31D16F2F11225C88&miuiVersion=MIUI-V816.0.1.0&region=cn&updateType=incremental&version=1&versionType=stable&94e6cec2-19dc-4bb6-823c-613f72d3ea16
sign: 2430E111300B104EA134BAAEE400701A
```

### Transport

```
POST <url>
Content-Type: application/x-www-form-urlencoded; charset=UTF-8
body: k=v&...&sign=...
timeouts: 15 s connect / 15 s read
```

Response is read as a string; `error_code 10012` means the request was rejected.

---

## 2. AES envelope — cloud scan and fraud report

Files: `J1/i.java` (crypto service), `F1/j.java` (request interceptor), `F1/n.java` (response interceptor), `H1/w.java` (Retrofit setup).

### Key

```java
InputStream in = getClassLoader().getResourceAsStream("META-INF/flamingo.dat");
in.skip(144);
byte[16] k = in.readNBytes(16);
long lo = LE64(k[0..8])  ^ 4831221107097930563L;
long hi = LE64(k[8..16]) ^ 4831221107097930563L;
key = AES-128(LE64(lo) || LE64(hi));
```

`META-INF/flamingo.dat` is 304 bytes and ships in every APK. The derived key's SHA-256 is `229c6c761bbca31cd8176c8727abe3f275cf4420897e709503ffbef1dbc5113f`.

### Message

```
iv      = 16 random bytes
body    = iv || AES/CBC/PKCS5(key, plaintext)
header  = EncryptVersion: <crypto service version>
```

Response: same key, first 16 bytes are the IV, `EncryptVersion` header must be present on 200, otherwise `IOException`.

No MAC, no signature, no nonce.

### Endpoints

| Host | Path | Body |
|---|---|---|
| `virus-engine.sec.miui.com` | `/virus/scan` | `List<CloudScanApkInfo>` + query map |
| `virus-engine.sec.miui.com` | `/virus/localResult/report` | `timestamp` + `List<ResultReportData>` |
| `fraud-detection.engine.miui.com` | `/hyperos/fraud/app/report` | `FakeAppReportRequest` + `timestamp` |

Client identity in this layer is one of two hardcoded UUIDs used by the sign-generation interceptor:

```
62b36629-ecb4-4725-9f33-ccb0fd158191   (H1/w.p())
39170bda-b0f2-4203-9968-25b238db4b57   (H1/w.q())
```

---

## 3. Confidential-computing envelope — AI Guard

Files: `com/miui/securitycenter/confidentialcomputing/{EncryptedData,c}.java`, `com/miui/guardprovider/aiguard/service/EncryptedResponse.java`, `p086m1/g.java`.

```
JSON {
  "version": 1,
  "secure_channel_session_public_key": "<base64>",
  "key_id": "<id>",
  "digest": "<digest>",
  "data_bytes": "<ciphertext>"
}
```

Sealing and opening are delegated to `N1.a` (a binder service). The key is not in the APK, so this layer is not defeated by possession of the binary. Endpoints:

| Path | Direction | Content |
|---|---|---|
| `/ai/audio/text/pcc/detection` | request/response | conversation text for scam scoring |
| `/ai/audio/text/pcc/rbi` | request | user feedback on a detection |
| `/aiVideo/pcc/rbi` | request | video-scene feedback |

Response shape: `{code, message, data, traceId}`.

---

## 4. Tencent TMSDK

Independent of the schemes above:

- hosts `rcpc-sdk-access.m.qq.com`, `rcpc-sdk-access-v4.m.qq.com:8002`, `rcpc-sdk-access-v6.m.qq.com:8002`;
- `http://tools.3g.qq.com/wifi/cw.html` for Wi-Fi portal checks, cleartext explicitly allowed by `network_security_config`;
- device identifier: `imeiHash` from `ExtraInfoFetcher` (`tmsdkobf/sa.java`);
- licence material in `assets/licence.conf`, `assets/licence1.conf` (encrypted blobs);
- engine data in `assets/mdetector`, `assets/avlsdk`.

Protocol details are internal to the SDK and were not reversed.
