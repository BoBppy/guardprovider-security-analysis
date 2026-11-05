package p058i1;

import java.util.ArrayList;
import p045g2.AbstractC0962q;
import p129t2.l;
import p135u2.m;

/* JADX INFO: loaded from: classes.dex */
public final class n {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final long f15827a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final l f15828b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final ArrayList f15829c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private long f15830d;

    public n(long j6, l lVar) {
        m.e(lVar, "contextOverflowListener");
        this.f15827a = j6;
        this.f15828b = lVar;
        this.f15829c = new ArrayList(16);
        this.f15830d = System.currentTimeMillis();
    }

    private final String b() {
        ArrayList arrayList = this.f15829c;
        String strLineSeparator = System.lineSeparator();
        m.d(strLineSeparator, "lineSeparator(...)");
        return AbstractC0962q.e0(arrayList, strLineSeparator, null, null, 0, null, null, 62, null);
    }

    public final void a(String str) {
        m.e(str, "sentence");
        this.f15829c.add(str);
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.f15830d > this.f15827a) {
            this.f15828b.m(b());
            this.f15830d = jCurrentTimeMillis;
        }
    }
}
