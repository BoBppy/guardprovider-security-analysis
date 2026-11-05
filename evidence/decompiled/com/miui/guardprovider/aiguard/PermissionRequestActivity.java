package com.miui.guardprovider.aiguard;

import android.R;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spanned;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.miui.guardprovider.aiguard.PermissionRequestActivity;
import java.util.Locale;
import java.util.Map;
import kotlin.Metadata;
import miuix.appcompat.app.n;
import miuix.appcompat.app.p;
import miuix.core.util.SystemProperties;
import p038f2.D;
import p058i1.G;
import p058i1.H;
import p058i1.K;
import p058i1.L;
import p129t2.l;
import p135u2.m;
import tmsdk.common.roach.nest.PowerNest;
import tmsdk.common.tcc.SmsCheckerContentTypes;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u000f\u0010\u0005\u001a\u00020\u0004H\u0002¢\u0006\u0004\b\u0005\u0010\u0003J\u000f\u0010\u0007\u001a\u00020\u0006H\u0002¢\u0006\u0004\b\u0007\u0010\bJ\u000f\u0010\t\u001a\u00020\u0004H\u0002¢\u0006\u0004\b\t\u0010\u0003J\u000f\u0010\n\u001a\u00020\u0004H\u0002¢\u0006\u0004\b\n\u0010\u0003J\u000f\u0010\u000b\u001a\u00020\u0004H\u0002¢\u0006\u0004\b\u000b\u0010\u0003J\u000f\u0010\f\u001a\u00020\u0004H\u0002¢\u0006\u0004\b\f\u0010\u0003J\u000f\u0010\r\u001a\u00020\u0004H\u0002¢\u0006\u0004\b\r\u0010\u0003J\u0019\u0010\u0010\u001a\u00020\u00042\b\u0010\u000f\u001a\u0004\u0018\u00010\u000eH\u0014¢\u0006\u0004\b\u0010\u0010\u0011R \u0010\u0017\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00140\u00130\u00128\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b\u0015\u0010\u0016¨\u0006\u0018"}, d2 = {"Lcom/miui/guardprovider/aiguard/PermissionRequestActivity;", "Lmiuix/appcompat/app/p;", "<init>", "()V", "Lf2/D;", "z1", com.xiaomi.onetrack.util.a.f14247c, "y1", "()Z", "A1", "D1", "M1", "I1", "Q1", "Landroid/os/Bundle;", "savedInstanceState", "onCreate", "(Landroid/os/Bundle;)V", "Le/c;", com.xiaomi.onetrack.util.a.f14247c, com.xiaomi.onetrack.util.a.f14247c, "F", "Le/c;", "requestPermission", "ai-guard_release"}, k = 1, mv = {2, 2, 0}, xi = SmsCheckerContentTypes.TYPE_TEL_NET_TEL)
public final class PermissionRequestActivity extends p {

    /* JADX INFO: renamed from: F, reason: collision with root package name and from kotlin metadata */
    private final p028e.c requestPermission = r0(new p035f.b(), new p028e.b() { // from class: i1.s
        @Override // p028e.b
        public final void a(Object obj) {
            PermissionRequestActivity.C1(this.f15834a, (Map) obj);
        }
    });

    private final void A1() {
        p098o1.b.f19925h.b(this, new l() { // from class: i1.t
            @Override // p129t2.l
            public final Object m(Object obj) {
                return PermissionRequestActivity.B1(this.f15835b, ((Boolean) obj).booleanValue());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final D B1(PermissionRequestActivity permissionRequestActivity, boolean z6) {
        if (z6) {
            permissionRequestActivity.setResult(-1);
        }
        permissionRequestActivity.finish();
        return D.f14789a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void C1(PermissionRequestActivity permissionRequestActivity, Map map) {
        m.e(map, "grantedResult");
        if (!m.a(map.get("android.permission.RECORD_AUDIO"), Boolean.TRUE)) {
            permissionRequestActivity.Q1();
        } else {
            Log.i("PermissionRequest", "Required permissions have been granted.");
            permissionRequestActivity.D1();
        }
    }

    private final void D1() {
        boolean z6 = SystemProperties.getBoolean("debug.config.media.video.voip.detect", false);
        final SharedPreferences preferences = getPreferences(0);
        boolean z7 = preferences.getBoolean("ai_guard_projection_permission", false);
        if (z6 || z7) {
            A1();
            return;
        }
        CharSequence charSequenceLoadLabel = getApplicationInfo().loadLabel(getPackageManager());
        m.d(charSequenceLoadLabel, "loadLabel(...)");
        n.a aVar = new n.a(this, L.f15806b);
        aVar.p(getString(K.f15796r, new Object[]{charSequenceLoadLabel}));
        aVar.i(K.f15793o);
        aVar.l(new DialogInterface.OnCancelListener() { // from class: i1.C
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                PermissionRequestActivity.E1(this.f15768b, dialogInterface);
            }
        });
        aVar.d(true);
        aVar.f(H.f15775b);
        int iA = (int) J.f.a(32.0f, aVar.b().getResources().getDisplayMetrics());
        aVar.h(iA, iA);
        aVar.k(K.f15794p, new DialogInterface.OnClickListener() { // from class: i1.D
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i6) {
                PermissionRequestActivity.F1(this.f15769b, dialogInterface, i6);
            }
        });
        aVar.n(K.f15795q, new DialogInterface.OnClickListener() { // from class: i1.E
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i6) {
                PermissionRequestActivity.G1(preferences, this, dialogInterface, i6);
            }
        });
        final n nVarR = aVar.r();
        nVarR.s().post(new Runnable() { // from class: i1.F
            @Override // java.lang.Runnable
            public final void run() {
                PermissionRequestActivity.H1(nVarR);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void E1(PermissionRequestActivity permissionRequestActivity, DialogInterface dialogInterface) {
        permissionRequestActivity.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void F1(PermissionRequestActivity permissionRequestActivity, DialogInterface dialogInterface, int i6) {
        permissionRequestActivity.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void G1(SharedPreferences sharedPreferences, PermissionRequestActivity permissionRequestActivity, DialogInterface dialogInterface, int i6) {
        m.b(sharedPreferences);
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        editorEdit.putBoolean("ai_guard_projection_permission", true);
        editorEdit.apply();
        permissionRequestActivity.A1();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void H1(n nVar) {
        nVar.s().setGravity(17);
    }

    private final void I1() {
        n.a aVar = new n.a(this, L.f15806b);
        aVar.o(K.f15799u);
        aVar.i(K.f15798t);
        aVar.k(R.string.cancel, new DialogInterface.OnClickListener() { // from class: i1.u
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i6) {
                PermissionRequestActivity.J1(this.f15836b, dialogInterface, i6);
            }
        });
        aVar.n(K.f15792n, new DialogInterface.OnClickListener() { // from class: i1.v
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i6) {
                PermissionRequestActivity.K1(this.f15837b, dialogInterface, i6);
            }
        });
        aVar.l(new DialogInterface.OnCancelListener() { // from class: i1.w
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                PermissionRequestActivity.L1(this.f15838b, dialogInterface);
            }
        });
        aVar.r();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void J1(PermissionRequestActivity permissionRequestActivity, DialogInterface dialogInterface, int i6) {
        permissionRequestActivity.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void K1(PermissionRequestActivity permissionRequestActivity, DialogInterface dialogInterface, int i6) {
        permissionRequestActivity.requestPermission.a(G.f15773a);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void L1(PermissionRequestActivity permissionRequestActivity, DialogInterface dialogInterface) {
        permissionRequestActivity.finish();
    }

    private final void M1() {
        n.a aVar = new n.a(this, L.f15806b);
        aVar.o(K.f15803y);
        Locale locale = Locale.getDefault();
        String str = "https://privacy.mi.com/AIalert/" + locale.getLanguage() + "_" + locale.getCountry();
        Spanned spannedA = I.a.a(getString((getIntent().getIntExtra("capabilities", 1) & 2) == 0 ? K.f15802x : K.f15801w, new Object[]{str, str}), 63);
        m.d(spannedA, "fromHtml(...)");
        aVar.l(new DialogInterface.OnCancelListener() { // from class: i1.z
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                PermissionRequestActivity.N1(this.f15841b, dialogInterface);
            }
        });
        aVar.j(spannedA);
        aVar.n(K.f15800v, new DialogInterface.OnClickListener() { // from class: i1.A
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i6) {
                PermissionRequestActivity.O1(this.f15766b, dialogInterface, i6);
            }
        });
        aVar.k(R.string.cancel, new DialogInterface.OnClickListener() { // from class: i1.B
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i6) {
                PermissionRequestActivity.P1(this.f15767b, dialogInterface, i6);
            }
        });
        TextView textViewS = aVar.r().s();
        if (textViewS != null) {
            textViewS.setMovementMethod(miuix.androidbasewidget.widget.d.getInstance());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void N1(PermissionRequestActivity permissionRequestActivity, DialogInterface dialogInterface) {
        permissionRequestActivity.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void O1(PermissionRequestActivity permissionRequestActivity, DialogInterface dialogInterface, int i6) {
        if (permissionRequestActivity.y1()) {
            permissionRequestActivity.D1();
        } else {
            permissionRequestActivity.I1();
        }
        Settings.Secure.putInt(permissionRequestActivity.getContentResolver(), "com.miui.guardprovider.AI_GUARD_PRIVACY_AGREEMENT", 1);
        SharedPreferences preferences = permissionRequestActivity.getPreferences(0);
        m.d(preferences, "getPreferences(...)");
        SharedPreferences.Editor editorEdit = preferences.edit();
        editorEdit.putBoolean("ai_guard_first_open_guiding", false);
        editorEdit.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void P1(PermissionRequestActivity permissionRequestActivity, DialogInterface dialogInterface, int i6) {
        permissionRequestActivity.finish();
    }

    private final void Q1() {
        n.a aVar = new n.a(this, L.f15806b);
        aVar.o(K.f15797s);
        aVar.i(K.f15791m);
        aVar.l(new DialogInterface.OnCancelListener() { // from class: i1.r
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                PermissionRequestActivity.R1(this.f15833b, dialogInterface);
            }
        });
        aVar.n(K.f15792n, new DialogInterface.OnClickListener() { // from class: i1.x
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i6) {
                PermissionRequestActivity.S1(this.f15839b, dialogInterface, i6);
            }
        });
        aVar.k(R.string.cancel, new DialogInterface.OnClickListener() { // from class: i1.y
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i6) {
                PermissionRequestActivity.T1(this.f15840b, dialogInterface, i6);
            }
        });
        aVar.r();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void R1(PermissionRequestActivity permissionRequestActivity, DialogInterface dialogInterface) {
        permissionRequestActivity.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void S1(PermissionRequestActivity permissionRequestActivity, DialogInterface dialogInterface, int i6) {
        if (permissionRequestActivity.shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO")) {
            permissionRequestActivity.requestPermission.a(G.f15773a);
            return;
        }
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR_PRIVATE").putExtra("extra_pkgname", permissionRequestActivity.getPackageName()).setPackage("com.miui.securitycenter");
        m.d(intent, "setPackage(...)");
        permissionRequestActivity.startActivity(intent);
        permissionRequestActivity.finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void T1(PermissionRequestActivity permissionRequestActivity, DialogInterface dialogInterface, int i6) {
        permissionRequestActivity.finish();
    }

    private final boolean y1() {
        return checkSelfPermission("android.permission.RECORD_AUDIO") == 0;
    }

    private final void z1() {
        boolean z6 = getPreferences(0).getBoolean("ai_guard_first_open_guiding", true);
        int i6 = Settings.Secure.getInt(getContentResolver(), "com.miui.guardprovider.AI_GUARD_PRIVACY_AGREEMENT", 0);
        if (z6 || i6 == 0) {
            M1();
        } else if (y1()) {
            D1();
        } else {
            Q1();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.p, androidx.fragment.app.AbstractActivityC0802j, androidx.activity.ComponentActivity, p156y.d, android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(PowerNest.SHARK_TCP);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.layoutInDisplayCutoutMode = 1;
        window.setAttributes(attributes);
        z1();
    }
}
