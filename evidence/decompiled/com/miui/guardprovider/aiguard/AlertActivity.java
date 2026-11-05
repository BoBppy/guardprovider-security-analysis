package com.miui.guardprovider.aiguard;

import android.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.DynamicEffect;
import android.os.HapticPlayer;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import androidx.fragment.app.DialogInterfaceOnCancelListenerC0797e;
import com.miui.guardprovider.aiguard.service.FeedbackResponse;
import com.xiaomi.onetrack.api.ah;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import miuix.appcompat.app.n;
import miuix.appcompat.app.p;
import org.json.JSONObject;
import p038f2.q;
import p058i1.I;
import p058i1.J;
import p058i1.K;
import p058i1.L;
import tmsdk.common.roach.nest.PowerNest;

/* JADX INFO: loaded from: classes.dex */
public class AlertActivity extends p {

    /* JADX INFO: renamed from: F, reason: collision with root package name */
    private int f12509F;

    /* JADX INFO: renamed from: G, reason: collision with root package name */
    private String f12510G;

    /* JADX INFO: renamed from: H, reason: collision with root package name */
    private p065j1.a f12511H;

    interface a {
        p086m1.c a();
    }

    public static final class b extends DialogInterfaceOnCancelListenerC0797e {

        /* JADX INFO: renamed from: C0, reason: collision with root package name */
        private HapticPlayer f12512C0;

        /* JADX INFO: renamed from: D0, reason: collision with root package name */
        private p065j1.a f12513D0;

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void Q1(AlertActivity alertActivity, DialogInterface dialogInterface, int i6) {
            T1(true);
            this.f12513D0.a("click", new q(ah.ab, "1127.1.0.1.37702"), new q("trigger_scenario", alertActivity.f12510G), new q("click_content", "忽略"));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void R1(AlertActivity alertActivity, DialogInterface dialogInterface, int i6) {
            this.f12513D0.a("click", new q(ah.ab, "1127.1.0.1.37702"), new q("trigger_scenario", alertActivity.f12510G), new q("click_content", "挂断"));
            ((TelecomManager) alertActivity.getSystemService(TelecomManager.class)).endCall();
            alertActivity.finish();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void S1(AlertActivity alertActivity, DialogInterface dialogInterface, int i6) {
            T1(false);
            this.f12513D0.a("click", new q(ah.ab, "1127.1.0.1.37702"), new q("trigger_scenario", alertActivity.f12510G), new q("click_content", "知道了"));
        }

        private void T1(boolean z6) {
            D1();
            Bundle bundle = new Bundle();
            bundle.putBoolean("AlertActivity_Feedback_voiceCall", z6);
            c cVar = new c();
            cVar.t1(bundle);
            cVar.M1(D(), "AlertActivity_Feedback");
        }

        @Override // androidx.fragment.app.DialogInterfaceOnCancelListenerC0797e
        public Dialog G1(Bundle bundle) {
            final AlertActivity alertActivity = (AlertActivity) l1();
            int i6 = alertActivity.f12509F;
            this.f12513D0 = alertActivity.f12511H;
            if (bundle == null) {
                HapticPlayer hapticPlayer = new HapticPlayer(DynamicEffect.create("{\n  \"Metadata\": {\n    \"Version\": 1,\n    \"Created\": \"2024-09-10\",\n    \"Description\": \"alert haptic\"\n  },\n  \"Pattern\": [\n    {\n      \"Event\": {\n        \"Type\": \"continuous\",\n        \"RelativeTime\": 0,\n        \"Duration\": 3000,\n        \"Parameters\": {\n          \"Intensity\": 100,\n          \"Frequency\": 50,\n          \"Curve\": [\n            {\n              \"Time\": 0,\n              \"Intensity\": 0,\n              \"Frequency\": 0\n            },\n            {\n              \"Time\": 1,\n              \"Intensity\": 1.0,\n              \"Frequency\": 0\n            },\n            {\n              \"Time\": 2999,\n              \"Intensity\": 1.0,\n              \"Frequency\": 0\n            },\n            {\n              \"Time\": 3000,\n              \"Intensity\": 0,\n              \"Frequency\": 0\n            }\n          ]\n        }\n      }\n    }\n  ]\n}"));
                this.f12512C0 = hapticPlayer;
                hapticPlayer.start();
                this.f12513D0.a("expose", new q(ah.ab, "1127.1.0.1.37701"), new q("trigger_scenario", alertActivity.f12510G));
            }
            K1(false);
            n.a aVar = new n.a(alertActivity, L.f15805a);
            aVar.k(K.f15781c, new DialogInterface.OnClickListener() { // from class: i1.i
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i7) {
                    this.f15815b.Q1(alertActivity, dialogInterface, i7);
                }
            });
            if (i6 == 1073741824) {
                aVar.n(K.f15783e, new DialogInterface.OnClickListener() { // from class: i1.j
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i7) {
                        this.f15817b.R1(alertActivity, dialogInterface, i7);
                    }
                });
            }
            View viewInflate = y().inflate(J.f15778a, (ViewGroup) null, false);
            TextView textView = (TextView) viewInflate.findViewById(I.f15777b);
            TextView textView2 = (TextView) viewInflate.findViewById(I.f15776a);
            if ((i6 & 268435456) != 0) {
                textView2.setText(K.f15780b);
                aVar.k(K.f15782d, new DialogInterface.OnClickListener() { // from class: i1.k
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i7) {
                        this.f15819b.S1(alertActivity, dialogInterface, i7);
                    }
                });
            }
            textView.setText(I.a.a(Q(K.f15779a), 63));
            aVar.q(viewInflate);
            return aVar.a();
        }

        @Override // androidx.fragment.app.DialogInterfaceOnCancelListenerC0797e, android.content.DialogInterface.OnDismissListener
        public void onDismiss(DialogInterface dialogInterface) {
            HapticPlayer hapticPlayer = this.f12512C0;
            if (hapticPlayer != null) {
                hapticPlayer.stop();
                this.f12512C0 = null;
            }
        }
    }

    public static final class c extends DialogInterfaceOnCancelListenerC0797e {

        /* JADX INFO: renamed from: C0, reason: collision with root package name */
        private p065j1.a f12514C0;

        /* JADX INFO: renamed from: D0, reason: collision with root package name */
        private com.miui.securitycenter.confidentialcomputing.c f12515D0;

        /* JADX INFO: renamed from: E0, reason: collision with root package name */
        private boolean f12516E0;

        /* JADX INFO: renamed from: F0, reason: collision with root package name */
        private final DialogInterface.OnClickListener f12517F0 = new DialogInterface.OnClickListener() { // from class: i1.l
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i6) {
                this.f15821b.S1(dialogInterface, i6);
            }
        };

        private void P1(Context context) {
            try {
                i.h(context).f();
            } catch (IOException unused) {
            }
        }

        private String Q1(Path path, String str) throws Exception {
            com.miui.securitycenter.confidentialcomputing.c.b bVarA = this.f12515D0.a(false);
            try {
                String strEncodeToString = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("requestId", UUID.randomUUID().toString());
                jSONObject.put("audioPCM", strEncodeToString);
                jSONObject.put("audioText", str);
                String json = bVarA.a(jSONObject.toString().getBytes()).toJson();
                p014c.c.a(bVarA);
                return json;
            } catch (Throwable th) {
                if (bVarA != null) {
                    try {
                        p014c.c.a(bVarA);
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        private String R1(Path path) throws Exception {
            com.miui.securitycenter.confidentialcomputing.c.b bVarA = this.f12515D0.a(false);
            try {
                String strEncodeToString = Base64.getEncoder().encodeToString(Files.readAllBytes(path));
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("fileType", "MP4");
                jSONObject.put("requestId", UUID.randomUUID().toString());
                jSONObject.put("fileBase64", strEncodeToString);
                String json = bVarA.a(jSONObject.toString().getBytes()).toJson();
                p014c.c.a(bVarA);
                return json;
            } catch (Throwable th) {
                if (bVarA != null) {
                    try {
                        p014c.c.a(bVarA);
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void S1(DialogInterface dialogInterface, int i6) {
            AlertActivity alertActivity = (AlertActivity) l1();
            if (i6 == -2) {
                this.f12514C0.a("click", new q(ah.ab, "1127.1.0.1.45358"), new q("click_content", "取消"), new q("trigger_scenario", alertActivity.f12510G));
                P1(alertActivity);
            } else if (i6 == -1) {
                this.f12514C0.a("click", new q(ah.ab, "1127.1.0.1.45358"), new q("click_content", "标记为误报"), new q("trigger_scenario", alertActivity.f12510G));
                this.f12515D0 = new com.miui.securitycenter.confidentialcomputing.c(l1());
                U1(this.f12516E0);
            }
            alertActivity.finish();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void T1(boolean z6, i iVar, p086m1.c cVar, Context context) {
            try {
                if (z6) {
                    Log.i("AlertActivity_Feedback", "submitFeedback: " + ((FeedbackResponse) cVar.b(Q1(iVar.i(), iVar.g().toString())).g().a()));
                } else {
                    Path pathJ = iVar.j();
                    if (Files.exists(pathJ, new LinkOption[0])) {
                        Log.i("AlertActivity_Feedback", "submitFeedback: " + ((FeedbackResponse) cVar.a(R1(pathJ)).g().a()));
                    }
                }
                P1(context);
            } catch (Exception e6) {
                Log.e("AlertActivity_Feedback", "submitFeedback: ", e6);
            }
        }

        private void U1(final boolean z6) {
            final p086m1.c cVarA = h.a().a().a();
            ExecutorService executorServiceNewSingleThreadExecutor = Executors.newSingleThreadExecutor();
            final Context applicationContext = l1().getApplicationContext();
            final i iVarH = i.h(applicationContext);
            executorServiceNewSingleThreadExecutor.execute(new Runnable() { // from class: i1.m
                @Override // java.lang.Runnable
                public final void run() {
                    this.f15822b.T1(z6, iVarH, cVarA, applicationContext);
                }
            });
        }

        @Override // androidx.fragment.app.DialogInterfaceOnCancelListenerC0797e
        public Dialog G1(Bundle bundle) {
            AlertActivity alertActivity = (AlertActivity) l1();
            this.f12516E0 = m1().getBoolean("AlertActivity_Feedback_voiceCall", true);
            p065j1.a aVar = alertActivity.f12511H;
            this.f12514C0 = aVar;
            if (bundle == null) {
                aVar.a("expose", new q(ah.ab, "1127.1.0.1.45357"), new q("trigger_scenario", alertActivity.f12510G));
            }
            return new n.a(alertActivity, L.f15805a).o(K.f15787i).i(this.f12516E0 ? K.f15784f : K.f15785g).k(R.string.cancel, this.f12517F0).n(K.f15786h, this.f12517F0).a();
        }

        @Override // androidx.fragment.app.DialogInterfaceOnCancelListenerC0797e, android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            this.f12517F0.onClick(dialogInterface, -2);
        }
    }

    private static String m1(int i6) {
        if (i6 == 536870913) {
            return "QQ语音通话";
        }
        if (i6 == 268435457) {
            return "QQ视频通话";
        }
        if (i6 == 536870914) {
            return "微信语音通话";
        }
        return i6 == 268435458 ? "微信视频通话" : "电话";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // miuix.appcompat.app.p, androidx.fragment.app.AbstractActivityC0802j, androidx.activity.ComponentActivity, p156y.d, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.clearFlags(PowerNest.SHARK_TCP);
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.layoutInDisplayCutoutMode = 1;
        window.setAttributes(attributes);
        this.f12511H = p065j1.a.b(this);
        int intExtra = getIntent().getIntExtra("alert_cause", 268435456);
        this.f12509F = intExtra;
        this.f12510G = m1(intExtra);
        Log.i("AlertActivity", "alert cause: " + this.f12509F);
        if (bundle == null) {
            new b().M1(y0(), "AlertActivity_Alert");
        }
    }
}
