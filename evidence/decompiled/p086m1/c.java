package p086m1;

import I5.d;
import K5.e;
import K5.o;
import com.miui.guardprovider.aiguard.service.FeedbackResponse;

/* JADX INFO: loaded from: classes.dex */
public interface c {
    @e
    @o("/aiVideo/pcc/rbi")
    d<FeedbackResponse> a(@K5.c("data") String str);

    @e
    @o("/ai/audio/text/pcc/rbi")
    d<FeedbackResponse> b(@K5.c("data") String str);
}
