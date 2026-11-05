package F1;

import com.miui.guardprovider.sdk.android.pojo.networkdomain.FakeAppReportRequest;
import kotlin.Metadata;
import tmsdk.common.tcc.SmsCheckerContentTypes;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b`\u0018\u00002\u00020\u0001J$\u0010\u0007\u001a\u00020\u00062\b\b\u0001\u0010\u0003\u001a\u00020\u00022\b\b\u0003\u0010\u0005\u001a\u00020\u0004H§@¢\u0006\u0004\b\u0007\u0010\b¨\u0006\tÀ\u0006\u0003"}, d2 = {"LF1/g;", com.xiaomi.onetrack.util.a.f14247c, "Lcom/miui/guardprovider/sdk/android/pojo/networkdomain/FakeAppReportRequest;", "request", com.xiaomi.onetrack.util.a.f14247c, "ts", "Lcom/google/gson/m;", "a", "(Lcom/miui/guardprovider/sdk/android/pojo/networkdomain/FakeAppReportRequest;JLj2/e;)Ljava/lang/Object;", "flamingo-android_cnRelease"}, k = 1, mv = {2, 2, 0}, xi = SmsCheckerContentTypes.TYPE_TEL_NET_TEL)
public interface g {
    static /* synthetic */ Object b(g gVar, FakeAppReportRequest fakeAppReportRequest, long j6, p066j2.e eVar, int i6, Object obj) {
        if (obj != null) {
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: report");
        }
        if ((i6 & 2) != 0) {
            j6 = System.currentTimeMillis();
        }
        return gVar.a(fakeAppReportRequest, j6, eVar);
    }

    @K5.o("/hyperos/fraud/app/report")
    Object a(@K5.a FakeAppReportRequest fakeAppReportRequest, @K5.t("timestamp") long j6, p066j2.e<? super com.google.gson.m> eVar);
}
