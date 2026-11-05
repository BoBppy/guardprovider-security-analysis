package p098o1;

import android.media.AudioRecord;
import android.util.Log;
import java.nio.ByteBuffer;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
final class h implements d {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile AudioRecord f19954c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private volatile AudioRecord f19955d;

    h() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c(int i6, Consumer consumer) {
        int i7;
        int i8;
        synchronized (this) {
            try {
                if (this.f19954c != null && this.f19955d != null) {
                    this.f19954c.startRecording();
                    this.f19955d.startRecording();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        Log.i("AudioCapture", "Recording is starting.");
        ByteBuffer byteBufferAllocateDirect = ByteBuffer.allocateDirect(i6);
        ByteBuffer byteBufferAllocateDirect2 = ByteBuffer.allocateDirect(i6);
        ByteBuffer byteBufferAllocateDirect3 = ByteBuffer.allocateDirect(i6);
        while (this.f19954c != null && this.f19955d != null && (i7 = this.f19954c.read(byteBufferAllocateDirect2, i6)) > 0 && (i8 = this.f19955d.read(byteBufferAllocateDirect, i6)) > 0) {
            byteBufferAllocateDirect2.limit(i7);
            byteBufferAllocateDirect.limit(i8);
            d(byteBufferAllocateDirect2, byteBufferAllocateDirect, (ByteBuffer) byteBufferAllocateDirect3.clear());
            consumer.accept(byteBufferAllocateDirect3);
        }
    }

    @Override // p098o1.d
    public void a(final Consumer consumer) {
        final int i6 = d.f19945b * 25;
        this.f19954c = new AudioRecord.Builder().setAudioSource(7).setAudioFormat(d.f19944a).setBufferSizeInBytes(i6).build();
        try {
            this.f19955d = f.c();
            new Thread(new Runnable() { // from class: o1.g
                @Override // java.lang.Runnable
                public final void run() {
                    this.f19951b.c(i6, consumer);
                }
            }).start();
        } catch (Exception e6) {
            Log.e("AudioCapture", "Create voip downlink AudioRecord failed: ", e6);
        }
    }

    void d(ByteBuffer byteBuffer, ByteBuffer byteBuffer2, ByteBuffer byteBuffer3) {
        for (int i6 = 0; i6 < Math.min(byteBuffer.limit(), byteBuffer2.limit()); i6++) {
            byteBuffer3.put(i6, (byte) ((byteBuffer.get(i6) >> 1) + (byteBuffer2.get(i6) >> 1)));
        }
    }

    @Override // p098o1.d
    public void stop() {
        if (this.f19954c == null || this.f19955d == null) {
            return;
        }
        synchronized (this) {
            this.f19954c.stop();
            this.f19955d.stop();
            this.f19954c.release();
            this.f19955d.release();
            this.f19954c = null;
            this.f19955d = null;
        }
        Log.i("AudioCapture", "Recording stopped.");
    }
}
