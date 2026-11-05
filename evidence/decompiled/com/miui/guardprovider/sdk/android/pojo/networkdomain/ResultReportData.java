package com.miui.guardprovider.sdk.android.pojo.networkdomain;

import kotlin.Metadata;
import p135u2.m;
import tmsdk.bg.module.wifidetect.WifiDetectManager;
import tmsdk.common.roach.nest.PowerNest;
import tmsdk.common.tcc.SmsCheckerContentTypes;

/* JADX INFO: loaded from: classes.dex */
@Metadata(d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b5\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0081\b\u0018\u00002\u00020\u0001B\u0091\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\u0003\u0012\u0006\u0010\t\u001a\u00020\u0003\u0012\u0006\u0010\n\u001a\u00020\u0007\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\u0006\u0010\f\u001a\u00020\u0003\u0012\u0006\u0010\r\u001a\u00020\u0003\u0012\u0006\u0010\u000e\u001a\u00020\u0003\u0012\u0006\u0010\u000f\u001a\u00020\u0003\u0012\u0006\u0010\u0010\u001a\u00020\u0003\u0012\u0006\u0010\u0011\u001a\u00020\u0007\u0012\u0006\u0010\u0012\u001a\u00020\u0003\u0012\u0006\u0010\u0013\u001a\u00020\u0003\u0012\b\u0010\u0014\u001a\u0004\u0018\u00010\u0003¢\u0006\u0004\b\u0015\u0010\u0016J\t\u0010*\u001a\u00020\u0003HÆ\u0003J\t\u0010+\u001a\u00020\u0003HÆ\u0003J\t\u0010,\u001a\u00020\u0003HÆ\u0003J\t\u0010-\u001a\u00020\u0007HÆ\u0003J\t\u0010.\u001a\u00020\u0003HÆ\u0003J\t\u0010/\u001a\u00020\u0003HÆ\u0003J\t\u00100\u001a\u00020\u0007HÆ\u0003J\t\u00101\u001a\u00020\u0003HÆ\u0003J\t\u00102\u001a\u00020\u0003HÆ\u0003J\t\u00103\u001a\u00020\u0003HÆ\u0003J\t\u00104\u001a\u00020\u0003HÆ\u0003J\t\u00105\u001a\u00020\u0003HÆ\u0003J\t\u00106\u001a\u00020\u0003HÆ\u0003J\t\u00107\u001a\u00020\u0007HÆ\u0003J\t\u00108\u001a\u00020\u0003HÆ\u0003J\t\u00109\u001a\u00020\u0003HÆ\u0003J\u000b\u0010:\u001a\u0004\u0018\u00010\u0003HÆ\u0003Jµ\u0001\u0010;\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u00032\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u00032\b\b\u0002\u0010\t\u001a\u00020\u00032\b\b\u0002\u0010\n\u001a\u00020\u00072\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\u00032\b\b\u0002\u0010\r\u001a\u00020\u00032\b\b\u0002\u0010\u000e\u001a\u00020\u00032\b\b\u0002\u0010\u000f\u001a\u00020\u00032\b\b\u0002\u0010\u0010\u001a\u00020\u00032\b\b\u0002\u0010\u0011\u001a\u00020\u00072\b\b\u0002\u0010\u0012\u001a\u00020\u00032\b\b\u0002\u0010\u0013\u001a\u00020\u00032\n\b\u0002\u0010\u0014\u001a\u0004\u0018\u00010\u0003HÆ\u0001J\u0013\u0010<\u001a\u00020=2\b\u0010>\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010?\u001a\u00020\u0007HÖ\u0001J\t\u0010@\u001a\u00020\u0003HÖ\u0001R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0018R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0018R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0018R\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u0018R\u0011\u0010\n\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010\u001cR\u0011\u0010\u000b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u0018R\u0011\u0010\f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u0018R\u0011\u0010\r\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0018R\u0011\u0010\u000e\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b#\u0010\u0018R\u0011\u0010\u000f\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0018R\u0011\u0010\u0010\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b%\u0010\u0018R\u0011\u0010\u0011\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b&\u0010\u001cR\u0011\u0010\u0012\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b'\u0010\u0018R\u0011\u0010\u0013\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b(\u0010\u0018R\u0013\u0010\u0014\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b)\u0010\u0018¨\u0006A"}, d2 = {"Lcom/miui/guardprovider/sdk/android/pojo/networkdomain/ResultReportData;", com.xiaomi.onetrack.util.a.f14247c, "threatLevel", com.xiaomi.onetrack.util.a.f14247c, "threatName", "malwareCodeFeature", "version", com.xiaomi.onetrack.util.a.f14247c, "packageName", "appName", "versionCode", "apkCertificateMD5", "apkCertificateSHA256", "mfFileSHA256", "apkMd5", "apkSha256", "miuiVersion", "androidVersion", "device", "uuid", "hitFiles", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getThreatLevel", "()Ljava/lang/String;", "getThreatName", "getMalwareCodeFeature", "getVersion", "()I", "getPackageName", "getAppName", "getVersionCode", "getApkCertificateMD5", "getApkCertificateSHA256", "getMfFileSHA256", "getApkMd5", "getApkSha256", "getMiuiVersion", "getAndroidVersion", "getDevice", "getUuid", "getHitFiles", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "copy", "equals", com.xiaomi.onetrack.util.a.f14247c, "other", "hashCode", "toString", "flamingo-android_cnRelease"}, k = 1, mv = {2, 2, 0}, xi = SmsCheckerContentTypes.TYPE_TEL_NET_TEL)
public final /* data */ class ResultReportData {
    private final int androidVersion;
    private final String apkCertificateMD5;
    private final String apkCertificateSHA256;
    private final String apkMd5;
    private final String apkSha256;
    private final String appName;
    private final String device;
    private final String hitFiles;
    private final String malwareCodeFeature;
    private final String mfFileSHA256;
    private final String miuiVersion;
    private final String packageName;
    private final String threatLevel;
    private final String threatName;
    private final String uuid;
    private final int version;
    private final int versionCode;

    public ResultReportData(String str, String str2, String str3, int i6, String str4, String str5, int i7, String str6, String str7, String str8, String str9, String str10, String str11, int i8, String str12, String str13, String str14) {
        m.e(str, "threatLevel");
        m.e(str2, "threatName");
        m.e(str3, "malwareCodeFeature");
        m.e(str4, "packageName");
        m.e(str5, "appName");
        m.e(str6, "apkCertificateMD5");
        m.e(str7, "apkCertificateSHA256");
        m.e(str8, "mfFileSHA256");
        m.e(str9, "apkMd5");
        m.e(str10, "apkSha256");
        m.e(str11, "miuiVersion");
        m.e(str12, "device");
        m.e(str13, "uuid");
        this.threatLevel = str;
        this.threatName = str2;
        this.malwareCodeFeature = str3;
        this.version = i6;
        this.packageName = str4;
        this.appName = str5;
        this.versionCode = i7;
        this.apkCertificateMD5 = str6;
        this.apkCertificateSHA256 = str7;
        this.mfFileSHA256 = str8;
        this.apkMd5 = str9;
        this.apkSha256 = str10;
        this.miuiVersion = str11;
        this.androidVersion = i8;
        this.device = str12;
        this.uuid = str13;
        this.hitFiles = str14;
    }

    public static /* synthetic */ ResultReportData copy$default(ResultReportData resultReportData, String str, String str2, String str3, int i6, String str4, String str5, int i7, String str6, String str7, String str8, String str9, String str10, String str11, int i8, String str12, String str13, String str14, int i9, Object obj) {
        String str15;
        String str16;
        String str17 = (i9 & 1) != 0 ? resultReportData.threatLevel : str;
        String str18 = (i9 & 2) != 0 ? resultReportData.threatName : str2;
        String str19 = (i9 & 4) != 0 ? resultReportData.malwareCodeFeature : str3;
        int i10 = (i9 & 8) != 0 ? resultReportData.version : i6;
        String str20 = (i9 & 16) != 0 ? resultReportData.packageName : str4;
        String str21 = (i9 & 32) != 0 ? resultReportData.appName : str5;
        int i11 = (i9 & 64) != 0 ? resultReportData.versionCode : i7;
        String str22 = (i9 & 128) != 0 ? resultReportData.apkCertificateMD5 : str6;
        String str23 = (i9 & WifiDetectManager.SECURITY_NONE) != 0 ? resultReportData.apkCertificateSHA256 : str7;
        String str24 = (i9 & PowerNest.SHARK_HTTP) != 0 ? resultReportData.mfFileSHA256 : str8;
        String str25 = (i9 & PowerNest.SHARK_TCP) != 0 ? resultReportData.apkMd5 : str9;
        String str26 = (i9 & 2048) != 0 ? resultReportData.apkSha256 : str10;
        String str27 = (i9 & 4096) != 0 ? resultReportData.miuiVersion : str11;
        int i12 = (i9 & 8192) != 0 ? resultReportData.androidVersion : i8;
        String str28 = str17;
        String str29 = (i9 & 16384) != 0 ? resultReportData.device : str12;
        String str30 = (i9 & 32768) != 0 ? resultReportData.uuid : str13;
        if ((i9 & 65536) != 0) {
            str16 = str30;
            str15 = resultReportData.hitFiles;
        } else {
            str15 = str14;
            str16 = str30;
        }
        return resultReportData.copy(str28, str18, str19, i10, str20, str21, i11, str22, str23, str24, str25, str26, str27, i12, str29, str16, str15);
    }

    /* JADX INFO: renamed from: component1, reason: from getter */
    public final String getThreatLevel() {
        return this.threatLevel;
    }

    /* JADX INFO: renamed from: component10, reason: from getter */
    public final String getMfFileSHA256() {
        return this.mfFileSHA256;
    }

    /* JADX INFO: renamed from: component11, reason: from getter */
    public final String getApkMd5() {
        return this.apkMd5;
    }

    /* JADX INFO: renamed from: component12, reason: from getter */
    public final String getApkSha256() {
        return this.apkSha256;
    }

    /* JADX INFO: renamed from: component13, reason: from getter */
    public final String getMiuiVersion() {
        return this.miuiVersion;
    }

    /* JADX INFO: renamed from: component14, reason: from getter */
    public final int getAndroidVersion() {
        return this.androidVersion;
    }

    /* JADX INFO: renamed from: component15, reason: from getter */
    public final String getDevice() {
        return this.device;
    }

    /* JADX INFO: renamed from: component16, reason: from getter */
    public final String getUuid() {
        return this.uuid;
    }

    /* JADX INFO: renamed from: component17, reason: from getter */
    public final String getHitFiles() {
        return this.hitFiles;
    }

    /* JADX INFO: renamed from: component2, reason: from getter */
    public final String getThreatName() {
        return this.threatName;
    }

    /* JADX INFO: renamed from: component3, reason: from getter */
    public final String getMalwareCodeFeature() {
        return this.malwareCodeFeature;
    }

    /* JADX INFO: renamed from: component4, reason: from getter */
    public final int getVersion() {
        return this.version;
    }

    /* JADX INFO: renamed from: component5, reason: from getter */
    public final String getPackageName() {
        return this.packageName;
    }

    /* JADX INFO: renamed from: component6, reason: from getter */
    public final String getAppName() {
        return this.appName;
    }

    /* JADX INFO: renamed from: component7, reason: from getter */
    public final int getVersionCode() {
        return this.versionCode;
    }

    /* JADX INFO: renamed from: component8, reason: from getter */
    public final String getApkCertificateMD5() {
        return this.apkCertificateMD5;
    }

    /* JADX INFO: renamed from: component9, reason: from getter */
    public final String getApkCertificateSHA256() {
        return this.apkCertificateSHA256;
    }

    public final ResultReportData copy(String threatLevel, String threatName, String malwareCodeFeature, int version, String packageName, String appName, int versionCode, String apkCertificateMD5, String apkCertificateSHA256, String mfFileSHA256, String apkMd5, String apkSha256, String miuiVersion, int androidVersion, String device, String uuid, String hitFiles) {
        m.e(threatLevel, "threatLevel");
        m.e(threatName, "threatName");
        m.e(malwareCodeFeature, "malwareCodeFeature");
        m.e(packageName, "packageName");
        m.e(appName, "appName");
        m.e(apkCertificateMD5, "apkCertificateMD5");
        m.e(apkCertificateSHA256, "apkCertificateSHA256");
        m.e(mfFileSHA256, "mfFileSHA256");
        m.e(apkMd5, "apkMd5");
        m.e(apkSha256, "apkSha256");
        m.e(miuiVersion, "miuiVersion");
        m.e(device, "device");
        m.e(uuid, "uuid");
        return new ResultReportData(threatLevel, threatName, malwareCodeFeature, version, packageName, appName, versionCode, apkCertificateMD5, apkCertificateSHA256, mfFileSHA256, apkMd5, apkSha256, miuiVersion, androidVersion, device, uuid, hitFiles);
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ResultReportData)) {
            return false;
        }
        ResultReportData resultReportData = (ResultReportData) other;
        return m.a(this.threatLevel, resultReportData.threatLevel) && m.a(this.threatName, resultReportData.threatName) && m.a(this.malwareCodeFeature, resultReportData.malwareCodeFeature) && this.version == resultReportData.version && m.a(this.packageName, resultReportData.packageName) && m.a(this.appName, resultReportData.appName) && this.versionCode == resultReportData.versionCode && m.a(this.apkCertificateMD5, resultReportData.apkCertificateMD5) && m.a(this.apkCertificateSHA256, resultReportData.apkCertificateSHA256) && m.a(this.mfFileSHA256, resultReportData.mfFileSHA256) && m.a(this.apkMd5, resultReportData.apkMd5) && m.a(this.apkSha256, resultReportData.apkSha256) && m.a(this.miuiVersion, resultReportData.miuiVersion) && this.androidVersion == resultReportData.androidVersion && m.a(this.device, resultReportData.device) && m.a(this.uuid, resultReportData.uuid) && m.a(this.hitFiles, resultReportData.hitFiles);
    }

    public final int getAndroidVersion() {
        return this.androidVersion;
    }

    public final String getApkCertificateMD5() {
        return this.apkCertificateMD5;
    }

    public final String getApkCertificateSHA256() {
        return this.apkCertificateSHA256;
    }

    public final String getApkMd5() {
        return this.apkMd5;
    }

    public final String getApkSha256() {
        return this.apkSha256;
    }

    public final String getAppName() {
        return this.appName;
    }

    public final String getDevice() {
        return this.device;
    }

    public final String getHitFiles() {
        return this.hitFiles;
    }

    public final String getMalwareCodeFeature() {
        return this.malwareCodeFeature;
    }

    public final String getMfFileSHA256() {
        return this.mfFileSHA256;
    }

    public final String getMiuiVersion() {
        return this.miuiVersion;
    }

    public final String getPackageName() {
        return this.packageName;
    }

    public final String getThreatLevel() {
        return this.threatLevel;
    }

    public final String getThreatName() {
        return this.threatName;
    }

    public final String getUuid() {
        return this.uuid;
    }

    public final int getVersion() {
        return this.version;
    }

    public final int getVersionCode() {
        return this.versionCode;
    }

    public int hashCode() {
        int iHashCode = ((((((((((((((((((((((((((((((this.threatLevel.hashCode() * 31) + this.threatName.hashCode()) * 31) + this.malwareCodeFeature.hashCode()) * 31) + Integer.hashCode(this.version)) * 31) + this.packageName.hashCode()) * 31) + this.appName.hashCode()) * 31) + Integer.hashCode(this.versionCode)) * 31) + this.apkCertificateMD5.hashCode()) * 31) + this.apkCertificateSHA256.hashCode()) * 31) + this.mfFileSHA256.hashCode()) * 31) + this.apkMd5.hashCode()) * 31) + this.apkSha256.hashCode()) * 31) + this.miuiVersion.hashCode()) * 31) + Integer.hashCode(this.androidVersion)) * 31) + this.device.hashCode()) * 31) + this.uuid.hashCode()) * 31;
        String str = this.hitFiles;
        return iHashCode + (str == null ? 0 : str.hashCode());
    }

    public String toString() {
        return "ResultReportData(threatLevel=" + this.threatLevel + ", threatName=" + this.threatName + ", malwareCodeFeature=" + this.malwareCodeFeature + ", version=" + this.version + ", packageName=" + this.packageName + ", appName=" + this.appName + ", versionCode=" + this.versionCode + ", apkCertificateMD5=" + this.apkCertificateMD5 + ", apkCertificateSHA256=" + this.apkCertificateSHA256 + ", mfFileSHA256=" + this.mfFileSHA256 + ", apkMd5=" + this.apkMd5 + ", apkSha256=" + this.apkSha256 + ", miuiVersion=" + this.miuiVersion + ", androidVersion=" + this.androidVersion + ", device=" + this.device + ", uuid=" + this.uuid + ", hitFiles=" + this.hitFiles + ")";
    }
}
