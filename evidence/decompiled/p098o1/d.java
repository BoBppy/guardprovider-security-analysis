package p098o1;

import android.media.AudioFormat;
import android.media.AudioRecord;
import java.util.function.Consumer;

/* JADX INFO: loaded from: classes.dex */
public interface d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final AudioFormat f19944a = new AudioFormat.Builder().setSampleRate(16000).setEncoding(2).setChannelMask(16).build();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final int f19945b = AudioRecord.getMinBufferSize(16000, 16, 2);

    static d of(int i6) {
        if (i6 == 2) {
            return new f();
        }
        if (i6 == 3 || i6 == 4) {
            return new h();
        }
        throw new IllegalArgumentException("Unexpected value: " + i6);
    }

    void a(Consumer consumer);

    void stop();
}
