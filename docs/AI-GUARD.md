# AI Guard — capture, analysis and upload chain

This document reconstructs the `com.miui.guardprovider.aiguard` module from the DEX of
`系统安全组件_2.5.6-20251106.0.apk`. It covers what is captured, how, for how long, what
leaves the device, and what the user is shown while it happens.

All paths are relative to `evidence/decompiled/` unless stated otherwise.

---

## 1. Short answer

| Question | Answer |
|---|---|
| Does it record call audio? | **Yes.** Two `AudioRecord` streams are opened and mixed: the near end (`VOICE_COMMUNICATION`) and the far end (an `AudioPolicy` loopback). 16 kHz, mono, PCM 16-bit. |
| Does it record the screen? | **Yes**, in the video path. Full-display `MediaRecorder` capture, H.264, 720×1280, 24 fps, 1 Mbit/s, in **3-second clips**. |
| Is the audio uploaded? | The **transcript** is uploaded every 10 s. The **raw PCM stays on disk** (`files/feedback01.tmp`) and is uploaded only if the user later taps “mark as false positive”. |
| Is the video uploaded? | **Yes, every clip**, as base64 inside a JSON body, to `/aiVideo/pcc/detection`. |
| Is it silent? | No per-session prompt, no system screen-capture indicator, and the foreground notification does not mention capture. A **one-time** privacy notice does disclose “call audio” and “video frames or clips”. |
| Who can start it? | Only a platform-signed app holding `com.miui.guardprovider.permission.Guard_Provider` — in practice the security centre. |

---

## 2. Trigger and lifecycle

`AiGuardService` (`AndroidManifest.xml`):

```xml
<service android:name="com.miui.guardprovider.aiguard.AiGuardService"
         android:permission="com.miui.guardprovider.permission.Guard_Provider"
         android:exported="true"
         android:process=":ai"
         android:foregroundServiceType="0x000000A0"/>
```

`0xA0` = `FOREGROUND_SERVICE_TYPE_MICROPHONE` (0x80) | `FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION` (0x20).
Declaring both types on one service is the structural statement that this module captures
microphone and screen together.

`onStartCommand` (`AiGuardService.java:1573`):

| `COMMAND` extra | Path | Extra |
|---|---|---|
| `1` | phone/voice detection → `L(scene)` | `PHONE_SCAM_SCENE` |
| `2` | video detection → `P(scene)` | `VIDEO_SCAM_SCENE` |
| `3` | toggle feature (`ai_guard_function_switch`) | boolean |

Before either path:

```java
startForeground(1, y(), 160);          // :1581 — notification + type 0xA0
J();                                    // :1582 — thermal listener
K();                                    // :1583 — confidential-computing client
if (checkSelfPermission("android.permission.RECORD_AUDIO") == 0) { ... }
```

There is **no runtime permission request** in this path. `RECORD_AUDIO` is a normal
permission, but the package is platform-signed and pre-installed, so it is granted at
install time; the code only checks whether it was revoked.

`useOnDevice`, `voiceDetectionGapInSeconds`, `detectionServiceLifetimeThresholdInMinutes`
and the device whitelist are read from `MiuiSettings.SettingsCloudData`
(`p058i1/o.java`), i.e. they are pushed by Xiaomi, not chosen by the user. Defaults are
`10` for the gap and `10` minutes for the service lifetime.

The service is single-shot: `j` (`AiGuardService.java:544`) waits
`detectionServiceLifetimeThresholdInMinutes` and then calls `stopSelf()`.

---

## 3. Audio path

### 3.1 Stream selection

`p098o1/d.java:16` maps the scene to a capture strategy:

```java
static d of(int scene) {
    if (scene == 2) return new f();          // WeChat video: VoIP downlink + mic
    if (scene == 3 || scene == 4) return new h();
    throw new IllegalArgumentException(...);
}
```

### 3.2 Near end + far end, mixed

`p098o1/h.java:49` opens **two** recorders and averages them sample by sample:

```java
this.f19954c = new AudioRecord.Builder()
        .setAudioSource(7)                  // VOICE_COMMUNICATION — near end
        .setAudioFormat(d.f19944a)          // 16 kHz, mono, PCM16
        .setBufferSizeInBytes(i6).build();
this.f19955d = f.c();                       // far end, see below
...
byteBuffer3.put(i6, (byte)((byteBuffer.get(i6) >> 1) + (byteBuffer2.get(i6) >> 1)));
```

`p098o1/f.java:27` builds the far-end stream through an audio policy rather than a normal
source. It reflects into the hidden `AudioMixingRule` / `AudioMix` / `AudioPolicy` APIs,
registers the policy with `registerAudioPolicyStatic`, and takes the loopback sink:

```java
method.invoke(objNewInstance, 1, new AudioAttributes.Builder().setUsage(2).setFlags(65536).build()); // VOICE_COMMUNICATION
method.invoke(objNewInstance, 1, new AudioAttributes.Builder().setUsage(3).setFlags(65536).build()); // MEDIA
method2.invoke(objNewInstance, 0);
...
method5.invoke(objNewInstance2, 3);          // ROUTE_FLAG_RENDER | ROUTE_FLAG_LOOP_BACK
...
AudioManager.class.getDeclaredMethod("registerAudioPolicyStatic", ...)
Object sink = ... "createAudioRecordSink" ...
```

This is full-duplex call recording. Source 7 alone would capture only the local
microphone; the loopback mixes in the downlink, i.e. **the other party's voice**.

It works because the package holds `CAPTURE_AUDIO_OUTPUT`, `CAPTURE_VOICE_COMMUNICATION_OUTPUT`
and `MODIFY_AUDIO_ROUTING` — all `signature|privileged`. A normal app cannot build this
pipeline even with `RECORD_AUDIO`.

### 3.3 Collision check

`AiGuardService.n.A` (`AiGuardService.java:822`) counts active
`AudioRecordingConfiguration`s whose `getClientAudioSource() == 4`; if another app is
already recording a call it logs `Audio recording can't start because there is another app
using VOICE_CALL right now` and skips capture. The `AudioRecordingCallback` is a
**collision guard**, not the capture mechanism.

### 3.4 Speech recognition

`p098o1/b.java` forwards each PCM buffer to a bound Xiaomi service and receives sentence
callbacks:

```java
this.f19927b = new AiCapability(context, null, null, 6, null);   // :258
AiCapability.startRecognize$default(this.f19927b, 4, 0, false, "zh-CN", "zh-CN", false, false, ...);
...
bVar.f19927b.postData(bArr, false, str);                        // :183
```

`AiCore` binds `com.xiaomi.aiasst.vision.sdk.c` (a separate system component) over Binder
and streams the PCM there (`AiCore.java`, `attachToServer`, `postData`). The SDK exposes
`isSupportOfflineRecognize`, `checkModelExist` and `startDownloadModel`, so the recogniser
may run on-device or remotely depending on that service and the device. **This APK does
not determine which.** The consequence is a second data path — call audio leaving the
package — that is outside `GuardApplication.isAllowConnectNetwork()` and outside the
`detection-aigc` envelope.

### 3.5 Transcript assembly and upload

`p058i1/n.java` accumulates sentences and flushes when the gap since the last flush exceeds
`voiceDetectionGapInSeconds` (default 10 s, `p058i1/o.java`):

```java
public final void a(String str) {
    this.f15829c.add(str);
    long now = System.currentTimeMillis();
    if (now - this.f15830d > this.f15827a) {
        this.f15828b.m(b());      // joined with line separators
        this.f15830d = now;
    }
}
```

`AiGuardService.B` (`:1390`) posts the text:

```json
{"requestId":"<uuid>","text":"<accumulated transcript>"}
```

sealed as `EncryptedData` and sent to `POST /ai/audio/text/pcc/detection`
(`p086m1/g.java`). The result is a boolean `checkRes`; `true` raises the warning activity
and stops the service.

### 3.6 Raw audio on disk

Every PCM buffer is also appended to a file:

```java
// com/miui/guardprovider/aiguard/i.java:86
public final void d(byte[] bArr) {
    Files.write(this.f12544a, bArr, WRITE, CREATE);   // files/feedback01.tmp
}
```

`feedback01.tmp` is deleted when the service is destroyed **unless** a warning was raised
(`AiGuardService.java:599`). It is uploaded in full — `audioPCM` base64 plus `audioText` —
only when the user taps “标记为误报” on the warning screen
(`AlertActivity.java:170`, endpoint `/ai/audio/text/pcc/rbi`).

---

## 4. Video path

### 4.1 Consent-free MediaProjection

`p079l1/d.java:45` obtains the projection by reflection, not through
`MediaProjectionManager.createScreenCaptureIntent()`:

```java
static MediaProjection a(Context context) {
    Object mgr = f17308a.get((MediaProjectionManager) context.getSystemService(MediaProjectionManager.class));
    return (MediaProjection) f17310c.invoke(context,
        f17311d ? f17309b.invoke(mgr, Os.getuid(), context.getPackageName(), 0, false, 0)
                : f17309b.invoke(mgr, Os.getuid(), context.getPackageName(), 0, false));
}
```

This is `IMediaProjectionManager.createProjection(uid, packageName, 0, permanentGrant=false)`,
available only to holders of `MANAGE_MEDIA_PROJECTION` (`signature`). Consequences:

- the system screen-capture consent dialog is never shown;
- the system “screen is being recorded” indicator is never raised;
- no per-session token exists, so capture can start again on every call.

### 4.2 Recorder

`p079l1/a.java:49`:

```java
mediaRecorder.setVideoSource(2);            // SURFACE
mediaRecorder.setOutputFormat(2);           // MPEG_4
mediaRecorder.setVideoEncoder(2);           // H.264
mediaRecorder.setVideoSize(720, 1280);
mediaRecorder.setVideoFrameRate(24);
mediaRecorder.setVideoEncodingBitRate(1048576);
Path p = Files.createTempFile(context.getCacheDir().toPath(), "clip_", null);
```

`c()` starts the virtual display and the recorder; `d()` stops it and returns the temp
file. `MediaProjection.Callback.onStop()` is empty — no user-visible reaction.

### 4.3 Loop

`AiGuardService$p.A` (bytecode, since jadx cannot build a region for it) is:

```
recorder = new l1.a(context)
while (isActive(scope)) {
    recorder.c()                       // start
    delay(3, SECONDS)
    path = recorder.d()                // stop -> /cache/clip_*.tmp
    if (path == null) continue
    hit = withContext(IO) { C(path) }  // upload + delete
    cause = I(scene)
    if (hit) { u(cause); stopSelf(); break }
    delay(2, SECONDS)
}
recorder.b()
```

Measured cadence: **3 s capture, upload, 2 s idle** — a clip roughly every 5 seconds for as
long as the service lives (10 minutes by default).

### 4.4 Upload framing

`AiGuardService.C` (`:1466`):

```java
JSONObject o = new JSONObject();
o.put("streamName", "clip.mp4");
o.put("fileBase64", Base64.encodeToString(Files.readAllBytes(path)));
o.put("requestId", UUID.randomUUID().toString());
```

sealed as `EncryptedData` → `POST /aiVideo/pcc/detection` (`p086m1/i.java`). The whole MP4 is
in memory and in the JSON body; a 3-second 1 Mbit/s clip is ≈375 KB, ≈500 KB after base64.

A copy is kept as `files/clip01.tmp` when a hit is confirmed and uploaded to
`/aiVideo/pcc/rbi` if the user marks a false positive (`AlertActivity.java:193`).

---

## 5. What the user is shown

### 5.1 Notification (always, while active)

`y()` (`AiGuardService.java:1355`) builds a MIUI focus notification:

| Field | zh-CN | en |
|---|---|---|
| title | 通话防护中 | Call protection is on |
| body (video capable) | 识别到疑似欺诈或换脸伪造身份时发送预警 | You'll get an alert if fraud or identity falsification is suspected |
| body (voice only) | 识别到疑似欺诈时发送预警。 | You'll get an alert if fraud is suspected |

It states that protection is on. It does **not** state that the screen is being recorded or
that call audio is being captured at that moment.

### 5.2 One-time notice

`PermissionRequestActivity` shows the AI Guard privacy text before first use:

> 在您进行语音通话时，我们需要收集您的**通话语音**……在您进行视频通话时，除上述**通话语音**内容外，我们还需要收集您视频通话的**视频帧或视频片段**……
> （`ai_guard_privacy_policy_message`）

and a screen-recording dialog:

> 允许“系统安全组件”进行录屏？ / 用于识别视频通话中是否包含换脸仿冒身份的风险。
> （`ai_guard_permission_request_projection_title` / `_message`）

So the **collection is disclosed** and requires “同意并继续”. What is not disclosed at the
point of capture is that it is happening again, on this call, right now.

### 5.3 Asymmetry

| Party | Informed? | Consented? |
|---|---|---|
| Device owner | once, at feature enablement | yes |
| Other party on the call | never | never |
| Other party's image | captured and uploaded | never |

The second and third rows are inherent to the feature design, not a coding defect, but they
are the reason this is reported as a privacy finding rather than a vulnerability.

---

## 6. Cloud-controlled parameters

`p058i1/o.java`, namespace `AiGuardServiceConfigs`:

| Key | Default | Effect |
|---|---|---|
| `onDevice` | device list | whether a model runs locally (only affects `videoCaptureVelocityInFps` in this build) |
| `videoCaptureVelocity4CloudSide` | 10 | parsed but never applied — the recorder hardcodes 24 fps |
| `voiceDetectionGapInSeconds` | 10 | transcript flush interval |
| `detectionServiceLifetimeThresholdInMinutes` | 10 | hard stop for the service |

Because these are read from `SettingsCloudData`, Xiaomi can change capture cadence and
lifetime per device without an app update. In this build the video cadence is not actually
configurable: the 3 s / 2 s constants are compiled in.

---

## 7. What is not evidenced

- Whether the bound recogniser (`com.xiaomi.aiasst.vision`) runs on-device or in the cloud.
  The APK ships `libavlm.so`, `libavlasys.so` and `assets/avlsdk` (1.5 MB), which suggests a
  local model, but the decision is made by the other service.
- Whether the security centre actually starts the service on every call. The trigger is
  external and signature-protected; this APK only implements the callee.
- Any server-side retention. No endpoint was queried with data.
- Whether `checkRes` can be forced to `true` by a network attacker. The envelope is the
  strongest in the package; no weakness was found in it, and no attempt was made to break it.
