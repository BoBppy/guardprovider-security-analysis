package com.miui.guardprovider;

import L1.f;
import L1.g;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.util.Log;
import miui.os.Build;
import p122s1.d;

/* JADX INFO: loaded from: classes.dex */
public class GuardApplication extends b {
    public static GuardApplication application;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private BroadcastReceiver f12413d;

    class a extends BroadcastReceiver {
        a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction())) {
                f.b(context);
                Log.d("GuardProvider", "init task when boot completed");
                GuardApplication.this.g();
            }
        }
    }

    private static boolean e(Context context) {
        return f.a().d("network_status", 0) == 1;
    }

    private void f() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.USER_UNLOCKED");
        if (this.f12413d == null) {
            this.f12413d = new a();
        }
        p162z.a.g(this, this.f12413d, intentFilter, 2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        BroadcastReceiver broadcastReceiver = this.f12413d;
        if (broadcastReceiver != null) {
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (Exception e6) {
                e6.printStackTrace();
            }
            this.f12413d = null;
        }
    }

    public static boolean isAllowConnectNetwork(Context context) {
        if (!Build.IS_INTERNATIONAL_BUILD) {
            return e(context) || d.b("persist.sys.sc_allow_conn", false);
        }
        String strA = d.a("ro.miui.customized.region", com.xiaomi.onetrack.util.a.f14247c);
        boolean z6 = p122s1.b.c(context, "com.miui.securitycenter") && d.b("persist.sys.sc_allow_conn", false);
        if ("jp_kd".equals(strA)) {
            return z6 && (Settings.Secure.getInt(context.getContentResolver(), "antivirus_engine_network_enable", 0) == 1);
        }
        return z6;
    }

    @Override // android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        application = this;
        if (g.b(context)) {
            f.b(context);
        } else {
            f();
        }
    }

    @Override // com.miui.guardprovider.b, android.app.Application
    public void onCreate() {
        super.onCreate();
    }
}
