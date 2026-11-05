package p058i1;

import A2.n;
import android.content.ContentResolver;
import android.content.Context;
import com.google.gson.g;
import com.google.gson.j;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import p045g2.AbstractC0962q;
import p123s2.a;
import p135u2.B;
import p135u2.m;

/* JADX INFO: loaded from: classes.dex */
public final class o {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final o f15831a = new o();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f15832b = "[\n                                                            \"dada\",//O3\n                                                            \"haotian\",//O2\n                                                            \"xuanyuan\",//O1\n                                                            \"pudding\",//P3\n                                                            \"popsicle\",//P2\n                                                            \"miro\"//O11U\n                                                    ]";

    private o() {
    }

    public static final int a(Context context) {
        String str;
        Class<Integer> cls = Integer.class;
        m.e(context, "context");
        ContentResolver contentResolver = context.getContentResolver();
        m.d(contentResolver, "getContentResolver(...)");
        Integer num = 10;
        try {
            Class<?> cls2 = Class.forName("android.provider.MiuiSettings$SettingsCloudData");
            Class cls3 = Integer.TYPE;
            n nVarI = B.i(cls3);
            if (m.a(nVarI, B.i(cls3))) {
                str = "getCloudDataInt";
            } else if (m.a(nVarI, B.i(Boolean.TYPE))) {
                str = "getCloudDataBoolean";
            } else {
                if (!m.a(nVarI, B.i(Long.TYPE))) {
                    if (m.a(nVarI, B.i(String.class))) {
                        str = "getCloudDataString";
                    }
                    return num.intValue();
                }
                str = "getCloudDataLong";
            }
            Class<Integer> clsD = a.d(B.b(cls));
            if (clsD != null) {
                cls = clsD;
            }
            Object objInvoke = cls2.getMethod(str, ContentResolver.class, String.class, String.class, cls).invoke(null, contentResolver, "AiGuardServiceConfigs", "detectionServiceLifetimeThresholdInMinutes", num);
            if (objInvoke == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
            }
            num = (Integer) objInvoke;
            return num.intValue();
        } catch (Exception unused) {
        }
    }

    /* JADX WARN: Code duplicated, block: B:27:0x00a7 A[Catch: n -> 0x00b5, TRY_LEAVE, TryCatch #0 {n -> 0x00b5, blocks: (B:24:0x0089, B:25:0x00a1, B:27:0x00a7), top: B:35:0x0089 }] */
    public static final List b(Context context) {
        ArrayList arrayList;
        Iterator it;
        String str;
        m.e(context, "context");
        ContentResolver contentResolver = context.getContentResolver();
        m.d(contentResolver, "getContentResolver(...)");
        String str2 = f15832b;
        try {
            Class<?> cls = Class.forName("android.provider.MiuiSettings$SettingsCloudData");
            n nVarI = B.i(String.class);
            if (m.a(nVarI, B.i(Integer.TYPE))) {
                str = "getCloudDataInt";
            } else if (m.a(nVarI, B.i(Boolean.TYPE))) {
                str = "getCloudDataBoolean";
            } else {
                if (!m.a(nVarI, B.i(Long.TYPE))) {
                    if (m.a(nVarI, B.i(String.class))) {
                        str = "getCloudDataString";
                    }
                    try {
                        g gVarF = com.google.gson.o.c(str2).f();
                        m.d(gVarF, "getAsJsonArray(...)");
                        arrayList = new ArrayList(AbstractC0962q.v(gVarF, 10));
                        it = gVarF.iterator();
                        while (it.hasNext()) {
                            arrayList.add(((j) it.next()).m());
                        }
                    } catch (com.google.gson.n unused) {
                        g gVarF2 = com.google.gson.o.c(f15832b).f();
                        m.d(gVarF2, "getAsJsonArray(...)");
                        arrayList = new ArrayList(AbstractC0962q.v(gVarF2, 10));
                        Iterator it2 = gVarF2.iterator();
                        while (it2.hasNext()) {
                            arrayList.add(((j) it2.next()).m());
                        }
                    }
                    return arrayList;
                }
                str = "getCloudDataLong";
            }
            Class clsD = a.d(B.b(String.class));
            if (clsD == null) {
                clsD = String.class;
            }
            Object objInvoke = cls.getMethod(str, ContentResolver.class, String.class, String.class, clsD).invoke(null, contentResolver, "AiGuardServiceConfigs", "onDevice", str2);
            if (objInvoke == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.String");
            }
            str2 = (String) objInvoke;
            g gVarF3 = com.google.gson.o.c(str2).f();
            m.d(gVarF3, "getAsJsonArray(...)");
            arrayList = new ArrayList(AbstractC0962q.v(gVarF3, 10));
            it = gVarF3.iterator();
            while (it.hasNext()) {
                arrayList.add(((j) it.next()).m());
            }
            return arrayList;
        } catch (Exception unused2) {
        }
    }

    public static final int d(Context context) {
        String str;
        Class<Integer> cls = Integer.class;
        m.e(context, "context");
        ContentResolver contentResolver = context.getContentResolver();
        m.d(contentResolver, "getContentResolver(...)");
        Integer num = 10;
        try {
            Class<?> cls2 = Class.forName("android.provider.MiuiSettings$SettingsCloudData");
            Class cls3 = Integer.TYPE;
            n nVarI = B.i(cls3);
            if (m.a(nVarI, B.i(cls3))) {
                str = "getCloudDataInt";
            } else if (m.a(nVarI, B.i(Boolean.TYPE))) {
                str = "getCloudDataBoolean";
            } else {
                if (!m.a(nVarI, B.i(Long.TYPE))) {
                    if (m.a(nVarI, B.i(String.class))) {
                        str = "getCloudDataString";
                    }
                    return num.intValue();
                }
                str = "getCloudDataLong";
            }
            Class<Integer> clsD = a.d(B.b(cls));
            if (clsD != null) {
                cls = clsD;
            }
            Object objInvoke = cls2.getMethod(str, ContentResolver.class, String.class, String.class, cls).invoke(null, contentResolver, "AiGuardServiceConfigs", "videoCaptureVelocity4CloudSide", num);
            if (objInvoke == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
            }
            num = (Integer) objInvoke;
            return num.intValue();
        } catch (Exception unused) {
        }
    }

    public static final float e(Context context) {
        m.e(context, "context");
        return 1 / f15831a.c(context);
    }

    public static final int f(Context context) {
        String str;
        Class<Integer> cls = Integer.class;
        m.e(context, "context");
        ContentResolver contentResolver = context.getContentResolver();
        m.d(contentResolver, "getContentResolver(...)");
        Integer num = 10;
        try {
            Class<?> cls2 = Class.forName("android.provider.MiuiSettings$SettingsCloudData");
            Class cls3 = Integer.TYPE;
            n nVarI = B.i(cls3);
            if (m.a(nVarI, B.i(cls3))) {
                str = "getCloudDataInt";
            } else if (m.a(nVarI, B.i(Boolean.TYPE))) {
                str = "getCloudDataBoolean";
            } else {
                if (!m.a(nVarI, B.i(Long.TYPE))) {
                    if (m.a(nVarI, B.i(String.class))) {
                        str = "getCloudDataString";
                    }
                    return num.intValue();
                }
                str = "getCloudDataLong";
            }
            Class<Integer> clsD = a.d(B.b(cls));
            if (clsD != null) {
                cls = clsD;
            }
            Object objInvoke = cls2.getMethod(str, ContentResolver.class, String.class, String.class, cls).invoke(null, contentResolver, "AiGuardServiceConfigs", "voiceDetectionGapInSeconds", num);
            if (objInvoke == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
            }
            num = (Integer) objInvoke;
            return num.intValue();
        } catch (Exception unused) {
        }
    }

    public final int c(Context context) {
        String str;
        Class<Integer> cls = Integer.class;
        m.e(context, "context");
        ContentResolver contentResolver = context.getContentResolver();
        m.d(contentResolver, "getContentResolver(...)");
        Integer num = 10;
        try {
            Class<?> cls2 = Class.forName("android.provider.MiuiSettings$SettingsCloudData");
            Class cls3 = Integer.TYPE;
            n nVarI = B.i(cls3);
            if (m.a(nVarI, B.i(cls3))) {
                str = "getCloudDataInt";
            } else if (m.a(nVarI, B.i(Boolean.TYPE))) {
                str = "getCloudDataBoolean";
            } else {
                if (!m.a(nVarI, B.i(Long.TYPE))) {
                    if (m.a(nVarI, B.i(String.class))) {
                        str = "getCloudDataString";
                    }
                    return num.intValue();
                }
                str = "getCloudDataLong";
            }
            Class<Integer> clsD = a.d(B.b(cls));
            if (clsD != null) {
                cls = clsD;
            }
            Object objInvoke = cls2.getMethod(str, ContentResolver.class, String.class, String.class, cls).invoke(null, contentResolver, "AiGuardServiceConfigs", "videoCaptureGap4OnDevice", num);
            if (objInvoke == null) {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
            }
            num = (Integer) objInvoke;
            return num.intValue();
        } catch (Exception unused) {
        }
    }
}
