package p086m1;

import K5.c;
import K5.e;
import K5.o;
import com.miui.guardprovider.aiguard.service.EncryptedResponse;
import com.miui.securitycenter.confidentialcomputing.EncryptedData;
import com.xiaomi.onetrack.util.a;
import kotlin.Metadata;
import tmsdk.common.tcc.SmsCheckerContentTypes;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\b`\u0018\u00002\u00020\u0001J\u001a\u0010\u0005\u001a\u00020\u00042\b\b\u0001\u0010\u0003\u001a\u00020\u0002H§@¢\u0006\u0004\b\u0005\u0010\u0006¨\u0006\u0007À\u0006\u0003"}, d2 = {"Lm1/i;", a.f14247c, "Lcom/miui/securitycenter/confidentialcomputing/EncryptedData;", "data", "Lcom/miui/guardprovider/aiguard/service/EncryptedResponse;", "a", "(Lcom/miui/securitycenter/confidentialcomputing/EncryptedData;Lj2/e;)Ljava/lang/Object;", "ai-guard_release"}, k = 1, mv = {2, 2, 0}, xi = SmsCheckerContentTypes.TYPE_TEL_NET_TEL)
public interface i {
    @e
    @o("/aiVideo/pcc/detection")
    Object a(@c("data") EncryptedData encryptedData, p066j2.e<? super EncryptedResponse> eVar);
}
