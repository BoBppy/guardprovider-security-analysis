package com.miui.guardprovider.manager;

import C1.g;
import C1.j;
import C1.k;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.miui.guardprovider.GuardApplication;
import java.util.List;
import p051h1.c;
import p051h1.e;
import p116r1.d;
import p152x1.h;

/* JADX INFO: loaded from: classes.dex */
public class SecurityService extends Service {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final p051h1.a.AbstractBinderC0220a f12576b = new a();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final h1.b.a f12577c = new b();

    class a extends p051h1.a.AbstractBinderC0220a {
        a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void k0(String str) {
            d.g(SecurityService.this).b(str);
            p116r1.b.f(SecurityService.this).b(str);
        }

        @Override // p051h1.a
        public int E(c cVar) {
            if (!SecurityService.this.c()) {
                L1.c.a("d", "SecurityService", "updateScan on Server");
                return g.y().L(cVar);
            }
            SecurityService.this.d("updateScan on Server, but user is locked, skip.");
            e eVar = new e();
            eVar.f15643b = com.xiaomi.onetrack.util.a.f14247c;
            eVar.f15644c = 2;
            if (cVar == null) {
                return 0;
            }
            try {
                cVar.l(eVar);
                cVar.f0(-1);
                return 0;
            } catch (RemoteException e6) {
                e6.printStackTrace();
                return 0;
            }
        }

        @Override // p051h1.a
        public int F() {
            if (SecurityService.this.c()) {
                SecurityService.this.d("detectAppsScan on Server, but user is locked, skip.");
                return -1;
            }
            p158y1.c.i(GuardApplication.application);
            return 0;
        }

        @Override // p051h1.a
        public void G(final String str, int i6) {
            if (i6 == 2 || i6 == 3) {
                C1.a.b().a(new Runnable() { // from class: com.miui.guardprovider.manager.a
                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f12580b.k0(str);
                    }
                });
            } else if (i6 == 1) {
                p152x1.d dVarD = p152x1.d.d();
                dVarD.b(SecurityService.this);
                dVarD.a().b(SecurityService.this, str);
            }
        }

        @Override // p051h1.a
        public void J(c cVar, boolean z6) {
            g.y().B(cVar, z6);
        }

        @Override // p051h1.a
        public void K(int i6) {
            if (SecurityService.this.c()) {
                SecurityService.this.d("cancelScan on Server, but user is locked, skip.");
            } else {
                g.y().p(i6);
                L1.c.a("d", "SecurityService", "cancelScan on Server");
            }
        }

        @Override // p051h1.a
        public List M(List list) {
            return g.y().M(list);
        }

        @Override // p051h1.a
        public int N(String str, p051h1.d dVar) {
            if (!SecurityService.this.c()) {
                return k.a().e(str, dVar);
            }
            SecurityService.this.d("wifiDetectNetworkState on Server, but user is locked, skip.");
            return 0;
        }

        @Override // p051h1.a
        public void O(int i6) {
            if (SecurityService.this.c()) {
                SecurityService.this.d("cancelUpdate on Server, but user is locked, skip.");
            } else {
                L1.c.a("d", "SecurityService", "cancelUpdate on Server");
                g.y().q(i6);
            }
        }

        @Override // p051h1.a
        public String R(String str) {
            if (!SecurityService.this.c()) {
                return p158y1.c.c(str);
            }
            SecurityService.this.d("detectAppScan on Server, but user is locked, skip.");
            return null;
        }

        @Override // p051h1.a
        public int S(String[] strArr, c cVar, boolean z6) {
            if (!SecurityService.this.c()) {
                L1.c.a("d", "SecurityService", "startScan on Server");
                return g.y().K(strArr, cVar, z6, 0, null);
            }
            SecurityService.this.d("startScan on Server, but user is locked, skip.");
            if (cVar == null) {
                return 0;
            }
            try {
                cVar.b0(-1, null);
                return 0;
            } catch (RemoteException e6) {
                e6.printStackTrace();
                return 0;
            }
        }

        @Override // p051h1.a
        public int U(String str, p051h1.d dVar, String str2) {
            if (!SecurityService.this.c()) {
                return k.a().c(str, dVar, str2);
            }
            SecurityService.this.d("wifiDetectArp on Server, but user is locked, skip.");
            return 0;
        }

        @Override // p051h1.a
        public boolean Z(String str) {
            if (SecurityService.this.c()) {
                SecurityService.this.d("getEngineEnable on Server, but user is locked, skip.");
                return false;
            }
            L1.c.a("d", "SecurityService", "getEngineEnable engineName : " + str);
            return g.y().v(str);
        }

        @Override // p051h1.a
        public int b(String str, p051h1.d dVar) {
            if (!SecurityService.this.c()) {
                return k.a().f(str, dVar);
            }
            SecurityService.this.d("wifiDetectSecurity on Server, but user is locked, skip.");
            return 0;
        }

        @Override // p051h1.a
        public String[] d() {
            return g.y().w();
        }

        @Override // p051h1.a
        public int e(String str, p051h1.d dVar) {
            if (!SecurityService.this.c()) {
                return k.a().d(str, dVar);
            }
            SecurityService.this.d("wifiDetectDnsAndPhishing on Server, but user is locked, skip.");
            return 0;
        }

        @Override // p051h1.a
        public String e0() {
            L1.c.a("d", "SecurityService", "getVersion on Server");
            return "1";
        }

        @Override // p051h1.a
        public void f(String str, boolean z6) {
            if (SecurityService.this.c()) {
                SecurityService.this.d("setEngineEnable on Server, but user is locked, skip.");
                return;
            }
            L1.c.a("d", "SecurityService", "setEngineEnable engineName : " + str);
            g.y().H(str, z6);
        }

        @Override // p051h1.a
        public boolean i0(String str) {
            L1.c.a("d", "SecurityService", "isSupportCloudScan engineName : " + str);
            return g.y().E(str);
        }

        @Override // p051h1.a
        public int k(String[] strArr, c cVar, boolean z6, int i6) {
            if (!SecurityService.this.c()) {
                L1.c.a("d", "SecurityService", "startScanAsScene on Server");
                return g.y().K(strArr, cVar, z6, i6, null);
            }
            SecurityService.this.d("startScanAsScene on Server, but user is locked, skip.");
            if (cVar == null) {
                return 0;
            }
            try {
                cVar.b0(-1, null);
                return 0;
            } catch (RemoteException e6) {
                e6.printStackTrace();
                return 0;
            }
        }

        @Override // p051h1.a
        public String n(String str) {
            L1.c.a("d", "SecurityService", "getNameString : " + str);
            return g.y().x(str);
        }

        @Override // p051h1.a
        public List p(List list) {
            return g.y().N(list);
        }

        @Override // p051h1.a
        public boolean v() {
            if (h.h(GuardApplication.application)) {
                return !d.g(GuardApplication.application).f().isEmpty();
            }
            return false;
        }

        @Override // p051h1.a
        public int y(c cVar) {
            if (SecurityService.this.c()) {
                SecurityService.this.d("checkUpdate on Server, but user is locked, skip.");
                return -1;
            }
            L1.c.a("d", "SecurityService", "checkUpdate on Server");
            return g.y().r();
        }

        @Override // p051h1.a
        public String[] z() {
            if (!SecurityService.this.c()) {
                return p158y1.c.a();
            }
            SecurityService.this.d("detectAppsScan on Server, but user is locked, skip.");
            return new String[0];
        }
    }

    class b extends h1.b.a {
        b() {
        }

        @Override // p051h1.b
        public int H(String str, String str2) {
            L1.c.a("d", "SecurityService", "GP AVL scanURL");
            return j.a().b(str, str2);
        }

        @Override // p051h1.b
        public int i(String str) {
            if (SecurityService.this.c()) {
                SecurityService.this.d("GP AVL updateURLRule, but user is locked, skip.");
                return -2;
            }
            L1.c.a("d", "SecurityService", "GP AVL updateURLRule");
            return j.a().c(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean c() {
        return !G.j.a(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d(String str) {
        Log.d("SecurityService", "------ " + str + "------");
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        d("service on bind");
        if (intent == null) {
            return null;
        }
        if ("com.miui.guardprovider.action.antivirusservice".equals(intent.getAction())) {
            return this.f12576b;
        }
        if ("com.miui.guardprovider.action.urlscan".equals(intent.getAction())) {
            return this.f12577c;
        }
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        d("service create");
    }

    @Override // android.app.Service
    public void onDestroy() {
        d("service on destroy");
        super.onDestroy();
    }

    @Override // android.app.Service
    public void onRebind(Intent intent) {
        d("service on rebind");
        super.onRebind(intent);
    }

    @Override // android.app.Service
    public void onStart(Intent intent, int i6) {
        d("service start id=" + i6);
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i6, int i7) {
        d("onStartCommand");
        return 1;
    }

    @Override // android.app.Service
    public boolean onUnbind(Intent intent) {
        d("service on unbind");
        return super.onUnbind(intent);
    }
}
