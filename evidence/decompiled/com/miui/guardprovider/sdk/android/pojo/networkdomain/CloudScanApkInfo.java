package com.miui.guardprovider.sdk.android.pojo.networkdomain;

import kotlin.Metadata;
import p135u2.m;
import tmsdk.common.tcc.SmsCheckerContentTypes;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0081\b\u0018\u00002\u00020\u0001B/\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003¢\u0006\u0004\b\b\u0010\tJ\t\u0010\u0010\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0011\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0012\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0013\u001a\u00020\u0003HÆ\u0003J\t\u0010\u0014\u001a\u00020\u0003HÆ\u0003J;\u0010\u0015\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00032\b\b\u0002\u0010\u0007\u001a\u00020\u0003HÆ\u0001J\u0013\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0019\u001a\u00020\u001aHÖ\u0001J\t\u0010\u001b\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000bR\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0011\u0010\u0007\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000b¨\u0006\u001c"}, d2 = {"Lcom/miui/guardprovider/sdk/android/pojo/networkdomain/CloudScanApkInfo;", com.xiaomi.onetrack.util.a.f14247c, "packageName", com.xiaomi.onetrack.util.a.f14247c, "versionCode", "apkCertificateMD5", "apkCertificateSHA256", "mfFileSHA256", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getPackageName", "()Ljava/lang/String;", "getVersionCode", "getApkCertificateMD5", "getApkCertificateSHA256", "getMfFileSHA256", "component1", "component2", "component3", "component4", "component5", "copy", "equals", com.xiaomi.onetrack.util.a.f14247c, "other", "hashCode", com.xiaomi.onetrack.util.a.f14247c, "toString", "flamingo-android_cnRelease"}, k = 1, mv = {2, 2, 0}, xi = SmsCheckerContentTypes.TYPE_TEL_NET_TEL)
public final /* data */ class CloudScanApkInfo {
    private final String apkCertificateMD5;
    private final String apkCertificateSHA256;
    private final String mfFileSHA256;
    private final String packageName;
    private final String versionCode;

    public CloudScanApkInfo(String str, String str2, String str3, String str4, String str5) {
        m.e(str, "packageName");
        m.e(str2, "versionCode");
        m.e(str3, "apkCertificateMD5");
        m.e(str4, "apkCertificateSHA256");
        m.e(str5, "mfFileSHA256");
        this.packageName = str;
        this.versionCode = str2;
        this.apkCertificateMD5 = str3;
        this.apkCertificateSHA256 = str4;
        this.mfFileSHA256 = str5;
    }

    public static /* synthetic */ CloudScanApkInfo copy$default(CloudScanApkInfo cloudScanApkInfo, String str, String str2, String str3, String str4, String str5, int i6, Object obj) {
        if ((i6 & 1) != 0) {
            str = cloudScanApkInfo.packageName;
        }
        if ((i6 & 2) != 0) {
            str2 = cloudScanApkInfo.versionCode;
        }
        if ((i6 & 4) != 0) {
            str3 = cloudScanApkInfo.apkCertificateMD5;
        }
        if ((i6 & 8) != 0) {
            str4 = cloudScanApkInfo.apkCertificateSHA256;
        }
        if ((i6 & 16) != 0) {
            str5 = cloudScanApkInfo.mfFileSHA256;
        }
        String str6 = str5;
        String str7 = str3;
        return cloudScanApkInfo.copy(str, str2, str7, str4, str6);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getPackageName() {
        return this.packageName;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getVersionCode() {
        return this.versionCode;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getApkCertificateMD5() {
        return this.apkCertificateMD5;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final String getApkCertificateSHA256() {
        return this.apkCertificateSHA256;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final String getMfFileSHA256() {
        return this.mfFileSHA256;
    }

    public final CloudScanApkInfo copy(String packageName, String versionCode, String apkCertificateMD5, String apkCertificateSHA256, String mfFileSHA256) {
        m.e(packageName, "packageName");
        m.e(versionCode, "versionCode");
        m.e(apkCertificateMD5, "apkCertificateMD5");
        m.e(apkCertificateSHA256, "apkCertificateSHA256");
        m.e(mfFileSHA256, "mfFileSHA256");
        return new CloudScanApkInfo(packageName, versionCode, apkCertificateMD5, apkCertificateSHA256, mfFileSHA256);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CloudScanApkInfo)) {
            return false;
        }
        CloudScanApkInfo cloudScanApkInfo = (CloudScanApkInfo) other;
        return m.a(this.packageName, cloudScanApkInfo.packageName) && m.a(this.versionCode, cloudScanApkInfo.versionCode) && m.a(this.apkCertificateMD5, cloudScanApkInfo.apkCertificateMD5) && m.a(this.apkCertificateSHA256, cloudScanApkInfo.apkCertificateSHA256) && m.a(this.mfFileSHA256, cloudScanApkInfo.mfFileSHA256);
    }

    public final String getApkCertificateMD5() {
        return this.apkCertificateMD5;
    }

    public final String getApkCertificateSHA256() {
        return this.apkCertificateSHA256;
    }

    public final String getMfFileSHA256() {
        return this.mfFileSHA256;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final String getVersionCode() {
        return this.versionCode;
    }

    public int hashCode() {
        return (((((((this.packageName.hashCode() * 31) + this.versionCode.hashCode()) * 31) + this.apkCertificateMD5.hashCode()) * 31) + this.apkCertificateSHA256.hashCode()) * 31) + this.mfFileSHA256.hashCode();
    }

    public String toString() {
        return "CloudScanApkInfo(packageName=" + this.packageName + ", versionCode=" + this.versionCode + ", apkCertificateMD5=" + this.apkCertificateMD5 + ", apkCertificateSHA256=" + this.apkCertificateSHA256 + ", mfFileSHA256=" + this.mfFileSHA256 + ")";
    }
}
