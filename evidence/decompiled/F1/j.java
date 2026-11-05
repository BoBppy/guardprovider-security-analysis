package F1;

import p5.A;
import p5.F;
import p5.G;
import p5.H;

/* JADX INFO: loaded from: classes.dex */
public final class j implements A {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final J1.i f1589a;

    public j(J1.i iVar) {
        p135u2.m.e(iVar, "cryptoService");
        this.f1589a = iVar;
    }

    @Override // p5.A
    public H a(A.a aVar) {
        p135u2.m.e(aVar, "chain");
        F fH = aVar.h();
        F.a aVarH = fH.i().h("EncryptVersion", this.f1589a.d());
        G gA = fH.a();
        if (gA == null) {
            return aVar.a(aVarH.a());
        }
        G5.d dVar = new G5.d();
        gA.g(dVar);
        aVarH.j(fH.h(), G.a.h(G.f20341a, this.f1589a.a(dVar.C()), gA.b(), 0, 0, 6, null));
        return aVar.a(aVarH.a());
    }
}
