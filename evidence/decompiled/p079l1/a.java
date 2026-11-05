package p079l1;

import android.content.Context;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.util.DisplayMetrics;
import android.util.Log;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import p135u2.m;

/* JADX INFO: loaded from: classes.dex */
public final class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f17303a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private MediaProjection f17304b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private MediaRecorder f17305c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private VirtualDisplay f17306d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private Path f17307e;

    /* JADX INFO: renamed from: l1.a$a, reason: collision with other inner class name */
    public static final class C0247a extends MediaProjection.Callback {
        C0247a() {
        }

        @Override // android.media.projection.MediaProjection.Callback
        public void onStop() {
        }
    }

    public a(Context context) {
        m.e(context, "ctx");
        this.f17303a = context;
        this.f17305c = new MediaRecorder(context);
    }

    private final void a(MediaRecorder mediaRecorder) throws IOException {
        mediaRecorder.setVideoSource(2);
        mediaRecorder.setOutputFormat(2);
        mediaRecorder.setVideoEncoder(2);
        mediaRecorder.setVideoSize(720, 1280);
        mediaRecorder.setVideoFrameRate(24);
        mediaRecorder.setVideoEncodingBitRate(1048576);
        Path pathCreateTempFile = Files.createTempFile(this.f17303a.getCacheDir().toPath(), "clip_", null, new FileAttribute[0]);
        this.f17307e = pathCreateTempFile;
        mediaRecorder.setOutputFile(pathCreateTempFile.toFile());
    }

    public final void b() {
        this.f17305c.release();
    }

    public final void c() throws IOException {
        MediaProjection mediaProjectionA = d.a(this.f17303a);
        mediaProjectionA.registerCallback(new C0247a(), null);
        this.f17304b = mediaProjectionA;
        a(this.f17305c);
        this.f17305c.prepare();
        DisplayMetrics displayMetrics = this.f17303a.getResources().getDisplayMetrics();
        MediaProjection mediaProjection = this.f17304b;
        this.f17306d = mediaProjection != null ? mediaProjection.createVirtualDisplay("ScreenRecorder", displayMetrics.widthPixels, displayMetrics.heightPixels, displayMetrics.densityDpi, 16, this.f17305c.getSurface(), null, null) : null;
        this.f17305c.start();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final Path d() {
        try {
            this.f17305c.stop();
            Object[] objArr = objArr == true ? 1 : 0;
            return this.f17307e;
        } catch (RuntimeException unused) {
            Log.e("ScreenRecorder", "No data has been recorded this time.");
            Path path = this.f17307e;
            if (path != null) {
                Files.deleteIfExists(path);
            }
            return null;
        } finally {
            this.f17305c.reset();
            MediaProjection mediaProjection = this.f17304b;
            if (mediaProjection != null) {
                mediaProjection.stop();
            }
            this.f17304b = null;
            VirtualDisplay virtualDisplay = this.f17306d;
            if (virtualDisplay != null) {
                virtualDisplay.release();
            }
            this.f17306d = null;
        }
    }
}
