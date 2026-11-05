package p098o1;

import android.content.Context;
import android.util.Log;
import com.xiaomi.ai.vision.sdk.AiCapability;
import com.xiaomi.ai.vision.sdk.callback.CtaCallback;
import com.xiaomi.ai.vision.sdk.callback.ErrorCallback;
import com.xiaomi.ai.vision.sdk.callback.SpeechRecognizeCallback;
import com.xiaomi.ai.vision.sdk.model.RecognizeResult;
import com.xiaomi.onetrack.api.ah;
import com.xiaomi.onetrack.util.aa;
import java.nio.ByteBuffer;
import java.util.function.Consumer;
import p038f2.D;
import p038f2.r;
import p038f2.s;
import p129t2.l;
import p135u2.AbstractC1069h;
import p135u2.m;
import tmsdk.bg.module.wifidetect.WifiDetectManager;

/* JADX INFO: loaded from: classes.dex */
public final class b {

    /* JADX INFO: renamed from: h, reason: collision with root package name */
    public static final a f19925h = new a(null);

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final p098o1.d f19926a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final AiCapability f19927b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f19928c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private boolean f19929d;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private l f19930e;

    /* JADX INFO: renamed from: f, reason: collision with root package name */
    private Consumer f19931f;

    /* JADX INFO: renamed from: g, reason: collision with root package name */
    private final d f19932g;

    public static final class a {

        /* JADX INFO: renamed from: o1.b$a$a, reason: collision with other inner class name */
        public static final class C0282a implements CtaCallback {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            final /* synthetic */ l f19933a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            final /* synthetic */ AiCapability f19934b;

            C0282a(l lVar, AiCapability aiCapability) {
                this.f19933a = lVar;
                this.f19934b = aiCapability;
            }

            @Override // com.xiaomi.ai.vision.sdk.callback.CtaCallback
            public void onResult(boolean z6) {
                this.f19933a.m(Boolean.valueOf(z6));
                this.f19934b.destroy();
            }

            @Override // com.xiaomi.ai.vision.sdk.callback.CtaCallback
            public void onShow() {
            }
        }

        public /* synthetic */ a(AbstractC1069h abstractC1069h) {
            this();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final D c(AiCapability aiCapability, l lVar, boolean z6) {
            if (z6) {
                lVar.m(Boolean.TRUE);
                aiCapability.destroy();
            } else {
                aiCapability.requestCtaAccredit(new C0282a(lVar, aiCapability));
            }
            return D.f14789a;
        }

        public final void b(Context context, final l lVar) {
            m.e(context, "context");
            m.e(lVar, "cb");
            final AiCapability aiCapability = new AiCapability(context, null, null, 6, null);
            aiCapability.checkIfCtaAllowed(new l() { // from class: o1.a
                @Override // p129t2.l
                public final Object m(Object obj) {
                    return b.a.c(aiCapability, lVar, ((Boolean) obj).booleanValue());
                }
            });
        }

        private a() {
        }
    }

    /* JADX INFO: renamed from: o1.b$b, reason: collision with other inner class name */
    private static final class C0283b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final String f19935a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private final int f19936b;

        public C0283b(String str, int i6) {
            m.e(str, "sentence");
            this.f19935a = str;
            this.f19936b = i6;
        }

        public final int a() {
            return this.f19936b;
        }

        public final String b() {
            return this.f19935a;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof C0283b)) {
                return false;
            }
            C0283b c0283b = (C0283b) obj;
            return m.a(this.f19935a, c0283b.f19935a) && this.f19936b == c0283b.f19936b;
        }

        public int hashCode() {
            return (this.f19935a.hashCode() * 31) + Integer.hashCode(this.f19936b);
        }

        public String toString() {
            return "Sentence(sentence=" + this.f19935a + ", id=" + this.f19936b + ")";
        }
    }

    private static final class c {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private C0283b f19937a;

        public final String a(C0283b c0283b) {
            m.e(c0283b, "sentence");
            C0283b c0283b2 = this.f19937a;
            if (c0283b2 == null) {
                this.f19937a = c0283b;
                return null;
            }
            if (c0283b2.a() == c0283b.a()) {
                this.f19937a = c0283b;
                return null;
            }
            this.f19937a = c0283b;
            return c0283b2.b();
        }
    }

    public static final class d implements SpeechRecognizeCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private final c f19938a = new c();

        d() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static final void b(b bVar, d dVar, String str, ByteBuffer byteBuffer) {
            Object objB;
            byte[] bArr = new byte[byteBuffer.remaining()];
            m.b(byteBuffer);
            byteBuffer.get(bArr);
            Consumer consumer = bVar.f19931f;
            if (consumer != null) {
                consumer.accept(bArr);
            }
            try {
                r.a aVar = r.f14813c;
                objB = r.b(Boolean.valueOf(bVar.f19927b.postData(bArr, false, str)));
            } catch (Throwable th) {
                r.a aVar2 = r.f14813c;
                objB = r.b(s.a(th));
            }
            Throwable thD = r.d(objB);
            if (thD != null) {
                Log.w("Asr", "Asr service might be dead", thD);
            }
        }

        @Override // com.xiaomi.ai.vision.sdk.callback.SpeechRecognizeCallback
        public void onRecognizeResult(String str, RecognizeResult recognizeResult) {
            m.e(str, "id");
            m.e(recognizeResult, ah.f13634K);
            String strA = this.f19938a.a(new C0283b(recognizeResult.getSrcStr(), recognizeResult.getSegId()));
            if (strA != null) {
                l lVar = b.this.f19930e;
                if (lVar == null) {
                    m.r("sentenceCallback");
                    lVar = null;
                }
                lVar.m(strA);
            }
        }

        @Override // com.xiaomi.ai.vision.sdk.callback.SpeechRecognizeCallback
        public void onRecognizeStart(final String str) {
            m.e(str, "id");
            b.this.f19928c = str;
            p098o1.d dVar = b.this.f19926a;
            final b bVar = b.this;
            dVar.a(new Consumer() { // from class: o1.c
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    b.d.b(bVar, this, str, (ByteBuffer) obj);
                }
            });
        }

        @Override // com.xiaomi.ai.vision.sdk.callback.SpeechRecognizeCallback
        public void onRecognizeStop(String str) {
            m.e(str, "id");
        }
    }

    public static final class e implements ErrorCallback {
        e() {
        }

        @Override // com.xiaomi.ai.vision.sdk.callback.ErrorCallback
        public void onError(int i6, String str) {
            m.e(str, com.xiaomi.onetrack.g.a.f14179c);
            Log.e("Asr", "onError: " + i6 + aa.f14258b + str);
            b.this.i();
        }
    }

    public b(Context context, p098o1.d dVar) {
        m.e(context, "context");
        m.e(dVar, "audioSource");
        this.f19926a = dVar;
        this.f19927b = new AiCapability(context, null, null, 6, null);
        this.f19932g = new d();
    }

    public final void f() {
        try {
            r.a aVar = r.f14813c;
            this.f19927b.start();
            r.b(D.f14789a);
        } catch (Throwable th) {
            r.a aVar2 = r.f14813c;
            r.b(s.a(th));
        }
    }

    public final void g() {
        try {
            r.a aVar = r.f14813c;
            this.f19927b.destroy();
            r.b(D.f14789a);
        } catch (Throwable th) {
            r.a aVar2 = r.f14813c;
            r.b(s.a(th));
        }
    }

    public final void h(l lVar, Consumer consumer) {
        m.e(lVar, "asrCallback");
        m.e(consumer, "recorderCallback");
        if (this.f19929d) {
            return;
        }
        this.f19930e = lVar;
        this.f19931f = consumer;
        try {
            r.a aVar = r.f14813c;
            AiCapability.startRecognize$default(this.f19927b, 4, 0, false, "zh-CN", "zh-CN", false, false, this.f19932g, null, new e(), WifiDetectManager.SECURITY_NONE, null);
            r.b(D.f14789a);
        } catch (Throwable th) {
            r.a aVar2 = r.f14813c;
            r.b(s.a(th));
        }
        this.f19929d = true;
    }

    public final void i() {
        if (this.f19929d) {
            try {
                r.a aVar = r.f14813c;
                String str = this.f19928c;
                if (str != null) {
                    this.f19927b.stopRecognize(str);
                }
                this.f19926a.stop();
                r.b(D.f14789a);
            } catch (Throwable th) {
                r.a aVar2 = r.f14813c;
                r.b(s.a(th));
            }
            this.f19928c = null;
            this.f19929d = false;
        }
    }
}
