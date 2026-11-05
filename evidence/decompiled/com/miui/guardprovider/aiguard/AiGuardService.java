package com.miui.guardprovider.aiguard;

import S3.C0596d;
import V3.AbstractC0648g;
import V3.AbstractC0652i;
import V3.C0641c0;
import V3.H0;
import V3.InterfaceC0684y0;
import V3.M;
import V3.N;
import V3.R0;
import V3.W;
import X3.r;
import X3.t;
import Y3.AbstractC0716g;
import Y3.InterfaceC0714e;
import Y3.InterfaceC0715f;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.media.AudioRecordingConfiguration;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import com.miui.guardprovider.aiguard.AiGuardService;
import com.miui.securitycenter.confidentialcomputing.EncryptedData;
import com.xiaomi.onetrack.api.ah;
import com.xiaomi.onetrack.api.ai;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import kotlin.Metadata;
import org.json.JSONObject;
import p038f2.D;
import p038f2.s;
import p038f2.w;
import p045g2.AbstractC0962q;
import p045g2.L;
import p058i1.AbstractC0985h;
import p058i1.H;
import p058i1.K;
import tmsdk.common.tcc.SmsCheckerContentTypes;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000©\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0007*\u0001a\u0018\u00002\u00020\u0001:\u0001kB\u0007¢\u0006\u0004\b\u0002\u0010\u0003J\u001b\u0010\u0007\u001a\u0004\u0018\u00010\u00062\b\u0010\u0005\u001a\u0004\u0018\u00010\u0004H\u0016¢\u0006\u0004\b\u0007\u0010\bJ\u000f\u0010\n\u001a\u00020\tH\u0016¢\u0006\u0004\b\n\u0010\u0003J'\u0010\u000e\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u00042\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000bH\u0017¢\u0006\u0004\b\u000e\u0010\u000fJ\u000f\u0010\u0010\u001a\u00020\tH\u0016¢\u0006\u0004\b\u0010\u0010\u0003J\u000f\u0010\u0011\u001a\u00020\tH\u0000¢\u0006\u0004\b\u0011\u0010\u0003J\u0018\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0013\u001a\u00020\u0012H\u0087@¢\u0006\u0004\b\u0015\u0010\u0016J\u0018\u0010\u0019\u001a\u00020\u00142\u0006\u0010\u0018\u001a\u00020\u0017H\u0087@¢\u0006\u0004\b\u0019\u0010\u001aJ\u0018\u0010\u001c\u001a\u00020\t2\u0006\u0010\u001b\u001a\u00020\u0014H\u0082@¢\u0006\u0004\b\u001c\u0010\u001dJ7\u0010#\u001a\u00020\t2\u0006\u0010\u001e\u001a\u00020\u000b2\u0006\u0010\u001f\u001a\u00020\u000b2\u0006\u0010 \u001a\u00020\u000b2\u0006\u0010!\u001a\u00020\u000b2\u0006\u0010\"\u001a\u00020\u000bH\u0002¢\u0006\u0004\b#\u0010$J\u0018\u0010&\u001a\u00020\t2\u0006\u0010%\u001a\u00020\u000bH\u0082@¢\u0006\u0004\b&\u0010'J\u0017\u0010(\u001a\u00020\u000b2\u0006\u0010%\u001a\u00020\u000bH\u0002¢\u0006\u0004\b(\u0010)J\u0015\u0010+\u001a\b\u0012\u0004\u0012\u00020\u000b0*H\u0002¢\u0006\u0004\b+\u0010,J\u000f\u0010-\u001a\u00020\tH\u0002¢\u0006\u0004\b-\u0010\u0003J\u000f\u0010/\u001a\u00020.H\u0002¢\u0006\u0004\b/\u00100J\u0017\u00101\u001a\u00020\t2\u0006\u0010%\u001a\u00020\u000bH\u0003¢\u0006\u0004\b1\u00102J\u001b\u00105\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u000204030*H\u0002¢\u0006\u0004\b5\u0010,J\u0017\u00107\u001a\u00020\t2\u0006\u00106\u001a\u00020\u000bH\u0002¢\u0006\u0004\b7\u00102R\u0014\u0010;\u001a\u0002088\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\b9\u0010:R#\u0010B\u001a\n =*\u0004\u0018\u00010<0<8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\b>\u0010?\u001a\u0004\b@\u0010AR\u0018\u0010F\u001a\u0004\u0018\u00010C8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bD\u0010ER\u0018\u0010J\u001a\u0004\u0018\u00010G8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bH\u0010IR\u0016\u0010M\u001a\u00020\u00148\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bK\u0010LR\u001b\u0010R\u001a\u00020N8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bO\u0010?\u001a\u0004\bP\u0010QR#\u0010W\u001a\n =*\u0004\u0018\u00010S0S8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bT\u0010?\u001a\u0004\bU\u0010VR\u001b\u0010\\\u001a\u00020X8BX\u0082\u0084\u0002¢\u0006\f\n\u0004\bY\u0010?\u001a\u0004\bZ\u0010[R\u0018\u0010`\u001a\u0004\u0018\u00010]8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\b^\u0010_R\u0014\u0010d\u001a\u00020a8\u0002X\u0082\u0004¢\u0006\u0006\n\u0004\bb\u0010cR\u0018\u0010h\u001a\u0004\u0018\u00010e8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bf\u0010gR\u0016\u0010j\u001a\u00020\u000b8\u0002@\u0002X\u0082\u000e¢\u0006\u0006\n\u0004\bi\u0010(¨\u0006l"}, d2 = {"Lcom/miui/guardprovider/aiguard/AiGuardService;", "Landroid/app/Service;", "<init>", "()V", "Landroid/content/Intent;", "intent", "Landroid/os/IBinder;", "onBind", "(Landroid/content/Intent;)Landroid/os/IBinder;", "Lf2/D;", "onCreate", com.xiaomi.onetrack.util.a.f14247c, "flags", "startId", "onStartCommand", "(Landroid/content/Intent;II)I", "onDestroy", "K", com.xiaomi.onetrack.util.a.f14247c, "conversation", com.xiaomi.onetrack.util.a.f14247c, ai.f13676a, "(Ljava/lang/String;Lj2/e;)Ljava/lang/Object;", "Ljava/nio/file/Path;", "clip", "C", "(Ljava/nio/file/Path;Lj2/e;)Ljava/lang/Object;", "switchStatusFromSecurityCenter", "z", "(ZLj2/e;)Ljava/lang/Object;", "qqVideoDetectionTimes", "qqVoiceDetectionTimes", "wechatVideoDetectionTimes", "wechatVoiceDetectionTimes", "phoneVoiceDetectionTimes", "R", "(IIIII)V", "scene", "P", "(ILj2/e;)Ljava/lang/Object;", "I", "(I)I", "LY3/e;", "Q", "()LY3/e;", "J", "Landroid/app/Notification;", "y", "()Landroid/app/Notification;", "L", "(I)V", com.xiaomi.onetrack.util.a.f14247c, "Landroid/media/AudioRecordingConfiguration;", "w", "cause", "u", "LV3/M;", "b", "LV3/M;", "scope", "Lcom/miui/guardprovider/aiguard/AiGuardService$a;", "kotlin.jvm.PlatformType", "c", "Lf2/j;", "G", "()Lcom/miui/guardprovider/aiguard/AiGuardService$a;", "deps", "Lo1/b;", "d", "Lo1/b;", "asr", "Li1/n;", "e", "Li1/n;", "callSessionContext", "f", "Z", "alerted", "Lcom/miui/guardprovider/aiguard/i;", "g", ai.f13677b, "()Lcom/miui/guardprovider/aiguard/i;", "feedbackInfoCollector", "Landroid/media/AudioManager;", "h", "E", "()Landroid/media/AudioManager;", "audioManager", "Lcom/miui/guardprovider/aiguard/f;", "i", "F", "()Lcom/miui/guardprovider/aiguard/f;", "configs", "Lcom/miui/securitycenter/confidentialcomputing/c;", "j", "Lcom/miui/securitycenter/confidentialcomputing/c;", "ccl", "com/miui/guardprovider/aiguard/AiGuardService$c", "k", "Lcom/miui/guardprovider/aiguard/AiGuardService$c;", "counter", "LV3/y0;", com.xiaomi.onetrack.b.e.f13883a, "LV3/y0;", "timedShutdownJob", "m", "capabilities", "a", "ai-guard_release"}, k = 1, mv = {2, 2, 0}, xi = SmsCheckerContentTypes.TYPE_TEL_NET_TEL)
public final class AiGuardService extends Service {

    /* JADX INFO: renamed from: d, reason: collision with root package name and from kotlin metadata */
    private p098o1.b asr;

    /* JADX INFO: renamed from: e, reason: collision with root package name and from kotlin metadata */
    private p058i1.n callSessionContext;

    /* JADX INFO: renamed from: f, reason: collision with root package name and from kotlin metadata */
    private boolean alerted;

    /* JADX INFO: renamed from: j, reason: collision with root package name and from kotlin metadata */
    private com.miui.securitycenter.confidentialcomputing.c ccl;

    /* JADX INFO: renamed from: l, reason: collision with root package name and from kotlin metadata */
    private InterfaceC0684y0 timedShutdownJob;

    /* JADX INFO: renamed from: m, reason: collision with root package name and from kotlin metadata */
    private int capabilities;

    /* JADX INFO: renamed from: b, reason: collision with root package name and from kotlin metadata */
    private final M scope = N.a(C0641c0.a().b0(R0.b(null, 1, null)));

    /* JADX INFO: renamed from: c, reason: collision with root package name and from kotlin metadata */
    private final p038f2.j deps = p038f2.k.b(new p129t2.a() { // from class: i1.a
        @Override // p129t2.a
        public final Object c() {
            return AiGuardService.A();
        }
    });

    /* JADX INFO: renamed from: g, reason: collision with root package name and from kotlin metadata */
    private final p038f2.j feedbackInfoCollector = p038f2.k.b(new p129t2.a() { // from class: i1.b
        @Override // p129t2.a
        public final Object c() {
            return AiGuardService.D(this.f15807b);
        }
    });

    /* JADX INFO: renamed from: h, reason: collision with root package name and from kotlin metadata */
    private final p038f2.j audioManager = p038f2.k.b(new p129t2.a() { // from class: i1.c
        @Override // p129t2.a
        public final Object c() {
            return AiGuardService.v(this.f15808b);
        }
    });

    /* JADX INFO: renamed from: i, reason: collision with root package name and from kotlin metadata */
    private final p038f2.j configs = p038f2.k.b(new p129t2.a() { // from class: i1.d
        @Override // p129t2.a
        public final Object c() {
            return AiGuardService.x(this.f15809b);
        }
    });

    /* JADX INFO: renamed from: k, reason: collision with root package name and from kotlin metadata */
    private final c counter = new c();

    public interface a {
        p086m1.i a();

        p086m1.g b();
    }

    static final class b extends p080l2.l implements p129t2.p {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        Object f12435f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        int f12436g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private /* synthetic */ Object f12437h;

        public static final class a extends AudioManager.AudioRecordingCallback {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ t f12439a;

            a(t tVar) {
                this.f12439a = tVar;
            }

            @Override // android.media.AudioManager.AudioRecordingCallback
            public void onRecordingConfigChanged(List list) {
                t tVar = this.f12439a;
                if (list == null) {
                    return;
                }
                tVar.m(list);
            }
        }

        b(p066j2.e eVar) {
            super(2, eVar);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final D F(AiGuardService aiGuardService, a aVar) {
            aiGuardService.E().unregisterAudioRecordingCallback(aVar);
            return D.f14789a;
        }

        @Override // p080l2.a
        public final Object A(Object obj) {
            t tVar = (t) this.f12437h;
            Object objE = p073k2.b.e();
            int i6 = this.f12436g;
            if (i6 == 0) {
                s.b(obj);
                final a aVar = new a(tVar);
                AiGuardService.this.E().registerAudioRecordingCallback(aVar, null);
                final AiGuardService aiGuardService = AiGuardService.this;
                p129t2.a aVar2 = new p129t2.a() { // from class: com.miui.guardprovider.aiguard.a
                    @Override // p129t2.a
                    public final Object c() {
                        return AiGuardService.b.F(aiGuardService, aVar);
                    }
                };
                this.f12437h = p080l2.k.a(tVar);
                this.f12435f = p080l2.k.a(aVar);
                this.f12436g = 1;
                if (r.a(tVar, aVar2, this) == objE) {
                    return objE;
                }
            } else {
                if (i6 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                s.b(obj);
            }
            return D.f14789a;
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: E, reason: merged with bridge method [inline-methods] */
        public final Object t(t tVar, p066j2.e eVar) {
            return ((b) f(tVar, eVar)).A(D.f14789a);
        }

        @Override // p080l2.a
        public final p066j2.e f(Object obj, p066j2.e eVar) {
            b bVar = AiGuardService.this.new b(eVar);
            bVar.f12437h = obj;
            return bVar;
        }
    }

    public static final class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private int f12440a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f12441b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private int f12442c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private int f12443d;

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        private int f12444e;

        c() {
        }

        public final int a() {
            return this.f12444e;
        }

        public final int b() {
            return this.f12440a;
        }

        public final int c() {
            return this.f12441b;
        }

        public final int d() {
            return this.f12442c;
        }

        public final int e() {
            return this.f12443d;
        }

        public final void f(int i6) {
            this.f12444e = i6;
        }

        public final void g(int i6) {
            this.f12440a = i6;
        }

        public final void h(int i6) {
            this.f12441b = i6;
        }

        public final void i(int i6) {
            this.f12442c = i6;
        }

        public final void j(int i6) {
            this.f12443d = i6;
        }
    }

    static final class d extends p080l2.l implements p129t2.p {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        int f12445f;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ boolean f12447h;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        d(boolean z6, p066j2.e eVar) {
            super(2, eVar);
            this.f12447h = z6;
        }

        @Override // p080l2.a
        public final Object A(Object obj) {
            p073k2.b.e();
            if (this.f12445f != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            s.b(obj);
            Log.i("AiGuardService", "Reporting daily statistics...");
            p065j1.a.C0238a c0238a = p065j1.a.f16479a;
            Context applicationContext = AiGuardService.this.getApplicationContext();
            p135u2.m.d(applicationContext, "getApplicationContext(...)");
            c0238a.a(applicationContext).a("daily_report", w.a(ah.ab, "1127.0.0.0.37700"), w.a("enable_status", p080l2.b.a(this.f12447h)));
            SharedPreferences sharedPreferences = AiGuardService.this.getSharedPreferences("ai_guard_daily_report", 0);
            List listC = AbstractC0962q.c();
            listC.add(L.k(w.a("trigger_scenario", "电话"), w.a("detect_number", p080l2.b.b(sharedPreferences.getInt("phoneVoiceDetectionTimes", 0)))));
            listC.add(L.k(w.a("trigger_scenario", "QQ语音通话"), w.a("detect_number", p080l2.b.b(sharedPreferences.getInt("qqVoiceDetectionTimes", 0)))));
            listC.add(L.k(w.a("trigger_scenario", "QQ视频通话"), w.a("detect_number", p080l2.b.b(sharedPreferences.getInt("qqVideoDetectionTimes", 0)))));
            listC.add(L.k(w.a("trigger_scenario", "微信语音通话"), w.a("detect_number", p080l2.b.b(sharedPreferences.getInt("wechatVoiceDetectionTimes", 0)))));
            listC.add(L.k(w.a("trigger_scenario", "微信视频通话"), w.a("detect_number", p080l2.b.b(sharedPreferences.getInt("wechatVideoDetectionTimes", 0)))));
            List listA = AbstractC0962q.a(listC);
            Context applicationContext2 = AiGuardService.this.getApplicationContext();
            p135u2.m.d(applicationContext2, "getApplicationContext(...)");
            c0238a.a(applicationContext2).a("daily_report", w.a(ah.ab, "1127.0.0.0.37704"), w.a("detect_status", listA));
            p135u2.m.b(sharedPreferences);
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.clear();
            editorEdit.apply();
            return D.f14789a;
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
        public final Object t(M m6, p066j2.e eVar) {
            return ((d) f(m6, eVar)).A(D.f14789a);
        }

        @Override // p080l2.a
        public final p066j2.e f(Object obj, p066j2.e eVar) {
            return AiGuardService.this.new d(this.f12447h, eVar);
        }
    }

    static final class e extends p080l2.d {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        Object f12448e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        Object f12449f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        Object f12450g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        Object f12451h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        Object f12452i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        int f12453j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        /* synthetic */ Object f12454k;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        int f12456m;

        e(p066j2.e eVar) {
            super(eVar);
        }

        @Override // p080l2.a
        public final Object A(Object obj) {
            this.f12454k = obj;
            this.f12456m |= Integer.MIN_VALUE;
            return AiGuardService.this.B(null, this);
        }
    }

    static final /* synthetic */ class f extends p135u2.k implements p129t2.p {
        f(Object obj) {
            super(2, obj, p086m1.g.class, "detectV2", "detectV2(Lcom/miui/securitycenter/confidentialcomputing/EncryptedData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", 0);
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: x, reason: merged with bridge method [inline-methods] */
        public final Object t(EncryptedData encryptedData, p066j2.e eVar) {
            return ((p086m1.g) this.f24080c).a(encryptedData, eVar);
        }
    }

    static final class g extends p080l2.d {

        /* JADX INFO: renamed from: e, reason: collision with root package name */
        Object f12457e;

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        Object f12458f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        Object f12459g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        Object f12460h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        Object f12461i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        int f12462j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        /* synthetic */ Object f12463k;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        int f12465m;

        g(p066j2.e eVar) {
            super(eVar);
        }

        @Override // p080l2.a
        public final Object A(Object obj) {
            this.f12463k = obj;
            this.f12465m |= Integer.MIN_VALUE;
            return AiGuardService.this.C(null, this);
        }
    }

    static final /* synthetic */ class h extends p135u2.k implements p129t2.p {
        h(Object obj) {
            super(2, obj, p086m1.i.class, "detectV2", "detectV2(Lcom/miui/securitycenter/confidentialcomputing/EncryptedData;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", 0);
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: x, reason: merged with bridge method [inline-methods] */
        public final Object t(EncryptedData encryptedData, p066j2.e eVar) {
            return ((p086m1.i) this.f24080c).a(encryptedData, eVar);
        }
    }

    static final class i extends p080l2.l implements p129t2.p {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        int f12466f;

        static final class a extends p080l2.l implements p129t2.p {

            /* JADX INFO: renamed from: f, reason: collision with root package name */
            int f12468f;

            /* JADX INFO: renamed from: g, reason: collision with root package name */
            final /* synthetic */ AiGuardService f12469g;

            /* JADX INFO: renamed from: com.miui.guardprovider.aiguard.AiGuardService$i$a$a, reason: collision with other inner class name */
            static final class C0185a implements InterfaceC0715f {

                /* JADX INFO: renamed from: b, reason: collision with root package name */
                final /* synthetic */ AiGuardService f12470b;

                C0185a(AiGuardService aiGuardService) {
                    this.f12470b = aiGuardService;
                }

                public final Object a(int i6, p066j2.e eVar) {
                    Log.i("AiGuardService", "ThermalState: " + i6);
                    if (i6 >= 3) {
                        Log.i("AiGuardService", "Stop service caused by thermal");
                        this.f12470b.stopSelf();
                    }
                    return D.f14789a;
                }

                @Override // Y3.InterfaceC0715f
                public /* bridge */ /* synthetic */ Object b(Object obj, p066j2.e eVar) {
                    return a(((Number) obj).intValue(), eVar);
                }
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(AiGuardService aiGuardService, p066j2.e eVar) {
                super(2, eVar);
                this.f12469g = aiGuardService;
            }

            @Override // p080l2.a
            public final Object A(Object obj) {
                Object objE = p073k2.b.e();
                int i6 = this.f12468f;
                if (i6 == 0) {
                    s.b(obj);
                    InterfaceC0714e interfaceC0714eQ = this.f12469g.Q();
                    C0185a c0185a = new C0185a(this.f12469g);
                    this.f12468f = 1;
                    if (interfaceC0714eQ.a(c0185a, this) == objE) {
                        return objE;
                    }
                } else {
                    if (i6 != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    s.b(obj);
                }
                return D.f14789a;
            }

            @Override // p129t2.p
            /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
            public final Object t(M m6, p066j2.e eVar) {
                return ((a) f(m6, eVar)).A(D.f14789a);
            }

            @Override // p080l2.a
            public final p066j2.e f(Object obj, p066j2.e eVar) {
                return new a(this.f12469g, eVar);
            }
        }

        i(p066j2.e eVar) {
            super(2, eVar);
        }

        @Override // p080l2.a
        public final Object A(Object obj) {
            Object objE = p073k2.b.e();
            int i6 = this.f12466f;
            if (i6 == 0) {
                s.b(obj);
                H0 h0C = C0641c0.c();
                a aVar = new a(AiGuardService.this, null);
                this.f12466f = 1;
                if (AbstractC0648g.e(h0C, aVar, this) == objE) {
                    return objE;
                }
            } else {
                if (i6 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                s.b(obj);
            }
            return D.f14789a;
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
        public final Object t(M m6, p066j2.e eVar) {
            return ((i) f(m6, eVar)).A(D.f14789a);
        }

        @Override // p080l2.a
        public final p066j2.e f(Object obj, p066j2.e eVar) {
            return AiGuardService.this.new i(eVar);
        }
    }

    static final class j extends p080l2.l implements p129t2.p {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        int f12471f;

        j(p066j2.e eVar) {
            super(2, eVar);
        }

        @Override // p080l2.a
        public final Object A(Object obj) {
            Object objE = p073k2.b.e();
            int i6 = this.f12471f;
            if (i6 == 0) {
                s.b(obj);
                U3.a.C0082a c0082a = U3.a.f6193b;
                long jO = U3.c.o(AiGuardService.this.F().f12530d, U3.d.MINUTES);
                this.f12471f = 1;
                if (W.b(jO, this) == objE) {
                    return objE;
                }
            } else {
                if (i6 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                s.b(obj);
            }
            Log.i("AiGuardService", "Shutdown because of timeout.");
            AiGuardService.this.stopSelf();
            return D.f14789a;
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
        public final Object t(M m6, p066j2.e eVar) {
            return ((j) f(m6, eVar)).A(D.f14789a);
        }

        @Override // p080l2.a
        public final p066j2.e f(Object obj, p066j2.e eVar) {
            return AiGuardService.this.new j(eVar);
        }
    }

    static final class k extends p080l2.l implements p129t2.p {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        int f12473f;

        k(p066j2.e eVar) {
            super(2, eVar);
        }

        @Override // p080l2.a
        public final Object A(Object obj) {
            p073k2.b.e();
            if (this.f12473f != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            s.b(obj);
            if (!AiGuardService.this.alerted) {
                try {
                    AiGuardService.this.H().f();
                } catch (IOException unused) {
                }
            }
            p098o1.b bVar = AiGuardService.this.asr;
            if (bVar != null) {
                bVar.i();
            }
            p098o1.b bVar2 = AiGuardService.this.asr;
            if (bVar2 != null) {
                bVar2.g();
            }
            AiGuardService.this.asr = null;
            return D.f14789a;
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
        public final Object t(M m6, p066j2.e eVar) {
            return ((k) f(m6, eVar)).A(D.f14789a);
        }

        @Override // p080l2.a
        public final p066j2.e f(Object obj, p066j2.e eVar) {
            return AiGuardService.this.new k(eVar);
        }
    }

    static final class l extends p080l2.l implements p129t2.p {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        Object f12475f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        int f12476g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        int f12477h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        private /* synthetic */ Object f12478i;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        final /* synthetic */ boolean f12480k;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        l(boolean z6, p066j2.e eVar) {
            super(2, eVar);
            this.f12480k = z6;
        }

        @Override // p080l2.a
        public final Object A(Object obj) {
            M m6 = (M) this.f12478i;
            Object objE = p073k2.b.e();
            int i6 = this.f12477h;
            try {
                if (i6 == 0) {
                    s.b(obj);
                    AiGuardService aiGuardService = AiGuardService.this;
                    boolean z6 = this.f12480k;
                    f2.r.a aVar = p038f2.r.f14813c;
                    this.f12478i = p080l2.k.a(m6);
                    this.f12475f = p080l2.k.a(m6);
                    this.f12476g = 0;
                    this.f12477h = 1;
                    if (aiGuardService.z(z6, this) == objE) {
                        return objE;
                    }
                } else {
                    if (i6 != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    s.b(obj);
                }
                p038f2.r.b(D.f14789a);
            } catch (Throwable th) {
                f2.r.a aVar2 = p038f2.r.f14813c;
                p038f2.r.b(s.a(th));
            }
            AiGuardService.this.stopSelf();
            return D.f14789a;
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
        public final Object t(M m6, p066j2.e eVar) {
            return ((l) f(m6, eVar)).A(D.f14789a);
        }

        @Override // p080l2.a
        public final p066j2.e f(Object obj, p066j2.e eVar) {
            l lVar = AiGuardService.this.new l(this.f12480k, eVar);
            lVar.f12478i = obj;
            return lVar;
        }
    }

    static final class m extends p080l2.l implements p129t2.p {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        int f12481f;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ int f12483h;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        m(int i6, p066j2.e eVar) {
            super(2, eVar);
            this.f12483h = i6;
        }

        @Override // p080l2.a
        public final Object A(Object obj) {
            Object objE = p073k2.b.e();
            int i6 = this.f12481f;
            if (i6 == 0) {
                s.b(obj);
                AiGuardService aiGuardService = AiGuardService.this;
                int i7 = this.f12483h;
                this.f12481f = 1;
                if (aiGuardService.P(i7, this) == objE) {
                    return objE;
                }
            } else {
                if (i6 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                s.b(obj);
            }
            return D.f14789a;
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
        public final Object t(M m6, p066j2.e eVar) {
            return ((m) f(m6, eVar)).A(D.f14789a);
        }

        @Override // p080l2.a
        public final p066j2.e f(Object obj, p066j2.e eVar) {
            return AiGuardService.this.new m(this.f12483h, eVar);
        }
    }

    static final class n extends p080l2.l implements p129t2.p {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        int f12484f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        int f12485g;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        final /* synthetic */ p098o1.b f12487i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        final /* synthetic */ p058i1.n f12488j;

        static final class a implements InterfaceC0715f {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ p098o1.b f12489b;

            a(p098o1.b bVar) {
                this.f12489b = bVar;
            }

            @Override // Y3.InterfaceC0715f
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public final Object b(List list, p066j2.e eVar) {
                int i6;
                List list2 = list;
                if ((list2 instanceof Collection) && list2.isEmpty()) {
                    i6 = 0;
                } else {
                    Iterator it = list2.iterator();
                    i6 = 0;
                    while (it.hasNext()) {
                        if (((AudioRecordingConfiguration) it.next()).getClientAudioSource() == 4 && (i6 = i6 + 1) < 0) {
                            AbstractC0962q.t();
                        }
                    }
                }
                if (i6 > 1) {
                    this.f12489b.i();
                    Log.w("AiGuardService", "Audio recording stopped because of collision.");
                }
                return D.f14789a;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        n(p098o1.b bVar, p058i1.n nVar, p066j2.e eVar) {
            super(2, eVar);
            this.f12487i = bVar;
            this.f12488j = nVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final D G(p058i1.n nVar, AiGuardService aiGuardService, String str) {
            nVar.a(str);
            aiGuardService.H().c(str);
            return D.f14789a;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void H(AiGuardService aiGuardService, byte[] bArr) throws IOException {
            com.miui.guardprovider.aiguard.i iVarH = aiGuardService.H();
            p135u2.m.b(bArr);
            iVarH.d(bArr);
        }

        /* JADX WARN: Code restructure failed: missing block: B:30:0x00a5, code lost:
        
            if (r7.a(r1, r6) == r0) goto L31;
         */
        @Override // p080l2.a
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object A(Object obj) {
            Object objE = p073k2.b.e();
            int i6 = this.f12485g;
            if (i6 == 0) {
                s.b(obj);
                this.f12485g = 1;
                if (W.a(300L, this) != objE) {
                }
                return objE;
            }
            if (i6 == 1) {
                s.b(obj);
            } else {
                if (i6 != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                s.b(obj);
            }
            return D.f14789a;
            List<AudioRecordingConfiguration> activeRecordingConfigurations = AiGuardService.this.E().getActiveRecordingConfigurations();
            p135u2.m.d(activeRecordingConfigurations, "getActiveRecordingConfigurations(...)");
            List<AudioRecordingConfiguration> list = activeRecordingConfigurations;
            int i7 = 0;
            if (!(list instanceof Collection) || !list.isEmpty()) {
                Iterator<T> it = list.iterator();
                while (it.hasNext()) {
                    if (((AudioRecordingConfiguration) it.next()).getClientAudioSource() == 4 && (i7 = i7 + 1) < 0) {
                        AbstractC0962q.t();
                    }
                }
            }
            if (i7 == 0) {
                p098o1.b bVar = this.f12487i;
                final p058i1.n nVar = this.f12488j;
                final AiGuardService aiGuardService = AiGuardService.this;
                p129t2.l lVar = new p129t2.l() { // from class: com.miui.guardprovider.aiguard.b
                    @Override // p129t2.l
                    public final Object m(Object obj2) {
                        return AiGuardService.n.G(nVar, aiGuardService, (String) obj2);
                    }
                };
                final AiGuardService aiGuardService2 = AiGuardService.this;
                bVar.h(lVar, new Consumer() { // from class: com.miui.guardprovider.aiguard.c
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj2) throws IOException {
                        AiGuardService.n.H(aiGuardService2, (byte[]) obj2);
                    }
                });
            } else {
                p080l2.b.b(Log.w("AiGuardService", "Audio recording can't start because there is another app using VOICE_CALL right now. "));
            }
            InterfaceC0714e interfaceC0714eW = AiGuardService.this.w();
            a aVar = new a(this.f12487i);
            this.f12484f = i7;
            this.f12485g = 2;
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: F, reason: merged with bridge method [inline-methods] */
        public final Object t(M m6, p066j2.e eVar) {
            return ((n) f(m6, eVar)).A(D.f14789a);
        }

        @Override // p080l2.a
        public final p066j2.e f(Object obj, p066j2.e eVar) {
            return AiGuardService.this.new n(this.f12487i, this.f12488j, eVar);
        }
    }

    static final class o extends p080l2.l implements p129t2.p {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        int f12490f;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        final /* synthetic */ String f12492h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        final /* synthetic */ int f12493i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        o(String str, int i6, p066j2.e eVar) {
            super(2, eVar);
            this.f12492h = str;
            this.f12493i = i6;
        }

        @Override // p080l2.a
        public final Object A(Object obj) {
            boolean zBooleanValue;
            int i6;
            Object objE = p073k2.b.e();
            int i7 = this.f12490f;
            try {
                if (i7 == 0) {
                    s.b(obj);
                    AiGuardService aiGuardService = AiGuardService.this;
                    String str = this.f12492h;
                    this.f12490f = 1;
                    obj = aiGuardService.B(str, this);
                    if (obj == objE) {
                        return objE;
                    }
                } else {
                    if (i7 != 1) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    s.b(obj);
                }
                zBooleanValue = ((Boolean) obj).booleanValue();
            } catch (Exception e6) {
                Log.e("AiGuardService", "Something wrong occurs", e6);
                zBooleanValue = false;
            }
            Log.i("AiGuardService", "Phone scam detection result:" + zBooleanValue);
            int i8 = this.f12493i;
            if (i8 == 2) {
                c cVar = AiGuardService.this.counter;
                cVar.f(cVar.a() + 1);
                i6 = 1073741824;
            } else if (i8 == 3) {
                c cVar2 = AiGuardService.this.counter;
                cVar2.h(cVar2.c() + 1);
                i6 = 536870913;
            } else {
                if (i8 != 4) {
                    return D.f14789a;
                }
                c cVar3 = AiGuardService.this.counter;
                cVar3.j(cVar3.e() + 1);
                i6 = 536870914;
            }
            if (zBooleanValue) {
                AiGuardService.this.u(i6);
                AiGuardService.this.stopSelf();
            }
            return D.f14789a;
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
        public final Object t(M m6, p066j2.e eVar) {
            return ((o) f(m6, eVar)).A(D.f14789a);
        }

        @Override // p080l2.a
        public final p066j2.e f(Object obj, p066j2.e eVar) {
            return AiGuardService.this.new o(this.f12492h, this.f12493i, eVar);
        }
    }

    static final class p extends p080l2.l implements p129t2.p {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        Object f12494f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        Object f12495g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        boolean f12496h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        int f12497i;

        /* JADX INFO: renamed from: j, reason: collision with root package name */
        int f12498j;

        /* JADX INFO: renamed from: k, reason: collision with root package name */
        private /* synthetic */ Object f12499k;

        /* JADX INFO: renamed from: m, reason: collision with root package name */
        final /* synthetic */ int f12501m;

        static final class a extends p080l2.l implements p129t2.p {

            /* JADX INFO: renamed from: f, reason: collision with root package name */
            int f12502f;

            /* JADX INFO: renamed from: g, reason: collision with root package name */
            final /* synthetic */ AiGuardService f12503g;

            /* JADX INFO: renamed from: h, reason: collision with root package name */
            final /* synthetic */ Path f12504h;

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            a(AiGuardService aiGuardService, Path path, p066j2.e eVar) {
                super(2, eVar);
                this.f12503g = aiGuardService;
                this.f12504h = path;
            }

            @Override // p080l2.a
            public final Object A(Object obj) throws IOException {
                boolean zBooleanValue;
                Object objE = p073k2.b.e();
                int i6 = this.f12502f;
                try {
                    try {
                        if (i6 == 0) {
                            s.b(obj);
                            AiGuardService aiGuardService = this.f12503g;
                            Path path = this.f12504h;
                            this.f12502f = 1;
                            obj = aiGuardService.C(path, this);
                            if (obj == objE) {
                                return objE;
                            }
                        } else {
                            if (i6 != 1) {
                                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                            }
                            s.b(obj);
                        }
                        AiGuardService aiGuardService2 = this.f12503g;
                        Path path2 = this.f12504h;
                        if (((Boolean) obj).booleanValue()) {
                            aiGuardService2.H().e(path2);
                        }
                        zBooleanValue = ((Boolean) obj).booleanValue();
                        Files.delete(this.f12504h);
                    } catch (IOException e6) {
                        Log.e("AiGuardService", "Request failed: ", e6);
                        Files.delete(this.f12504h);
                        zBooleanValue = false;
                    }
                    return p080l2.b.a(zBooleanValue);
                } catch (Throwable th) {
                    Files.delete(this.f12504h);
                    throw th;
                }
            }

            @Override // p129t2.p
            /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
            public final Object t(M m6, p066j2.e eVar) {
                return ((a) f(m6, eVar)).A(D.f14789a);
            }

            @Override // p080l2.a
            public final p066j2.e f(Object obj, p066j2.e eVar) {
                return new a(this.f12503g, this.f12504h, eVar);
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        p(int i6, p066j2.e eVar) {
            super(2, eVar);
            this.f12501m = i6;
        }

        /* JADX WARN: Code duplicated, block: B:30:0x006f A[Catch: all -> 0x0024, Exception -> 0x0027, CancellationException -> 0x0114, TryCatch #1 {all -> 0x0024, blocks: (B:8:0x0020, B:28:0x0069, B:30:0x006f, B:33:0x008a, B:36:0x0091, B:40:0x00b2, B:42:0x00d6, B:43:0x00e1, B:46:0x0100, B:49:0x0106, B:51:0x0114, B:25:0x0051), top: B:57:0x0010 }] */
        /* JADX WARN: Code duplicated, block: B:32:0x0088  */
        /* JADX WARN: Code duplicated, block: B:33:0x008a A[Catch: all -> 0x0024, Exception -> 0x0027, CancellationException -> 0x0114, PHI: r2
  0x008a: PHI (r2v12 l1.a) = (r2v29 l1.a), (r2v30 l1.a) binds: [B:31:0x0086, B:25:0x0051] A[DONT_GENERATE, DONT_INLINE], TryCatch #1 {all -> 0x0024, blocks: (B:8:0x0020, B:28:0x0069, B:30:0x006f, B:33:0x008a, B:36:0x0091, B:40:0x00b2, B:42:0x00d6, B:43:0x00e1, B:46:0x0100, B:49:0x0106, B:51:0x0114, B:25:0x0051), top: B:57:0x0010 }] */
        /* JADX WARN: Code duplicated, block: B:35:0x0090  */
        /* JADX WARN: Code duplicated, block: B:42:0x00d6 A[Catch: all -> 0x0024, Exception -> 0x0027, CancellationException -> 0x0114, TryCatch #1 {all -> 0x0024, blocks: (B:8:0x0020, B:28:0x0069, B:30:0x006f, B:33:0x008a, B:36:0x0091, B:40:0x00b2, B:42:0x00d6, B:43:0x00e1, B:46:0x0100, B:49:0x0106, B:51:0x0114, B:25:0x0051), top: B:57:0x0010 }] */
        /* JADX WARN: Code duplicated, block: B:43:0x00e1 A[Catch: all -> 0x0024, Exception -> 0x0027, CancellationException -> 0x0114, TryCatch #1 {all -> 0x0024, blocks: (B:8:0x0020, B:28:0x0069, B:30:0x006f, B:33:0x008a, B:36:0x0091, B:40:0x00b2, B:42:0x00d6, B:43:0x00e1, B:46:0x0100, B:49:0x0106, B:51:0x0114, B:25:0x0051), top: B:57:0x0010 }] */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r2v0, types: [int] */
        /* JADX WARN: Type inference failed for: r2v1 */
        /* JADX WARN: Type inference failed for: r2v10, types: [l1.a] */
        /* JADX WARN: Type inference failed for: r2v18 */
        /* JADX WARN: Type inference failed for: r2v19 */
        /* JADX WARN: Type inference failed for: r2v2 */
        /* JADX WARN: Type inference failed for: r2v20 */
        /* JADX WARN: Type inference failed for: r2v3, types: [l1.a] */
        /* JADX WARN: Type inference failed for: r2v32 */
        /* JADX WARN: Type inference failed for: r2v33 */
        /* JADX WARN: Type inference failed for: r2v34 */
        /* JADX WARN: Type inference failed for: r2v35 */
        /* JADX WARN: Type inference failed for: r2v4 */
        /* JADX WARN: Type inference failed for: r2v5, types: [l1.a] */
        /* JADX WARN: Type inference failed for: r2v6 */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:35:0x0090 -> B:28:0x0069). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:44:0x00fd -> B:28:0x0069). Please report as a decompilation issue!!! */
        /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
            jadx.core.utils.exceptions.JadxOverflowException: Regions stack size limit reached
            	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:59)
            	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
            	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:19)
            */
        @Override // p080l2.a
        public final java.lang.Object A(java.lang.Object r14) {
            /*
                Method dump skipped, instruction units count: 284
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.guardprovider.aiguard.AiGuardService.p.A(java.lang.Object):java.lang.Object");
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: D, reason: merged with bridge method [inline-methods] */
        public final Object t(M m6, p066j2.e eVar) {
            return ((p) f(m6, eVar)).A(D.f14789a);
        }

        @Override // p080l2.a
        public final p066j2.e f(Object obj, p066j2.e eVar) {
            p pVar = AiGuardService.this.new p(this.f12501m, eVar);
            pVar.f12499k = obj;
            return pVar;
        }
    }

    static final class q extends p080l2.l implements p129t2.p {

        /* JADX INFO: renamed from: f, reason: collision with root package name */
        Object f12505f;

        /* JADX INFO: renamed from: g, reason: collision with root package name */
        int f12506g;

        /* JADX INFO: renamed from: h, reason: collision with root package name */
        private /* synthetic */ Object f12507h;

        /* JADX INFO: renamed from: i, reason: collision with root package name */
        final /* synthetic */ PowerManager f12508i;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        q(PowerManager powerManager, p066j2.e eVar) {
            super(2, eVar);
            this.f12508i = powerManager;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void G(t tVar, int i6) {
            tVar.m(Integer.valueOf(i6));
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final D H(PowerManager powerManager, PowerManager.OnThermalStatusChangedListener onThermalStatusChangedListener) {
            powerManager.removeThermalStatusListener(onThermalStatusChangedListener);
            return D.f14789a;
        }

        @Override // p080l2.a
        public final Object A(Object obj) {
            final t tVar = (t) this.f12507h;
            Object objE = p073k2.b.e();
            int i6 = this.f12506g;
            if (i6 == 0) {
                s.b(obj);
                final PowerManager.OnThermalStatusChangedListener onThermalStatusChangedListener = new PowerManager.OnThermalStatusChangedListener() { // from class: com.miui.guardprovider.aiguard.d
                    @Override // android.os.PowerManager.OnThermalStatusChangedListener
                    public final void onThermalStatusChanged(int i7) {
                        AiGuardService.q.G(tVar, i7);
                    }
                };
                this.f12508i.addThermalStatusListener(onThermalStatusChangedListener);
                final PowerManager powerManager = this.f12508i;
                p129t2.a aVar = new p129t2.a() { // from class: com.miui.guardprovider.aiguard.e
                    @Override // p129t2.a
                    public final Object c() {
                        return AiGuardService.q.H(powerManager, onThermalStatusChangedListener);
                    }
                };
                this.f12507h = p080l2.k.a(tVar);
                this.f12505f = p080l2.k.a(onThermalStatusChangedListener);
                this.f12506g = 1;
                if (r.a(tVar, aVar, this) == objE) {
                    return objE;
                }
            } else {
                if (i6 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                s.b(obj);
            }
            return D.f14789a;
        }

        @Override // p129t2.p
        /* JADX INFO: renamed from: F, reason: merged with bridge method [inline-methods] */
        public final Object t(t tVar, p066j2.e eVar) {
            return ((q) f(tVar, eVar)).A(D.f14789a);
        }

        @Override // p080l2.a
        public final p066j2.e f(Object obj, p066j2.e eVar) {
            q qVar = new q(this.f12508i, eVar);
            qVar.f12507h = obj;
            return qVar;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final a A() {
        return com.miui.guardprovider.aiguard.g.a().a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final com.miui.guardprovider.aiguard.i D(AiGuardService aiGuardService) {
        return com.miui.guardprovider.aiguard.i.f12542d.b(aiGuardService);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final AudioManager E() {
        return (AudioManager) this.audioManager.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final com.miui.guardprovider.aiguard.f F() {
        return (com.miui.guardprovider.aiguard.f) this.configs.getValue();
    }

    private final a G() {
        return (a) this.deps.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final com.miui.guardprovider.aiguard.i H() {
        return (com.miui.guardprovider.aiguard.i) this.feedbackInfoCollector.getValue();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final int I(int scene) {
        if (scene == 1) {
            c cVar = this.counter;
            cVar.g(cVar.b() + 1);
            return 268435457;
        }
        if (scene == 2) {
            c cVar2 = this.counter;
            cVar2.i(cVar2.d() + 1);
            return 268435458;
        }
        Log.w("AiGuardService", "Video capture scene missing:" + scene);
        return 268435456;
    }

    private final void J() {
        AbstractC0652i.b(this.scope, null, null, new i(null), 3, null);
    }

    private final void L(final int scene) {
        final p058i1.n nVar = new p058i1.n(((long) F().f12529c) * 1000, new p129t2.l() { // from class: i1.e
            @Override // p129t2.l
            public final Object m(Object obj) {
                return AiGuardService.M(this.f15810b, scene, (String) obj);
            }
        });
        this.callSessionContext = nVar;
        p098o1.d dVarOf = p098o1.d.of(scene);
        p135u2.m.d(dVarOf, "of(...)");
        Context applicationContext = getApplicationContext();
        p135u2.m.d(applicationContext, "getApplicationContext(...)");
        p098o1.b bVar = new p098o1.b(applicationContext, dVarOf);
        this.asr = bVar;
        bVar.f();
        if (scene == 2) {
            AbstractC0652i.b(this.scope, null, null, new n(bVar, nVar, null), 3, null);
        } else {
            bVar.h(new p129t2.l() { // from class: i1.f
                @Override // p129t2.l
                public final Object m(Object obj) {
                    return AiGuardService.N(nVar, this, (String) obj);
                }
            }, new Consumer() { // from class: i1.g
                @Override // java.util.function.Consumer
                public final void accept(Object obj) throws IOException {
                    AiGuardService.O(this.f15814a, (byte[]) obj);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final D M(AiGuardService aiGuardService, int i6, String str) {
        p135u2.m.e(str, "conversation");
        Log.i("AiGuardService", "Phone scam detecting...");
        AbstractC0652i.b(aiGuardService.scope, null, null, aiGuardService.new o(str, i6, null), 3, null);
        return D.f14789a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final D N(p058i1.n nVar, AiGuardService aiGuardService, String str) {
        p135u2.m.e(str, "it");
        nVar.a(str);
        aiGuardService.H().c(str);
        return D.f14789a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void O(AiGuardService aiGuardService, byte[] bArr) throws IOException {
        p135u2.m.e(bArr, "it");
        aiGuardService.H().d(bArr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object P(int i6, p066j2.e eVar) {
        Object objE = AbstractC0648g.e(C0641c0.c(), new p(i6, null), eVar);
        return objE == p073k2.b.e() ? objE : D.f14789a;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final InterfaceC0714e Q() {
        return AbstractC0716g.d(new q((PowerManager) getSystemService(PowerManager.class), null));
    }

    private final void R(int qqVideoDetectionTimes, int qqVoiceDetectionTimes, int wechatVideoDetectionTimes, int wechatVoiceDetectionTimes, int phoneVoiceDetectionTimes) {
        SharedPreferences sharedPreferences = getSharedPreferences("ai_guard_daily_report", 0);
        p135u2.m.b(sharedPreferences);
        SharedPreferences.Editor editorEdit = sharedPreferences.edit();
        editorEdit.putInt("qqVideoDetectionTimes", sharedPreferences.getInt("qqVideoDetectionTimes", 0) + qqVideoDetectionTimes);
        editorEdit.putInt("qqVoiceDetectionTimes", sharedPreferences.getInt("qqVoiceDetectionTimes", 0) + qqVoiceDetectionTimes);
        editorEdit.putInt("wechatVideoDetectionTimes", sharedPreferences.getInt("wechatVideoDetectionTimes", 0) + wechatVideoDetectionTimes);
        editorEdit.putInt("wechatVoiceDetectionTimes", sharedPreferences.getInt("wechatVoiceDetectionTimes", 0) + wechatVoiceDetectionTimes);
        editorEdit.putInt("phoneVoiceDetectionTimes", sharedPreferences.getInt("phoneVoiceDetectionTimes", 0) + phoneVoiceDetectionTimes);
        editorEdit.apply();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void u(int cause) {
        startActivity(new Intent(getApplicationContext(), (Class<?>) AlertActivity.class).setFlags(268435456).putExtra("alert_cause", cause));
        this.alerted = true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final AudioManager v(AiGuardService aiGuardService) {
        return (AudioManager) aiGuardService.getSystemService(AudioManager.class);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final InterfaceC0714e w() {
        return AbstractC0716g.d(new b(null));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final com.miui.guardprovider.aiguard.f x(AiGuardService aiGuardService) {
        return new com.miui.guardprovider.aiguard.f(aiGuardService);
    }

    private final Notification y() {
        p156y.g gVarA = new y.g.c("AiGuardService", 3).c(getString(K.f15804z)).b(null).a();
        p135u2.m.d(gVarA, "build(...)");
        p156y.m mVarD = p156y.m.d(this);
        p135u2.m.d(mVarD, "from(...)");
        mVarD.c(gVarA);
        PendingIntent activity = PendingIntent.getActivity(this, 0, new Intent().setComponent(ComponentName.unflattenFromString("com.miui.securitycenter/com.miui.electricrisk.ElectricProtectPhoneActivity")).addFlags(268435456), 201326592);
        Bundle bundle = new Bundle();
        String string = getString(K.f15790l);
        p135u2.m.d(string, "getString(...)");
        String string2 = (this.capabilities & 2) == 0 ? getString(K.f15789k) : getString(K.f15788j);
        p135u2.m.b(string2);
        String strN = S3.o.n("\n            {\n              \"scene\": \"templateRevertProgressScene\",\n              \"param_v2\": {\n                \"protocol\": 1,\n                \"business\": \"AI Fraud Prevention\",\n                \"ticker\": \"" + string + "\", \n                \"tickerPic\": \"miui.focus.pic_landPic\",\n                \"tickerPicDark\": \"miui.focus.pic_landPic\",\n                \"updatable\": true,\n                \"showSmallIcon\": true,\n                \"picInfo\": {\n                  \"type\": 2,\n                  \"pic\": \"miui.focus.pic_landPic\"\n                },\n                \"baseInfo\": {\n                  \"title\": \"" + string + "\", \n                  \"content\": \"" + string2 + "\", \n                  \"type\": 2\n                },\n                \"aodPic\": \"miui.focus.pic_landPic\",\n                \"islandFirstFloat\": false,\n                \"param_island\": {\n                  \"islandProperty\": 1,\n                  \"islandPriority\": 1,\n                  \"islandOrder\": true,\n                  \"dismissIsland\": false,\n                  \"bigIslandArea\": {\n                    \"textInfo\": {\n                      \"title\": \"" + string + "\"\n                    },\n                    \"imageTextInfoLeft\": {\n                      \"type\": 1,\n                      \"picInfo\": {\n                        \"type\": 1,\n                        \"pic\": \"miui.focus.pic_landPic\"\n                      }\n                    }\n                  },\n                  \"smallIslandArea\": {\n                    \"picInfo\": {\n                      \"type\": 1,\n                      \"pic\": \"miui.focus.pic_landPic\"\n                    }\n                  }\n                }\n              }\n            }\n        ");
        Icon iconCreateWithResource = Icon.createWithResource(this, H.f15774a);
        p135u2.m.d(iconCreateWithResource, "createWithResource(...)");
        bundle.putParcelable("miui.focus.pics", G.c.a(w.a("miui.appIcon", iconCreateWithResource), w.a("miui.focus.pic_landPic", iconCreateWithResource)));
        bundle.putString("miui.focus.param", strN);
        Notification notificationB = new p156y.j(this, "AiGuardService").h(0).g(getString(K.f15790l)).f(string2).i(H.f15774a).a(bundle).e(activity).b();
        p135u2.m.d(notificationB, "build(...)");
        return notificationB;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final Object z(boolean z6, p066j2.e eVar) {
        Object objE = AbstractC0648g.e(C0641c0.b(), new d(z6, null), eVar);
        return objE == p073k2.b.e() ? objE : D.f14789a;
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0015  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r8v0, types: [com.miui.guardprovider.aiguard.AiGuardService] */
    /* JADX WARN: Type inference failed for: r8v1, types: [java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r8v13 */
    /* JADX WARN: Type inference failed for: r8v14 */
    /* JADX WARN: Type inference failed for: r8v5, types: [java.lang.AutoCloseable] */
    public final Object B(String str, p066j2.e eVar) throws Exception {
        e eVar2;
        if (eVar instanceof e) {
            eVar2 = (e) eVar;
            int i6 = eVar2.f12456m;
            if ((i6 & Integer.MIN_VALUE) != 0) {
                eVar2.f12456m = i6 - Integer.MIN_VALUE;
            } else {
                eVar2 = new e(eVar);
            }
        } else {
            eVar2 = new e(eVar);
        }
        Object objA = eVar2.f12454k;
        Object objE = p073k2.b.e();
        int i7 = eVar2.f12456m;
        try {
            if (i7 == 0) {
                s.b(objA);
                p086m1.g gVarB = G().b();
                com.miui.securitycenter.confidentialcomputing.c cVar = this.ccl;
                if (cVar == null) {
                    throw new IllegalStateException("Required value was null.");
                }
                com.miui.securitycenter.confidentialcomputing.c.b bVarA = cVar.a(false);
                JSONObject jSONObject = new JSONObject();
                String string = UUID.randomUUID().toString();
                p135u2.m.d(string, "toString(...)");
                AbstractC0985h.b(jSONObject, "requestId", string);
                AbstractC0985h.b(jSONObject, "text", str);
                String string2 = jSONObject.toString();
                p135u2.m.d(string2, "toString(...)");
                byte[] bytes = string2.getBytes(C0596d.f5679b);
                p135u2.m.d(bytes, "getBytes(...)");
                p135u2.m.b(bVarA);
                f fVar = new f(gVarB);
                eVar2.f12448e = p080l2.k.a(str);
                eVar2.f12449f = p080l2.k.a(gVarB);
                eVar2.f12450g = bVarA;
                eVar2.f12451h = p080l2.k.a(bVarA);
                eVar2.f12452i = p080l2.k.a(jSONObject);
                eVar2.f12453j = 0;
                eVar2.f12456m = 1;
                objA = p086m1.h.a(bytes, bVarA, fVar, eVar2);
                this = bVarA;
                if (objA == objE) {
                    return objE;
                }
            } else {
                if (i7 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                AutoCloseable autoCloseable = (AutoCloseable) eVar2.f12450g;
                s.b(objA);
                this = autoCloseable;
            }
            Boolean boolA = p080l2.b.a(((JSONObject) objA).getBoolean("checkRes"));
            p117r2.a.a(this, null);
            return boolA;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                p117r2.a.a(this, th);
                throw th2;
            }
        }
    }

    /* JADX WARN: Code duplicated, block: B:7:0x0015  */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0, types: [com.miui.guardprovider.aiguard.AiGuardService] */
    /* JADX WARN: Type inference failed for: r10v1, types: [java.lang.AutoCloseable] */
    /* JADX WARN: Type inference failed for: r10v13 */
    /* JADX WARN: Type inference failed for: r10v14 */
    /* JADX WARN: Type inference failed for: r10v5, types: [java.lang.AutoCloseable] */
    public final Object C(Path path, p066j2.e eVar) throws Exception {
        g gVar;
        if (eVar instanceof g) {
            gVar = (g) eVar;
            int i6 = gVar.f12465m;
            if ((i6 & Integer.MIN_VALUE) != 0) {
                gVar.f12465m = i6 - Integer.MIN_VALUE;
            } else {
                gVar = new g(eVar);
            }
        } else {
            gVar = new g(eVar);
        }
        Object objA = gVar.f12463k;
        Object objE = p073k2.b.e();
        int i7 = gVar.f12465m;
        try {
            if (i7 == 0) {
                s.b(objA);
                p086m1.i iVarA = G().a();
                com.miui.securitycenter.confidentialcomputing.c cVar = this.ccl;
                if (cVar == null) {
                    throw new IllegalStateException("Required value was null.");
                }
                com.miui.securitycenter.confidentialcomputing.c.b bVarA = cVar.a(false);
                JSONObject jSONObject = new JSONObject();
                AbstractC0985h.b(jSONObject, "streamName", "clip.mp4");
                Base64.Encoder encoder = Base64.getEncoder();
                byte[] allBytes = Files.readAllBytes(path);
                p135u2.m.d(allBytes, "readAllBytes(...)");
                String strEncodeToString = encoder.encodeToString(allBytes);
                p135u2.m.d(strEncodeToString, "encodeToString(...)");
                AbstractC0985h.b(jSONObject, "fileBase64", strEncodeToString);
                String string = UUID.randomUUID().toString();
                p135u2.m.d(string, "toString(...)");
                AbstractC0985h.b(jSONObject, "requestId", string);
                String string2 = jSONObject.toString();
                p135u2.m.d(string2, "toString(...)");
                byte[] bytes = string2.getBytes(C0596d.f5679b);
                p135u2.m.d(bytes, "getBytes(...)");
                p135u2.m.b(bVarA);
                h hVar = new h(iVarA);
                gVar.f12457e = p080l2.k.a(path);
                gVar.f12458f = p080l2.k.a(iVarA);
                gVar.f12459g = bVarA;
                gVar.f12460h = p080l2.k.a(bVarA);
                gVar.f12461i = p080l2.k.a(jSONObject);
                gVar.f12462j = 0;
                gVar.f12465m = 1;
                objA = p086m1.h.a(bytes, bVarA, hVar, gVar);
                this = bVarA;
                if (objA == objE) {
                    return objE;
                }
            } else {
                if (i7 != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                AutoCloseable autoCloseable = (AutoCloseable) gVar.f12459g;
                s.b(objA);
                this = autoCloseable;
            }
            Boolean boolA = p080l2.b.a(((JSONObject) objA).getBoolean("checkRes"));
            p117r2.a.a(this, null);
            return boolA;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                p117r2.a.a(this, th);
                throw th2;
            }
        }
    }

    public final void K() {
        try {
            this.ccl = new com.miui.securitycenter.confidentialcomputing.c(this);
        } catch (Exception e6) {
            Log.e("AiGuardService", "Failed to init pcc:", e6);
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
        this.timedShutdownJob = AbstractC0652i.b(this.scope, null, null, new j(null), 3, null);
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
        this.ccl = null;
        AbstractC0652i.b(this.scope, null, null, new k(null), 3, null);
        this.callSessionContext = null;
        this.timedShutdownJob = null;
        R(this.counter.b(), this.counter.c(), this.counter.d(), this.counter.e(), this.counter.a());
        N.c(this.scope, null, 1, null);
        Log.i("AiGuardService", "Destroyed.");
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int flags, int startId) {
        p135u2.m.e(intent, "intent");
        int intExtra = intent.getIntExtra("COMMAND", 0);
        this.capabilities = intent.getIntExtra("CAPABILITIES", 0);
        if (intExtra == 3) {
            AbstractC0652i.b(this.scope, null, null, new l(intent.getBooleanExtra("ai_guard_function_switch", false), null), 3, null);
            return super.onStartCommand(intent, flags, startId);
        }
        startForeground(1, y(), 160);
        J();
        K();
        if (intExtra == 1) {
            int intExtra2 = intent.getIntExtra("PHONE_SCAM_SCENE", 0);
            if (checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                L(intExtra2);
                Log.i("AiGuardService", "Phone scam detection started,scene:" + intExtra2);
            } else {
                Log.w("AiGuardService", "Required permissions have been revoked,phone scam detection is unavailable.");
            }
        } else if (intExtra != 2) {
            stopSelf();
            Log.w("AiGuardService", "Unknown command:" + intExtra + ".");
        } else {
            AbstractC0652i.b(this.scope, null, null, new m(intent.getIntExtra("VIDEO_SCAM_SCENE", 0), null), 3, null);
            Log.i("AiGuardService", "Video scam detection started.");
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
