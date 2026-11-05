# 系统安全组件 2.5.6 安全与隐私分析报告

**样本** `系统安全组件_2.5.6-20251106.0.apk`
**SHA-256** `b56c0b72ca5b442220b1c49632b7d70112813cb76cec9b1c21f4b5f49d77af92`
**包名** `com.miui.guardprovider` · versionCode 256 · minSdk 32 · targetSdk 36
**分析日期** 2026-11-05
**方法** 静态逆向（jadx 1.5.6）、清单与网络安全配置检查、签名与信封复现、端点非侵入式探测。分析期间无可用真机，未做动态插桩。
**样本性质** HyperOS 系统组件：端侧病毒扫描、URL/Wi-Fi 检测、AI 诈骗检测；平台签名、系统预装。

> 本文件为 `REPORT.md` 的中文对照版。字段名、类名、代码路径保留英文原文以便核对。

---

## 0. 结论摘要

这是同目录另一份报告中运营商代理的"防守方对照物"：一个平台签名的安全组件，用三套引擎扫描应用、维护云端威胁情报，并在安全中心开启后监听通话以识别诈骗。

与对照样本不同，它的组件暴露面是**正确防护**的：所有自定义导出组件都要求 `signature|privileged` 权限。问题出在它自己云端协议的**密码学设计**上，而不是访问控制。

六条判断：

1. **报文信封密钥就在 APK 里。** `J1/i.java` 从 `META-INF/flamingo.dat`（偏移 144，与常量异或）导出 16 字节 AES 密钥。该构建的每一次安装共用同一密钥；密钥 SHA-256 为 `229c6c76…`。这里的"加密"对持有 APK 的人只是混淆。
2. **请求认证是一个硬编码盐。** `sign = MD5(Base64(排序后的参数 + "&" + "94e6cec2-19dc-4bb6-823c-613f72d3ea16"))`，`mi` 由 `ANDROID_ID` 派生。没有每设备凭据，也没有 nonce。持有 APK 加任意 `ANDROID_ID` 即可构造被接受的请求。
3. **没有证书固定（pinning）。** 病毒库、白名单、扫描结论全部走标准 TLS 与系统信任库。对一个靠远程规则运行的安全产品，这条防线过于单薄。
4. **白名单没有签名校验。** `p152x1/f.java` 解析下载的 JSON 后直接清表重写；恒返回 `false` 的 `b(String,String)` 是被移除的完整性校验的残留。
5. **AI 防护是最敏感的数据路径。** 它注册 `AudioRecordingCallback` 监听 `VOICE_CALL`，把通话音频转写为文本后发往 `detection-aigc` / `fraud-detection`，使用机密计算信封。该信封设计明显强于应用其他部分（会话公钥、key id、digest），但这仍是整个包内风险最高的能力。
6. **未发现未认证读取。** `api.sec.miui.com` 在缺少签名参数时返回 `{"error_code":"10012","error":"Illegal request"}`；`virus-engine` 返回 `405 Allow: POST`。端点确实执行校验，但校验所依赖的秘密是公开的。

---

## 1. 范围与方法

| 项目 | 内容 |
|---|---|
| 目标 | `系统安全组件_2.5.6-20251106.0.apk`（20,361,708 字节） |
| 反编译 | jadx 1.5.6（内置 JRE 25）；5,712 个 Java 文件，42 处反编译错误 |
| 一手代码 | `com.miui.guardprovider.*` 下 159 个类 |
| 三方代码 | 腾讯 TMSDK（478 类）、OneTrack（178 类）、AndroidX/Room/WorkManager/OkHttp/Gson |
| 原生库 | 12 个 ARM64 库，未逆向 |
| 动态测试 | 无（无真机/模拟器） |
| 网络测试 | DNS、4 个主机 `HEAD /`、3 条已知路径无参数 `GET`；**未发 POST、未用凭据、未做路径枚举** |
| 证据 | `evidence/`：清单、26 个反编译类、字符串表、哈希、探测记录 |

---

## 2. 应用画像

### 2.1 身份与部署

- 无 `LAUNCHER` Activity，用户不可见；`AlertActivity`、`PermissionRequestActivity` 仅为告警与代安全中心征得同意而存在。
- 双进程：主进程与 `:ai`（诈骗检测模块）。
- `targetSdk 36`、`minSdk 32`、`allowBackup=false`、`largeHeap=true`、`extractNativeLibs=false`、`requestLegacyExternalStorage=true`。
- 存在网络安全配置且总体严格：仅允许 `staging.api.sec.miui.com`（本构建中零引用）与 `tools.3g.qq.com`（腾讯 SDK）明文。

### 2.2 签名

与对照样本同一平台证书：

`CN=MIUI, OU=MIUI, O=Xiaomi` · RSA-2048 · SHA-1 `7B:6D:C7:07:9C:34:73:9C:E8:11:59:71:9F:B5:EB:61:D2:A0:32:25`

MIUI 主题是历史身份，HyperOS 沿用同一密钥签名。正因是平台签名，其 `signature|privileged` 权限定义才有意义：只有平台签名或特权包才能获得。

### 2.3 组件

完整矩阵：`evidence/components.csv`。自定义组件：

| 组件 | 类型 | 导出 | 权限 | 功能 |
|---|---|---|---|---|
| `GuardProvider` | provider（`guard`） | 是 | `Guard_Provider` | 引擎信息、病毒/白名单表、安装/同步扫描、`call("getRiskAppData")` |
| `manager.SecurityService` | service | 是 | `Guard_Provider` | AIDL：启停扫描、引擎检查与更新、URL 扫描、Wi-Fi ARP/DNS/钓鱼检测 |
| `aiguard.AiGuardService` | service（`:ai`） | 是 | `Guard_Provider` | AI 诈骗检测生命周期、音频回调、ASR、云端检测 |
| `aiguard.PermissionRequestActivity` | activity（`:ai`） | 是 | `Guard_Provider` | 授权与权限提示、隐私政策链接 |
| `aiguard.AlertActivity` | activity（`:ai`） | 否 | — | 命中后的告警弹窗 |

框架组件（AndroidX WorkManager、Room、ProfileInstaller、Diagnostics）遵循其标准导出与权限规则。

**评估：** 导出面防护正确。`Guard_Provider` 声明 `protectionLevel=0x12`（signature|privileged），三方应用无法绑定、查询或拉起这些组件。这是与对照样本最主要的结构性差异——后者同类服务以无权限方式导出。

### 2.4 权限（37 项）

高危集合与"端侧安全产品"相符，与更小的用途不符：

```
CAPTURE_AUDIO_OUTPUT   CAPTURE_VOICE_COMMUNICATION_OUTPUT   CAPTURE_VIDEO_OUTPUT
RECORD_AUDIO           MANAGE_MEDIA_PROJECTION              MANAGE_EXTERNAL_STORAGE
MODIFY_PHONE_STATE     WRITE_SECURE_SETTINGS                READ_PRIVILEGED_PHONE_STATE
INTERACT_ACROSS_USERS  QUERY_ALL_PACKAGES                   SYSTEM_ALERT_WINDOW
com.miui.securitycenter.permission.READ_APP_BEHAVIORS
```

`CAPTURE_AUDIO_OUTPUT` 与 `CAPTURE_VOICE_COMMUNICATION_OUTPUT` 是签名级系统权限；配合 `READ_APP_BEHAVIORS`，该组件位于平台信任边界之内。

---

## 3. 采集内容

逐字段映射：`evidence/data-fields.csv`。

### 3.1 扫描清单（本地）

引擎对每个已安装包计算包名、versionCode、APK 路径、签名证书 MD5/SHA-256、manifest SHA-256、APK MD5/SHA-256。云端查询（`CloudScanApkInfo`）只发送：

`packageName, versionCode, apkCertificateMD5, apkCertificateSHA256, mfFileSHA256`

**不上传 APK 内容**，按哈希核验。这是克制的设计，需要明确写出——"杀毒软件上传你的应用"是常见误解。

### 3.2 威胁上报（`/virus/localResult/report`）

引擎命中时上传 `ResultReportData`：

`threatLevel, threatName, malwareCodeFeature, version, packageName, appName, versionCode, apkCertificateMD5, apkCertificateSHA256, mfFileSHA256, apkMd5, apkSha256, miuiVersion, androidVersion, device, uuid, hitFiles`

`hitFiles` 是包内命中的文件路径；`uuid` 使同一安装的多次上报可被关联；`device` 为机型。这是设备应用清单唯一可能被后端看见的路径——且仅限于被判定为威胁的应用。

### 3.3 AI 防护

`AiGuardService` 注册 `AudioManager.registerAudioRecordingCallback`，监听 `SOURCE_VOICE_CALL`（值 4）。安全中心开启后（`ai_guard_function_switch` intent extra，默认 `false`）：

1. 对通话音频启动 ASR（`p098o1.b`）；
2. 组织对话文本；
3. 封装为 `EncryptedData`，POST 至 `/ai/audio/text/pcc/detection`；
4. 可选上报用户反馈至 `/ai/audio/text/pcc/rbi`、`/aiVideo/pcc/rbi`；
5. 维护分场景计数（微信/QQ/电话，语音/视频）。

对话文本是本包处理的最敏感数据。其信封是全应用最强的：`secure_channel_session_public_key`、`key_id`、`digest`、`data_bytes`——会话公钥 + 密钥管理标识，而非静态密钥。

### 3.4 遥测

OneTrack 被初始化两次：

| appId | 用途 | 配置 |
|---|---|---|
| `31000401489` | 主包 | `Mode.APP`，channel=包名，`autoTrackActivityAction=true` |
| `2882303761517522561` | AI 模块 | 另加 `exceptionCatcherEnable=true`、`GAIDEnable=false` |

OneTrack 采集生命周期与异常事件，写入 `tracking.miui.com`。同一包两个 appId 值得记录：AI 模块的事件归属到与主体不同的分析身份。

### 3.5 三方信号

腾讯 TMSDK（`tmsdkobf`，478 类，`libTmsdk-2.0.16-mfr.so`）维护自有通道 `rcpc-sdk-access.m.qq.com`（另有 IPv4/IPv6 变体，端口 8002），并通过 `ExtraInfoFetcher` 计算 `imeiHash`。哈希不可逆，但它是与第三方共享的持久设备级标识。

---

## 4. 采集与上报时机

| 触发 | 动作 | 端点 |
|---|---|---|
| 引擎更新（手动或 `SecurityService` 调度） | 下载病毒库与白名单 | `api.sec.miui.com` |
| 扫描开始（`startScan`、`startScanAsScene`、`install_scan`、`sync_scan`） | 本地引擎运行；未知包走云端 | `virus-engine.sec.miui.com/virus/scan` |
| 命中威胁 | 每条命中上报一份 `ResultReportData` | `virus-engine.sec.miui.com/virus/localResult/report` |
| 诈骗应用 | 上报包元数据 | `fraud-detection.engine.miui.com/hyperos/fraud/app/report` |
| AI 防护开启且通话中 | ASR，逐句检测 | `detection-aigc.engine.miui.com` |
| 生命周期/异常 | OneTrack 批量 | `tracking.miui.com` |
| 腾讯 SDK 内部调度 | RCPC 查询 | `rcpc-sdk-access.m.qq.com` |

联网受 `GuardApplication.isAllowConnectNetwork()` 控制：

- 国内构建：`persist.sys.sc_allow_conn` 或安全中心写入的 `network_status`；
- 国际构建：需安装 `com.miui.securitycenter` 且同一开关打开；`jp_kd` 区域还要求 `Settings.Secure.antivirus_engine_network_enable`。

这是一个真实的区域感知开关，且在 HTTP 工具类（`p146w1/b`）与 OkHttp 拦截器（`p086m1/e`）两处同时生效。

---

## 5. 上报机制

### 5.1 签名表单 POST（威胁情报）

`p146w1/b.java`：

```
params  = {device, carrier, region, miuiVersion, versionType, appVersion, mi}
mi      = MD5(MD5(ANDROID_ID + "-" + SALT))
sign    = MD5_hex_upper(Base64("k1=v1&k2=v2&…" 排序 + "&" + SALT))
body    = application/x-www-form-urlencoded(params + sign)
```

`tools/sign_request.py` 可离线复现规范串与摘要。盐为 `94e6cec2-19dc-4bb6-823c-613f72d3ea16`，就在 APK 内。

### 5.2 AES 信封（云查、诈骗上报）

`J1/i.java` + `F1/j.java` + `F1/n.java`：

```
key    = AES-128，取 META-INF/flamingo.dat[144:160] 与 0x4312… 异或   （静态）
iv     = 16 字节随机，置于密文前
body   = AES/CBC/PKCS5(key, iv || plaintext)
header = EncryptVersion
```

响应以同一密钥解密；200 响应缺少 `EncryptVersion` 时 `F1/n.java` 直接抛异常。没有 MAC，也没有响应签名。

`tools/extract_crypto.py` 可导出密钥并打印其 SHA-256（`229c6c76…`），脚本本身不输出密钥明文。

### 5.3 机密计算信封（AI 防护）

`EncryptedData` 携带版本、会话公钥、`key_id`、digest 与密文。实际封装委托给 `N1.a`（机密计算 binder 服务），密钥不在 APK 内。这是全包唯一"持有 APK 也攻不破"的方案。

### 5.4 响应处理

- `p146w1/b.c()` 对任何非 200 返回 `null` 并记录异常；helper 内无重试策略。
- `p152x1/f.c()` 接受白名单 JSON，清表后重写——**不校验签名**。
- `F1/n` 只解密，不认证明文。

---

## 6. 端点

| 主机 | 路径 | 用途 | 实测 |
|---|---|---|---|
| `api.sec.miui.com` | `/antivirus/viruses`、`/antivirus/whitelist` | 病毒库与白名单分发 | 200 + 无签名时 `error_code 10012` |
| `virus-engine.sec.miui.com` | `/virus/scan`、`/virus/localResult/report` | 云端查询、威胁上报 | 根路径 404；GET scan → 405 `Allow: POST` |
| `fraud-detection.engine.miui.com` | `/hyperos/fraud/app/report` | 虚假/诈骗应用上报 | 根路径 404 |
| `detection-aigc.engine.miui.com` | `/ai/audio/text/pcc/detection`、`/ai/audio/text/pcc/rbi`、`/aiVideo/pcc/rbi` | AI 诈骗检测 | 根路径 404 |
| `rcpc-sdk-access.m.qq.com`（v4/v6 :8002） | 腾讯 RCPC | TMSDK 云查 | 未探测 |
| `http://tools.3g.qq.com` | `/wifi/cw.html`、`/j/sslstrip` | 腾讯 Wi-Fi 门户检测 | 未探测（明文） |
| `tracking.miui.com` | OneTrack | 分析 | 未探测 |

四个小米主机均在 openresty 网关之后并返回 JSON。`virus-engine` 返回 Spring 风格的 405 与 `Allow: POST`，暴露了后端框架族；网关未做隐藏。

原始记录：`evidence/network/probe-log.txt`。

---

## 7. 身份验证

本包使用三套方案，没有一套认证"设备"：

| 方案 | 秘密 | 强度 | 可重放 |
|---|---|---|---|
| 签名表单 POST | APK 内硬编码盐 | MD5、无 nonce；持有 APK 即可伪造 | 是 |
| AES 信封 | 由 APK 资产导出的静态密钥 | 无 MAC；可离线解密与伪造 | 是 |
| 机密计算 | 会话密钥 + key id | 最强；密钥不在 APK | 未知（服务端会话） |

`api.sec.miui.com` 确实执行签名校验：无参数请求返回 `error_code 10012`。这是真实校验，但校验的是"是否持有公开常量"，而非"是否为目标手机"。

`mi` 参数把请求绑定到 `ANDROID_ID`，它按应用签名/设备对稳定，应用可读。它不是秘密，也无法阻止知道某设备 `ANDROID_ID` 的一方冒充该设备。

---

## 8. 能否从端点获取数据

**未发现读取接口。** 客户端只调用扫描/更新/上报路径；159 个一手类中没有查询、列表或导出 API，也没有备用主机。

**未认证访问被拒绝。** `GET /antivirus/viruses`、`/antivirus/whitelist` 无参数返回 `{"error_code":"10012","error":"Illegal request"}`；`GET /virus/scan` 返回 405。未获得任何威胁数据、设备记录或扫描结果，也未尝试伪造签名。

**写入侧原则上可滥用。** 由于签名盐与信封密钥都在 APK 内，能到达端点的一方可以构造通过校验的请求。但受 API 面限制：可拉取病毒库、对自选哈希做云端判定、提交威胁记录。由于没有按设备返回记录的接口，不会暴露其他设备的数据。

**服务端存储不在范围内。** 留存、访问控制与内部控制台无法从 APK 判定。

---

## 9. 流量能否被截取

### 9.1 被动

一手流量全部为 HTTPS + 系统信任库；应用代码中未出现 `CertificatePinner`（仅有 OkHttp 内部的信任管理器适配）。被动网络观察者只能看到 TLS。

TLS 之内，签名 POST 明文携带 `device`、`carrier`、`region`、`miuiVersion` 与 `mi` 哈希——足以指纹化机型与固件。

### 9.2 主动

能终止 TLS 的对手（用户安装 CA、企业代理、被攻陷的 CA）可以：

- 从 APK 提取静态 AES 密钥后，读取云查请求与威胁上报；
- **在入库前篡改病毒库与白名单**，因为响应既不签名也无 MAC，而白名单加载器还留着被禁用的完整性校验桩；
- 丢弃或重放扫描请求，协议中没有 nonce。

规则篡改的实际后果是**检测压制**：白名单条目正是让扫描器忽略某个包的机制。对安全组件而言，这比普通应用更需要 pinning 与响应签名。

### 9.3 AI 防护

机密计算信封无法从 APK 解开；该路径的截取取决于密钥管理服务。剩余暴露是元数据（目标引擎、时间、报文大小）以及"该功能存在"这一事实。

---

## 10. 发现项

严重性为五级制；CVSS v3.1 向量为指示性。

### F-01 — 静态 AES 密钥保护云查与诈骗上报报文 · **高** · CVSS 7.4 `AV:N/AC:H/PR:N/UI:N/S:U/C:H/I:H/A:N`

密钥由 `META-INF/flamingo.dat` 导出，所有安装一致。因此加密只能防住"随手看一眼抓包"，既不能保护服务端静态数据，也挡不住持有 APK 的对手。
**CWE-321** 硬编码加密密钥
**修复** 在 TEE/Keystore 中派生每安装密钥，改用 AES-GCM 并认证头部；或去掉信封，依赖 TLS + pinning。

### F-02 — 硬编码签名盐、MD5、无 nonce · **高** · CVSS 7.5 `AV:N/AC:L/PR:N/UI:N/S:U/C:N/I:H/A:N`

`sign = MD5(Base64(排序参数 + "&" + SALT))`，盐编译进 APK。端点执行校验，但秘密公开；请求可重放。
**CWE-798** 硬编码凭据 · **CWE-328** 弱哈希
**修复** 安全硬件中的每设备密钥、HMAC-SHA-256、服务端下发 nonce 与时间窗。

### F-03 — 安全关键下载无证书固定 · **中** · CVSS 5.9

无 `CertificatePinner`；病毒库与白名单仅依赖系统信任库。
**CWE-295** 证书校验不当
**修复** 对四个小米主机做 pinning 并规划轮换；规则下载 pin 不匹配时失败关闭。

### F-04 — 白名单无完整性校验 · **中** · CVSS 5.3

`p152x1/f.c(String)` 解析 JSON、清空 `mi_virus_white_db` 后写入。疑似签名校验的 `b(String,String)` 恒返回 `false` 且无人调用。
**CWE-345** 数据真实性校验不足
**修复** 白名单由离线密钥签名，入库前验签；失败即拒绝更新。

### F-05 — AI 防护采集通话音频并转写 · **中**（隐私）

`AudioRecordingCallback` 监听 `SOURCE_VOICE_CALL`，ASR 后把文本发往小米引擎。同意由安全中心代收（`ai_guard_function_switch`，政策 `privacy.mi.com/AIalert`），传输使用会话级机密计算信封。设计是有意的，但这是全包最敏感的能力，需要最强的告知与审计。
**CWE-359**
**修复** 系统层默认关闭；采集期间常驻指示；转写仅驻留内存；提供按请求审计。

### F-06 — 三方引擎自有网络通道 · **中**

腾讯 TMSDK（478 类、原生库、`assets/mdetector`）连接 `rcpc-sdk-access.m.qq.com` 并派生 `imeiHash`。其流量不走小米签名体系，也不经过执行区域开关的 OkHttp 拦截器。
**CWE-1104** 过时三方组件
**修复** 在隐私告知中披露该数据流、确认数据处理协议、确认 SDK 遵守区域开关。

### F-07 — 明文例外，其中一条是死配置 · **低**

`network_security_config` 允许 `staging.api.sec.miui.com`（本构建零引用）与 `tools.3g.qq.com`（腾讯门户检测）明文。
**CWE-319**
**修复** 生产构建移除 staging 条目；腾讯条目限定到确切主机与路径。

### F-08 — 过期的旧存储标志 · **低**

`requestLegacyExternalStorage=true` 在 targetSdk 36 已失效，且与 `MANAGE_EXTERNAL_STORAGE` 并存。
**CWE-732**
**修复** 移除该标志；把全文件访问收窄到扫描流程。

### F-09 — 导出组件防护正确 · **信息（正面）**

所有自定义导出组件都要求 `Guard_Provider`（`signature|privileged`）。未发现无权限 IPC 面——与对照的运营商 DM 客户端形成刻意对比。

### F-10 — 同一包内两个 OneTrack appId · **信息**

主体 `31000401489`，AI 模块 `2882303761517522561`。事件分散到两个分析身份，隐私告知与数据地图应体现这一点。

---

## 11. 隐私与合规观察

1. **目的限制。** 扫描已安装包、仅上报命中项，是克制的；不存在全量清单上报。
2. **透明性。** 包本身不可见；AI 防护的同意由安全中心收集。政策链接存在（`privacy.mi.com/AIalert`），但只能经安全中心流程到达。
3. **三方披露。** 风险决策在小米侧，但扫描引擎与一条网络通道属于腾讯；SDK 的 `imeiHash` 是三方标识。
4. **最小化。** 云端查询按哈希（好）；威胁上报含 `uuid`、`device`、`hitFiles` 与完整 APK 哈希（分诊所需，但服务端留存超出审计必要）。
5. **留存。** 本地数据库与日志未穷尽梳理；服务端留存未知。
6. **区域开关。** `isAllowConnectNetwork()` 是真实控制，但服务于区域合规，而非用户选择。

---

## 12. 与对照样本的比较

| 维度 | `com.mobiletools.systemhelper`（DM 客户端） | `com.miui.guardprovider`（安全组件） |
|---|---|---|
| 签名 | 同一平台证书 | 同一平台证书 |
| 导出组件 | 服务与接收器**无权限** | 自定义导出全部 `signature\|privileged` |
| 传输 | 明文 HTTP 18080 | HTTPS，无 pinning |
| 报文保护 | MD5→AES-ECB、RSA-1024 公钥 | APK 内静态 AES-CBC 密钥；AI 用机密计算 |
| 客户端认证 | 无（公钥"签名"） | 硬编码盐 + MD5，无 nonce |
| 同意 | 仅 `device_provisioned` | 交由安全中心；功能开关默认关闭 |
| 上报数据 | IMEI、IMSI、ICCID、位置、应用清单 | 扫描元数据、命中威胁、AI 转写 |
| 端点读回 | WAF URL ACL 拦截 | 应用层 10012 |

两份样本展示了同一 OEM 镜像中的两种失效模式：运营商代理败在**访问控制与传输**，安全组件败在**密码学设计**。二者的共同点是——秘密都随 APK 一起分发。

---

## 13. 局限性

- **无动态分析。** 实际权限授予、引擎运行期行为、AI 防护是否开启均未观测。
- **未逆向原生引擎。** TMSDK、Antiy AVL、Flamingo 均已裁剪/加固；其内部采集依据 Java 壳类、资产与字符串推断。
- **未发送 POST。** 关于请求被接受的结论来自代码与端点对无参数 GET 的响应。
- **单一产物。** 结论针对 `2.5.6-20251106.0`；其他构建与区域可能不同。
- **混淆。** 业务逻辑分散在 `com.miui.guardprovider.*` 与单字母包（`p146w1`、`p152x1`、`J1`、`F1`），映射为人工重建。

---

## 附录 A — 指标

```
package      com.miui.guardprovider
sha256       b56c0b72ca5b442220b1c49632b7d70112813cb76cec9b1c21f4b5f49d77af92
signer sha1  7B:6D:C7:07:9C:34:73:9C:E8:11:59:71:9F:B5:EB:61:D2:A0:32:25
host         api.sec.miui.com                 (HTTPS)
host         virus-engine.sec.miui.com        (HTTPS)
host         fraud-detection.engine.miui.com  (HTTPS)
host         detection-aigc.engine.miui.com   (HTTPS)
host         rcpc-sdk-access.m.qq.com:8002    (腾讯, HTTPS)
host         tools.3g.qq.com                  (允许 HTTP)
host         tracking.miui.com                (OneTrack)
provider     content://guard/…
permission   com.miui.guardprovider.permission.Guard_Provider
static key   sha256 229c6c761bbca31cd8176c8727abe3f275cf4420897e709503ffbef1dbc5113f
```

## 附录 B — 证据索引

```
evidence/AndroidManifest.xml            解码后的清单
evidence/manifest-summary.txt           包名、SDK 级别、37 项权限、3 项声明
evidence/components.csv                 19 个组件的导出与权限
evidence/data-fields.csv                逐字段采集映射
evidence/hashes.txt                     哈希、签名者、静态密钥指纹
evidence/strings/dex-strings.txt        55,409 条可打印字符串
evidence/strings/urls.txt               20 条 URI
evidence/decompiled/                    报告引用的 26 个类
evidence/network/probe-log.txt          端点探测记录
tools/sign_request.py                   离线复现 MD5 签名
tools/extract_crypto.py                 静态 AES 密钥导出（仅指纹）
docs/WIRE-FORMAT.md                     签名、信封、端点格式
docs/DATA-FLOWS.md                      扫描 → 上报 → AI 防护数据流
docs/COMPONENTS.md                      组件面与权限保护
docs/METHOD.md                          工具与复现步骤
```

---

*本报告基于单一送检 APK。分析过程中未修改任何生产系统、未使用任何凭据、未向任何端点提交数据。*
