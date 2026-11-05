package p092n1;

import android.util.Log;
import com.miui.guardprovider.GuardApplication;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static b f19728c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private OneTrack f19729a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Configuration f19730b;

    private b() {
        Configuration configurationBuild = new Configuration.Builder().setAppId("2882303761517522561").setChannel(GuardApplication.application.getPackageName()).setExceptionCatcherEnable(true).setMode(OneTrack.Mode.APP).setAutoTrackActivityAction(true).setGAIDEnable(false).build();
        this.f19730b = configurationBuild;
        try {
            c();
            this.f19729a = OneTrack.createInstance(GuardApplication.application, configurationBuild);
        } catch (Exception e6) {
            Log.e("OneTrackManager", "init onetrack error", e6);
        }
    }

    public static synchronized b a() {
        try {
            if (f19728c == null) {
                f19728c = new b();
            }
        } catch (Throwable th) {
            throw th;
        }
        return f19728c;
    }

    public static void c() {
        GuardApplication guardApplication = GuardApplication.application;
        OneTrack.setAccessNetworkEnable(guardApplication, GuardApplication.isAllowConnectNetwork(guardApplication));
    }

    public void b(String str, Map map) {
        OneTrack oneTrack = this.f19729a;
        if (oneTrack != null) {
            oneTrack.track(str, map);
        }
    }
}
