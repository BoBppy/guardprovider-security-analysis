package p098o1;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
final class f implements d {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile AudioRecord f19949c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final int f19950d;

    public f() {
        this(4);
    }

    static AudioRecord c() throws IllegalAccessException, NoSuchMethodException, InstantiationException, ClassNotFoundException, InvocationTargetException {
        Class<?> cls = Class.forName("android.media.audiopolicy.AudioMixingRule$Builder");
        Object objNewInstance = cls.newInstance();
        Class cls2 = Integer.TYPE;
        Method method = cls.getMethod("addMixRule", cls2, Object.class);
        Method method2 = cls.getMethod("setTargetMixRole", cls2);
        Method method3 = cls.getMethod("build", null);
        method.invoke(objNewInstance, 1, new AudioAttributes.Builder().setUsage(2).setFlags(65536).build());
        method.invoke(objNewInstance, 1, new AudioAttributes.Builder().setUsage(3).setFlags(65536).build());
        method2.invoke(objNewInstance, 0);
        Object objInvoke = method3.invoke(objNewInstance, null);
        Class<?> cls3 = Class.forName("android.media.audiopolicy.AudioMix$Builder");
        Object objNewInstance2 = cls3.getConstructor(objInvoke.getClass()).newInstance(objInvoke);
        Method method4 = cls3.getMethod("setFormat", AudioFormat.class);
        Method method5 = cls3.getMethod("setRouteFlags", cls2);
        Method method6 = cls3.getMethod("build", null);
        method4.invoke(objNewInstance2, d.f19944a);
        method5.invoke(objNewInstance2, 3);
        Object objInvoke2 = method6.invoke(objNewInstance2, null);
        Class<?> cls4 = Class.forName("android.media.audiopolicy.AudioPolicy$Builder");
        Method method7 = cls4.getMethod("addMix", objInvoke2.getClass());
        Method method8 = cls4.getMethod("build", null);
        Object objNewInstance3 = cls4.getConstructor(Context.class).newInstance(null);
        method7.invoke(objNewInstance3, objInvoke2);
        Object objInvoke3 = method8.invoke(objNewInstance3, null);
        Method declaredMethod = AudioManager.class.getDeclaredMethod("registerAudioPolicyStatic", objInvoke3.getClass());
        declaredMethod.setAccessible(true);
        if (!declaredMethod.invoke(null, objInvoke3).equals(0)) {
            throw new UnsupportedOperationException("Error: could not register audio policy");
        }
        AudioRecord audioRecord = (AudioRecord) objInvoke3.getClass().getMethod("createAudioRecordSink", objInvoke2.getClass()).invoke(objInvoke3, objInvoke2);
        Method declaredMethod2 = AudioRecord.class.getDeclaredMethod("unregisterAudioPolicyOnRelease", objInvoke3.getClass());
        declaredMethod2.setAccessible(true);
        declaredMethod2.invoke(audioRecord, objInvoke3);
        return audioRecord;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void d(int i6, Consumer consumer) {
        int i7;
        synchronized (this) {
            try {
                if (this.f19949c != null) {
                    this.f19949c.startRecording();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.i("AudioCapture", "Recording is starting.");
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(i6);
        while (this.f19949c != null && (i7 = this.f19949c.read(byteBufferAllocateDirect, i6)) > 0) {
            byteBufferAllocateDirect.limit(i7);
            consumer.accept(byteBufferAllocateDirect);
            byteBufferAllocateDirect.clear();
        }
    }

    @Override // p098o1.d
    public void a(final Consumer consumer) {
        final int i6 = d.f19945b * 25;
        if (this.f19950d == 3000) {
            try {
                this.f19949c = c();
            } catch (Exception e6) {
                Log.e("AudioCapture", "Create voip downlink AudioRecord failed: ", e6);
                return;
            }
        } else {
            this.f19949c = new AudioRecord.Builder().setAudioFormat(d.f19944a).setBufferSizeInBytes(i6).setAudioSource(this.f19950d).build();
        }
        new Thread(new Runnable() { // from class: o1.e
            @Override // java.lang.Runnable
            public final void run() {
                this.f19946b.d(i6, consumer);
            }
        }).start();
    }

    @Override // p098o1.d
    public void stop() {
        if (this.f19949c != null) {
            synchronized (this) {
                this.f19949c.stop();
                this.f19949c.release();
                this.f19949c = null;
            }
            Log.i("AudioCapture", "Recording stopped.");
        }
    }

    public f(int i6) {
        this.f19950d = i6;
    }
}
