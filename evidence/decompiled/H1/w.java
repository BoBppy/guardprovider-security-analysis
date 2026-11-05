package H1;

import android.util.Log;
import com.miui.guardprovider.sdk.android.db.C0857c1;
import com.miui.guardprovider.sdk.android.db.C0861e;
import com.miui.guardprovider.sdk.android.db.Y;
import com.miui.guardprovider.sdk.android.pojo.ScanTarget;
import com.miui.guardprovider.sdk.android.pojo.networkdomain.CloudScanPayload;
import java.nio.file.Path;

/* JADX INFO: loaded from: classes.dex */
public final class w {

    public static final class a implements p5.A {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final com.google.gson.e f2528a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final J1.i f2529b;

        public a(com.google.gson.e eVar, J1.i iVar) {
            p135u2.m.e(eVar, "gson");
            p135u2.m.e(iVar, "cryptoService");
            this.f2528a = eVar;
            this.f2529b = iVar;
        }

        private final void b(String str, String str2) {
            if (p135u2.m.a(System.getProperty("java.vm.name"), "Dalvik")) {
                Log.d(str, str2);
                return;
            }
            System.out.println((Object) (str + ": " + str2));
        }

        @Override // p5.A
        public p5.H a(p5.A.a aVar) {
            byte[] bArrB;
            byte[] bArrE;
            p135u2.m.e(aVar, "chain");
            p5.F fA = aVar.h().i().a();
            b("MiEngineNG", "DEBUG: original request:" + fA);
            b("MiEngineNG", "DEBUG: original request body:" + fA.a());
            p5.H hC = aVar.a(fA.i().h("EncryptVersion", this.f2529b.d()).a()).V().c();
            b("MiEngineNG", "DEBUG: original response:" + hC);
            p5.I iF = hC.f();
            b("MiEngineNG", "DEBUG: original response body:" + iF);
            b("MiEngineNG", "DEBUG: original response encrypt version:" + p5.H.H(hC, "EncryptVersion", null, 2, null));
            if (iF == null || (bArrE = iF.e()) == null || (bArrB = J1.i.f3234a.b(bArrE)) == null) {
                bArrB = new byte[0];
            }
            b("MiEngineNG", "DEBUG: original response raw body:" + S3.o.x(J1.j.f3236a.a(bArrB)));
            return aVar.a(aVar.h());
        }
    }

    public final F1.a a(I5.y yVar) {
        p135u2.m.e(yVar, "retrofit");
        Object objB = yVar.b(F1.a.class);
        p135u2.m.d(objB, "create(...)");
        return (F1.a) objB;
    }

    public final F1.c b(I5.y yVar) {
        p135u2.m.e(yVar, "retrofit");
        Object objB = yVar.b(F1.c.class);
        p135u2.m.d(objB, "create(...)");
        return (F1.c) objB;
    }

    public final F1.b c(I5.y yVar) {
        p135u2.m.e(yVar, "retrofit");
        Object objB = yVar.b(F1.b.class);
        p135u2.m.d(objB, "create(...)");
        return (F1.b) objB;
    }

    public final F1.d d(I5.y yVar) {
        p135u2.m.e(yVar, "retrofit");
        Object objB = yVar.b(F1.d.class);
        p135u2.m.d(objB, "create(...)");
        return (F1.d) objB;
    }

    public final F1.e e(I5.y yVar) {
        p135u2.m.e(yVar, "retrofit");
        Object objB = yVar.b(F1.e.class);
        p135u2.m.d(objB, "create(...)");
        return (F1.e) objB;
    }

    public final F1.f f(I5.y yVar) {
        p135u2.m.e(yVar, "retrofit");
        Object objB = yVar.b(F1.f.class);
        p135u2.m.d(objB, "create(...)");
        return (F1.f) objB;
    }

    public final F1.g g(I5.y yVar) {
        p135u2.m.e(yVar, "retrofit");
        Object objB = yVar.b(F1.g.class);
        p135u2.m.d(objB, "create(...)");
        return (F1.g) objB;
    }

    public final com.google.gson.e h(Y y6, F1.u uVar, CloudScanPayload.b bVar, I1.l lVar, C0857c1 c0857c1, C0861e c0861e) {
        p135u2.m.e(y6, "conditionAdapter");
        p135u2.m.e(uVar, "scanTargetAdapter");
        p135u2.m.e(bVar, "resultTypeAdapter");
        p135u2.m.e(lVar, "statusTypeAdapter");
        p135u2.m.e(c0857c1, "pathAdapter");
        p135u2.m.e(c0861e, "androidAppGsonAdapter");
        com.google.gson.f fVarD = new com.google.gson.f().d(ScanTarget.class, uVar);
        p135u2.m.d(fVarD, "registerTypeAdapter(...)");
        com.google.gson.f fVarD2 = fVarD.d(com.miui.guardprovider.sdk.android.pojo.g.class, y6);
        p135u2.m.d(fVarD2, "registerTypeAdapter(...)");
        com.google.gson.f fVarD3 = fVarD2.d(CloudScanPayload.a.class, bVar);
        p135u2.m.d(fVarD3, "registerTypeAdapter(...)");
        com.google.gson.f fVarD4 = fVarD3.d(I1.j.class, lVar);
        p135u2.m.d(fVarD4, "registerTypeAdapter(...)");
        com.google.gson.e eVarB = fVarD4.d(Path.class, c0857c1).e(com.miui.guardprovider.sdk.android.pojo.a.class, c0861e).b();
        p135u2.m.d(eVarB, "create(...)");
        return eVarB;
    }

    public final F1.h i(I5.y yVar) {
        p135u2.m.e(yVar, "retrofit");
        Object objB = yVar.b(F1.h.class);
        p135u2.m.d(objB, "create(...)");
        return (F1.h) objB;
    }

    public final F1.i j(I5.y yVar) {
        p135u2.m.e(yVar, "retrofit");
        Object objB = yVar.b(F1.i.class);
        p135u2.m.d(objB, "create(...)");
        return (F1.i) objB;
    }

    public final p5.D k(F1.w wVar, F1.r rVar, F1.l lVar) {
        p135u2.m.e(wVar, "signGenInterceptor");
        p135u2.m.e(rVar, "networkInterceptor");
        p135u2.m.e(lVar, "gzip");
        p5.D.a aVar = new p5.D.a();
        aVar.b(rVar);
        aVar.a(wVar);
        aVar.a(lVar);
        return aVar.c();
    }

    public final p5.D l(F1.w wVar, F1.l lVar, F1.j jVar, F1.p pVar, F1.n nVar, F1.r rVar, a aVar) {
        p135u2.m.e(wVar, "signGenInterceptor");
        p135u2.m.e(lVar, "zipReq");
        p135u2.m.e(jVar, "encryptReq");
        p135u2.m.e(pVar, "unzipResp");
        p135u2.m.e(nVar, "decryptResp");
        p135u2.m.e(rVar, "networkInterceptor");
        p135u2.m.e(aVar, "log");
        p5.D.a aVar2 = new p5.D.a();
        aVar2.b(rVar);
        aVar2.a(wVar);
        aVar2.a(lVar);
        aVar2.a(jVar);
        aVar2.a(pVar);
        aVar2.a(nVar);
        return aVar2.c();
    }

    public final F1.t m(I5.y yVar) {
        p135u2.m.e(yVar, "retrofit");
        Object objB = yVar.b(F1.t.class);
        p135u2.m.d(objB, "create(...)");
        return (F1.t) objB;
    }

    public final I5.y n(p5.D d6, com.google.gson.e eVar) {
        p135u2.m.e(d6, "client");
        p135u2.m.e(eVar, "gson");
        I5.y yVarD = new I5.y.b().b("https://virus-engine.sec.miui.com").f(d6).a(J5.a.g(eVar)).d();
        p135u2.m.d(yVarD, "build(...)");
        return yVarD;
    }

    public final I5.y o(p5.D d6) {
        p135u2.m.e(d6, "client");
        I5.y yVarD = new I5.y.b().b("https://fraud-detection.engine.miui.com").f(d6).a(J5.a.g(new com.google.gson.e())).d();
        p135u2.m.b(yVarD);
        return yVarD;
    }

    public final F1.w p() {
        return new F1.w("62b36629-ecb4-4725-9f33-ccb0fd158191");
    }

    public final F1.w q() {
        return new F1.w("39170bda-b0f2-4203-9968-25b238db4b57");
    }
}
