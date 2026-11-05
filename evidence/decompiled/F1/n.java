package F1;

import java.io.IOException;
import p5.A;
import p5.H;
import p5.I;

/* JADX INFO: loaded from: classes.dex */
public final class n implements A {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final J1.i f1593a;

    public n(J1.i iVar) {
        p135u2.m.e(iVar, "cryptoService");
        this.f1593a = iVar;
    }

    @Override // p5.A
    public H a(A.a aVar) throws IOException {
        p135u2.m.e(aVar, "chain");
        H hA = aVar.a(aVar.h());
        I iF = hA.f();
        if (iF != null) {
            if (p135u2.m.a(H.H(hA, "EncryptVersion", null, 2, null), this.f1593a.d())) {
                try {
                    return hA.V().b(I.f20380c.d(this.f1593a.b(iF.e()), iF.o())).c();
                } catch (Exception e6) {
                    throw new IOException("Error occurs while parsing response", e6);
                }
            }
            if (hA.q() == 200) {
                throw new IOException("Header EncryptVersion not found.");
            }
        }
        return hA;
    }
}
