package F1;

import com.miui.guardprovider.sdk.android.pojo.networkdomain.ResultReportData;
import com.miui.guardprovider.sdk.android.pojo.networkdomain.ResultRequestResponse;
import java.util.List;
import kotlin.Metadata;
import tmsdk.common.tcc.SmsCheckerContentTypes;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b`\u0018\u00002\u00020\u0001J*\u0010\b\u001a\u00020\u00072\b\b\u0001\u0010\u0003\u001a\u00020\u00022\u000e\b\u0001\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004H§@¢\u0006\u0004\b\b\u0010\t¨\u0006\nÀ\u0006\u0003"}, d2 = {"LF1/t;", com.xiaomi.onetrack.util.a.f14247c, com.xiaomi.onetrack.util.a.f14247c, "ts", com.xiaomi.onetrack.util.a.f14247c, "Lcom/miui/guardprovider/sdk/android/pojo/networkdomain/ResultReportData;", "body", "Lcom/miui/guardprovider/sdk/android/pojo/networkdomain/ResultRequestResponse;", "a", "(JLjava/util/List;Lj2/e;)Ljava/lang/Object;", "flamingo-android_cnRelease"}, k = 1, mv = {2, 2, 0}, xi = SmsCheckerContentTypes.TYPE_TEL_NET_TEL)
public interface t {
    @K5.o("/virus/localResult/report")
    Object a(@K5.t("timestamp") long j6, @K5.a List<ResultReportData> list, p066j2.e<? super ResultRequestResponse> eVar);
}
