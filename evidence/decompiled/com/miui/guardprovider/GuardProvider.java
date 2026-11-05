package com.miui.guardprovider;

import C1.g;
import L1.e;
import L1.h;
import L1.i;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import miui.securitycenter.utils.SecurityCenterHelper;
import org.json.JSONArray;
import org.json.JSONObject;
import p051h1.f;
import p152x1.d;
import p152x1.m;

/* JADX INFO: loaded from: classes.dex */
public class GuardProvider extends ContentProvider {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final UriMatcher f12415d;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private h f12416b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Context f12417c;

    class a extends D1.a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ b f12418a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ CountDownLatch f12419b;

        a(b bVar, CountDownLatch countDownLatch) {
            this.f12418a = bVar;
            this.f12419b = countDownLatch;
        }

        @Override // D1.a, p051h1.c
        public void b0(int i6, f[] fVarArr) {
            if (fVarArr != null && fVarArr.length > 0) {
                this.f12418a.f12421a = fVarArr[0];
            }
            C1.h.a(GuardProvider.this.getContext(), this.f12418a.f12421a);
            this.f12419b.countDown();
        }
    }

    public static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        f f12421a = null;
    }

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        f12415d = uriMatcher;
        uriMatcher.addURI("guard", "engine_info_list", 1005);
        uriMatcher.addURI("guard", "network_set", 1010);
        uriMatcher.addURI("guard", "sync_scan", 1006);
        uriMatcher.addURI("guard", "virus", 1007);
        uriMatcher.addURI("guard", "install_scan", 1008);
        uriMatcher.addURI("guard", "support_incremental", 1009);
    }

    private Bitmap a(Drawable drawable) {
        Bitmap bitmapCreateBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapCreateBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmapCreateBitmap;
    }

    private Bundle b() throws Throwable {
        JSONObject jSONObjectC;
        Bundle bundle = new Bundle();
        boolean z6 = false;
        List<PackageInfo> installedPackages = getContext().getPackageManager().getInstalledPackages(0);
        List listE = p116r1.b.f(getContext()).e();
        JSONArray jSONArray = new JSONArray();
        for (PackageInfo packageInfo : installedPackages) {
            if ((packageInfo.applicationInfo.flags & 1) != 1 && (jSONObjectC = c(packageInfo, listE)) != null) {
                if (!z6) {
                    Log.i(" GuardProvider", "updatewhite when has gray app");
                    d.g(this.f12417c);
                    z6 = true;
                }
                if (p152x1.f.a(this.f12417c).e(packageInfo)) {
                    Log.i(" GuardProvider", "white package delete from resultdb " + packageInfo.packageName);
                    p116r1.b.f(this.f12417c).b(packageInfo.packageName);
                } else {
                    jSONArray.put(jSONObjectC);
                }
            }
        }
        bundle.putString("riskData", jSONArray.toString());
        return bundle;
    }

    private JSONObject c(PackageInfo packageInfo, List list) {
        JSONObject jSONObject;
        Iterator it = list.iterator();
        while (true) {
            jSONObject = null;
            if (!it.hasNext()) {
                break;
            }
            m mVar = (m) it.next();
            if (packageInfo.packageName.equals(mVar.f24782c) && packageInfo.versionCode == mVar.f24781b) {
                try {
                    JSONObject jSONObject2 = new JSONObject();
                    try {
                        jSONObject2.put("packageName", mVar.f24782c);
                        jSONObject2.put("versionCode", mVar.f24781b);
                        jSONObject2.put("type", mVar.f24780a);
                        jSONObject2.put("signaturesMd5", mVar.f24786g);
                        return jSONObject2;
                    } catch (Exception e6) {
                        e = e6;
                        jSONObject = jSONObject2;
                        Log.e(" GuardProvider", "getRiskData json failed", e);
                        return jSONObject;
                    }
                } catch (Exception e7) {
                    e = e7;
                }
            }
        }
        return jSONObject;
    }

    private Bundle d(String str, Bundle bundle) {
        Log.d(" GuardProvider", "start parse apk : " + str);
        Context context = getContext();
        File dataDirectory = Environment.getDataDirectory();
        if (context.checkPermission("android.permission.READ_EXTERNAL_STORAGE", Process.myPid(), Process.myUid()) != 0 && !str.startsWith(dataDirectory.getAbsolutePath())) {
            e.a(context);
        }
        PackageManager packageManager = context.getPackageManager();
        int i6 = bundle != null ? bundle.getInt("flags", 0) : 0;
        PackageInfo packageInfoA = L1.d.a(context, str, (i6 & 64) != 0 ? 64 : 0);
        if (packageInfoA == null) {
            Log.d(" GuardProvider", "parse apk failed !");
            return null;
        }
        Bundle bundle2 = new Bundle();
        ApplicationInfo applicationInfo = packageInfoA.applicationInfo;
        if ((i6 & 128) != 0) {
            String label = SecurityCenterHelper.getLabel(context, applicationInfo.publicSourceDir, applicationInfo);
            Drawable applicationIcon = packageManager.getApplicationIcon(applicationInfo);
            if (applicationIcon instanceof BitmapDrawable) {
                bundle2.putParcelable("icon", ((BitmapDrawable) applicationIcon).getBitmap());
            } else {
                try {
                    bundle2.putParcelable("icon", a(applicationIcon));
                } catch (Exception unused) {
                }
            }
            bundle2.putString("label", label);
        }
        bundle2.putParcelable("pkgInfo", packageInfoA);
        return bundle2;
    }

    private MatrixCursor e(String str, boolean z6) {
        return f(str, z6, null);
    }

    /* JADX WARN: Code duplicated, block: B:45:0x00e5 A[EXC_TOP_SPLITTER, PHI: r6
  0x00e5: PHI (r6v2 java.nio.file.Path) = (r6v3 java.nio.file.Path), (r6v8 java.nio.file.Path) binds: [B:36:0x00f2, B:30:0x00e3] A[DONT_GENERATE, DONT_INLINE], SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:52:0x00f8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Code duplicated, block: B:54:? A[SYNTHETIC] */
    /* JADX WARN: Not initialized variable reg: 6, insn: 0x0062: MOVE (r3 I:??[OBJECT, ARRAY]) = (r6 I:??[OBJECT, ARRAY]), block:B:16:0x0061 */
    private MatrixCursor f(String str, boolean z6, String str2) throws Throwable {
        Throwable th;
        Throwable th2;
        Path path;
        Path path2;
        int iJ;
        String string = str;
        Path path3 = null;
        try {
            try {
                if (!string.startsWith("file://")) {
                    if (string.startsWith("content://")) {
                        Context context = getContext();
                        path = Paths.get(context.getCacheDir().getAbsolutePath(), "copy.apk");
                        try {
                            Files.copy(context.getContentResolver().openInputStream(Uri.parse(string)), path, StandardCopyOption.REPLACE_EXISTING);
                            string = path.toAbsolutePath().toString();
                        } catch (IOException | InterruptedException e6) {
                            th2 = e6;
                            Log.e(" GuardProvider", "syncScan: ", th2);
                            if (path != null) {
                                try {
                                    Files.deleteIfExists(path);
                                } catch (IOException e7) {
                                    Log.e(" GuardProvider", "syncScan: ", e7);
                                }
                            }
                        }
                    }
                    CountDownLatch countDownLatch = new CountDownLatch(1);
                    b bVar = new b();
                    iJ = g.y().J(new String[]{string}, new a(bVar, countDownLatch), z6, 2, str2);
                    countDownLatch.await(2L, TimeUnit.MINUTES);
                    if (iJ >= 0 || bVar.f12421a == null) {
                        if (path != null) {
                            Files.deleteIfExists(path);
                        }
                        return null;
                    }
                    MatrixCursor matrixCursor = new MatrixCursor(new String[]{"Type", "VirusName", "Description", "EngineName", "AdType", "AdName", "AdDescription", "Forbidden"});
                    Integer numValueOf = Integer.valueOf(bVar.f12421a.f15655l);
                    f fVar = bVar.f12421a;
                    String str3 = fVar.f15647d;
                    String str4 = fVar.f15648e;
                    String str5 = fVar.f15649f;
                    Integer numValueOf2 = Integer.valueOf(fVar.f15656m);
                    f fVar2 = bVar.f12421a;
                    matrixCursor.addRow(new Object[]{numValueOf, str3, str4, str5, numValueOf2, fVar2.f15651h, fVar2.f15652i, Integer.valueOf(fVar2.f15660q)});
                    if (path != null) {
                        try {
                            Files.deleteIfExists(path);
                            return matrixCursor;
                        } catch (IOException e8) {
                            Log.e(" GuardProvider", "syncScan: ", e8);
                        }
                    }
                    return matrixCursor;
                }
                string = Uri.parse(string).getPath();
                path = null;
                CountDownLatch countDownLatch2 = new CountDownLatch(1);
                b bVar2 = new b();
                iJ = g.y().J(new String[]{string}, new a(bVar2, countDownLatch2), z6, 2, str2);
                countDownLatch2.await(2L, TimeUnit.MINUTES);
                if (iJ >= 0) {
                }
                if (path != null) {
                    Files.deleteIfExists(path);
                }
            } catch (Throwable th3) {
                th = th3;
                path3 = path2;
                if (path3 != null) {
                    throw th;
                }
                try {
                    Files.deleteIfExists(path3);
                    throw th;
                } catch (IOException e9) {
                    Log.e(" GuardProvider", "syncScan: ", e9);
                    throw th;
                }
            }
        } catch (IOException | InterruptedException e10) {
            th2 = e10;
            path = null;
        } catch (Throwable th4) {
            th = th4;
            if (path3 != null) {
                throw th;
            }
            Files.deleteIfExists(path3);
            throw th;
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        if ("parseApk".equals(str)) {
            return d(str2, bundle);
        }
        return "getRiskAppData".equals(str) ? b() : super.call(str, str2, bundle);
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        SQLiteDatabase writableDatabase = this.f12416b.getWritableDatabase();
        if (f12415d.match(uri) != 1007) {
            return 0;
        }
        int iDelete = writableDatabase.delete("t_virus_white_list", str, strArr);
        if (iDelete > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            p116r1.d.g(getContext()).c(strArr);
        }
        return iDelete;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase writableDatabase = this.f12416b.getWritableDatabase();
        int iMatch = f12415d.match(uri);
        if (iMatch == 1007) {
            long jInsert = writableDatabase.insert("t_virus_white_list", "_id", contentValues);
            if (jInsert > 0) {
                Uri uriWithAppendedPath = Uri.withAppendedPath(uri, String.valueOf(jInsert));
                getContext().getContentResolver().notifyChange(uri, null);
                p116r1.d.g(getContext()).c(new String[]{(String) contentValues.get("dir_path")});
                return uriWithAppendedPath;
            }
        } else if (iMatch == 1010) {
            L1.f.a().g("network_status", contentValues.getAsInteger("network_status").intValue());
            return null;
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        Context context = getContext();
        this.f12417c = context;
        this.f12416b = i.a(context);
        return true;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        String[] strArr3;
        Cursor cursorQuery = null;
        switch (f12415d.match(uri)) {
            case 1005:
                MatrixCursor matrixCursor = new MatrixCursor(new String[]{"Name", "NameString", "DescriptionName", "DialogTitle", "DialogHeader", "DialogPolicy", "DialogCloudScan", "DialogSummary", "DialogUrl", "Enable", "SupportCloud"});
                for (p134u1.b bVar : (List) p044g1.a.g().stream().collect(Collectors.toUnmodifiableList())) {
                    Object[] objArr = new Object[11];
                    objArr[0] = bVar.f24047b;
                    System.arraycopy(this.f12417c.getResources().getStringArray(bVar.f24053h), 0, objArr, 1, 8);
                    objArr[9] = Integer.valueOf(g.y().v(bVar.f24047b) ? 1 : 0);
                    objArr[10] = Integer.valueOf(bVar.f24051f ? 1 : 0);
                    matrixCursor.addRow(objArr);
                }
                return matrixCursor;
            case 1006:
                if (strArr2 != null && strArr2.length == 2) {
                    return e(strArr2[0], Boolean.parseBoolean(strArr2[1]));
                }
                return null;
            case 1007:
                strArr3 = strArr2;
                cursorQuery = this.f12416b.getWritableDatabase().query("t_virus_white_list", strArr, str, strArr3, null, null, str2);
                return (strArr3 != null || strArr3.length < 3) ? cursorQuery : f(strArr3[0], Boolean.parseBoolean(strArr3[1]), strArr3[2]);
            case 1008:
                strArr3 = strArr2;
                if (strArr3 != null) {
                }
                break;
            case 1009:
                if (p152x1.h.h(getContext())) {
                    MatrixCursor matrixCursor2 = new MatrixCursor(new String[]{"support"});
                    matrixCursor2.addRow(new Object[]{1});
                    return matrixCursor2;
                }
                return null;
            case 1010:
                MatrixCursor matrixCursor3 = new MatrixCursor(new String[]{"network_status"});
                matrixCursor3.addRow(new Integer[]{Integer.valueOf(L1.f.a().d("network_status", 0))});
                return matrixCursor3;
            default:
                return null;
        }
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }
}
