package p072k1;

import I5.y;
import p086m1.c;
import p086m1.e;
import p086m1.g;
import p086m1.i;
import p135u2.m;
import p5.D;

/* JADX INFO: loaded from: classes.dex */
public final class a {
    public final c a(y yVar) {
        m.e(yVar, "retrofit");
        Object objB = yVar.b(c.class);
        m.d(objB, "create(...)");
        return (c) objB;
    }

    public final D b(e eVar) {
        m.e(eVar, "networkAvailabilityInterceptor");
        D.a aVar = new D.a();
        aVar.b(eVar);
        return aVar.c();
    }

    public final g c(y yVar) {
        m.e(yVar, "retrofit");
        Object objB = yVar.b(g.class);
        m.d(objB, "create(...)");
        return (g) objB;
    }

    public final y d(D d6) {
        m.e(d6, "okHttpClient");
        y yVarD = new y.b().b("https://detection-aigc.engine.miui.com").f(d6).a(J5.a.f()).d();
        m.d(yVarD, "build(...)");
        return yVarD;
    }

    public final i e(y yVar) {
        m.e(yVar, "retrofit");
        Object objB = yVar.b(i.class);
        m.d(objB, "create(...)");
        return (i) objB;
    }
}
