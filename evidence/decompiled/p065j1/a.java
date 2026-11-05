package p065j1;

import android.content.Context;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import p038f2.D;
import p038f2.q;
import p045g2.L;
import p086m1.e;
import p135u2.m;

/* JADX INFO: loaded from: classes.dex */
public interface a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final C0238a f16479a = C0238a.f16480a;

    /* JADX INFO: renamed from: j1.a$a, reason: collision with other inner class name */
    public static final class C0238a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        static final /* synthetic */ C0238a f16480a = new C0238a();

        private C0238a() {
        }

        public final a a(Context context) {
            m.e(context, "context");
            OneTrack.setAccessNetworkEnable(context, e.f17631a.a());
            return b.f16481a.a(context);
        }
    }

    private static final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final b f16481a = new b();

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private static volatile a f16482b;

        /* JADX INFO: renamed from: j1.a$b$a, reason: collision with other inner class name */
        public static final class C0239a implements a {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            private final OneTrack f16483b;

            C0239a(Context context) {
                OneTrack oneTrackCreateInstance = OneTrack.createInstance(context, new Configuration.Builder().setAppId("31000401489").setChannel(context.getPackageName()).setMode(OneTrack.Mode.APP).setAutoTrackActivityAction(true).build());
                m.d(oneTrackCreateInstance, "createInstance(...)");
                this.f16483b = oneTrackCreateInstance;
            }

            @Override // p065j1.a
            public void a(String str, q... qVarArr) {
                m.e(str, com.xiaomi.onetrack.b.a.f13851b);
                m.e(qVarArr, "params");
                this.f16483b.track(str, L.u(qVarArr));
            }
        }

        private b() {
        }

        public final a a(Context context) {
            m.e(context, "context");
            if (f16482b == null) {
                synchronized (b.class) {
                    try {
                        if (f16482b == null) {
                            f16482b = new C0239a(context);
                        }
                        D d6 = D.f14789a;
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            a aVar = f16482b;
            m.b(aVar);
            return aVar;
        }
    }

    static a b(Context context) {
        return f16479a.a(context);
    }

    void a(String str, q... qVarArr);
}
