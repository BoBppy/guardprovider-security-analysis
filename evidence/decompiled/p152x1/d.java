package p152x1;

import A4.c;
import L1.b;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.xiaomi.onetrack.api.ah;
import com.xiaomi.onetrack.b.m;
import com.xiaomi.onetrack.util.a;
import java.util.HashMap;
import java.util.Map;
import miui.os.Build;
import org.json.JSONArray;
import org.json.JSONObject;
import p140v1.h;

/* JADX INFO: loaded from: classes.dex */
public class d implements a {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static volatile d f24767c;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Context f24768a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private b f24769b;

    private d() {
    }

    public static synchronized d d() {
        try {
            if (f24767c == null) {
                f24767c = new d();
            }
        } catch (Throwable th) {
            throw th;
        }
        return f24767c;
    }

    private Map e(boolean z6) {
        c cVarC = c();
        int i6 = cVarC != null ? cVarC.f24765a : 0;
        String strB = Build.IS_INTERNATIONAL_BUILD ? b.b() : b.a(this.f24768a);
        HashMap map = new HashMap();
        map.put("version", String.valueOf(i6));
        map.put("updateType", z6 ? "incremental" : "global");
        map.put(m.f13917a, strB);
        return map;
    }

    private static Map f(Context context) {
        String strB = Build.IS_INTERNATIONAL_BUILD ? b.b() : b.a(context);
        HashMap map = new HashMap();
        map.put(m.f13917a, strB);
        return map;
    }

    public static void g(Context context) throws Throwable {
        String strC = p146w1.b.c(context, "https://api.sec.miui.com/antivirus/whitelist", f(context), "94e6cec2-19dc-4bb6-823c-613f72d3ea16");
        if (TextUtils.isEmpty(strC)) {
            return;
        }
        f.a(context).c(strC);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r2v3 */
    private c h() throws Throwable {
        SQLiteDatabase readableDatabase;
        Cursor cursorQuery;
        ?? r6 = 0;
        r6 = 0;
        try {
            try {
                readableDatabase = this.f24769b.getReadableDatabase();
                try {
                    cursorQuery = readableDatabase.query("antivirus_db_version", new String[]{"version", "release_time"}, null, null, null, null, null);
                    if (cursorQuery != null) {
                        try {
                            if (cursorQuery.moveToFirst()) {
                                int i6 = cursorQuery.getInt(cursorQuery.getColumnIndexOrThrow("version"));
                                long j6 = cursorQuery.getLong(cursorQuery.getColumnIndexOrThrow("release_time"));
                                c cVar = new c();
                                cVar.f24765a = i6;
                                cVar.f24766b = j6;
                                c.a(cursorQuery);
                                c.a(readableDatabase);
                                return cVar;
                            }
                        } catch (Exception e6) {
                            e = e6;
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e7) {
                    e = e7;
                    cursorQuery = null;
                } catch (Throwable th) {
                    th = th;
                    c.a(r6);
                    c.a(readableDatabase);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                r6 = this;
            }
        } catch (Exception e8) {
            e = e8;
            cursorQuery = null;
            readableDatabase = null;
        } catch (Throwable th3) {
            th = th3;
            readableDatabase = null;
        }
        c.a(cursorQuery);
        c.a(readableDatabase);
        return null;
    }

    private boolean i(String str) throws Throwable {
        boolean z6;
        Object obj;
        int i6 = 0;
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (jSONObject.has("error_code")) {
                return false;
            }
            SQLiteDatabase writableDatabase = this.f24769b.getWritableDatabase();
            writableDatabase.beginTransaction();
            try {
                try {
                    writableDatabase.delete("antivirus_db_version", null, null);
                    int i7 = jSONObject.getInt("version");
                    long j6 = jSONObject.getLong("releaseTime");
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("version", Integer.valueOf(i7));
                    contentValues.put("release_time", Long.valueOf(j6));
                    writableDatabase.insert("antivirus_db_version", null, contentValues);
                    if ("global".equals(jSONObject.getString("updateType"))) {
                        writableDatabase.delete("antivirus_db", null, null);
                        z6 = false;
                    } else {
                        z6 = true;
                    }
                    JSONArray jSONArray = jSONObject.getJSONArray("viruses");
                    int i8 = 0;
                    while (i8 < jSONArray.length()) {
                        JSONObject jSONObject2 = jSONArray.getJSONObject(i8);
                        String string = jSONObject2.getString("packageName");
                        int iOptInt = jSONObject2.optInt("version");
                        String strOptString = jSONObject2.optString("signature");
                        String strOptString2 = jSONObject2.optString("md5");
                        int i9 = jSONObject2.getInt("type");
                        int iOptInt2 = jSONObject2.optInt("strategy", i6);
                        int i10 = i6;
                        try {
                            int iOptInt3 = jSONObject2.optInt("descType", 1);
                            boolean z7 = z6;
                            String string2 = z7 ? jSONObject2.getString(ah.f13632I) : jSONObject2.optString(ah.f13632I, "new");
                            JSONArray jSONArray2 = jSONArray;
                            String strOptString3 = jSONObject2.optString("virusName");
                            int i11 = i8;
                            JSONObject jSONObjectOptJSONObject = jSONObject2.optJSONObject("i18n");
                            String[] strArr = {string, strOptString, strOptString2};
                            if ("new".equals(string2) || "updated".equals(string2)) {
                                ContentValues contentValues2 = new ContentValues();
                                contentValues2.put("package", string);
                                contentValues2.put("version", Integer.valueOf(iOptInt));
                                contentValues2.put("signatures", strOptString);
                                if (strOptString2 != null) {
                                    contentValues2.put("md5", strOptString2);
                                }
                                contentValues2.put("type", Integer.valueOf(i9));
                                contentValues2.put("virus_name", strOptString3);
                                contentValues2.put("strategy", Integer.valueOf(iOptInt2));
                                contentValues2.put("descType", Integer.valueOf(iOptInt3));
                                if (jSONObjectOptJSONObject != null) {
                                    contentValues2.put("description", jSONObjectOptJSONObject.toString());
                                } else {
                                    contentValues2.put("description", a.f14247c);
                                }
                                writableDatabase.delete("antivirus_db", "package =? and signatures =? and md5 =? ", strArr);
                                obj = null;
                                writableDatabase.insert("antivirus_db", null, contentValues2);
                            } else {
                                if ("deleted".equals(string2)) {
                                    writableDatabase.delete("antivirus_db", "package =? and signatures =? and md5 =? ", strArr);
                                }
                                obj = null;
                            }
                            i8 = i11 + 1;
                            i6 = i10;
                            z6 = z7;
                            jSONArray = jSONArray2;
                        } catch (Throwable th) {
                            th = th;
                            writableDatabase.endTransaction();
                            c.a(writableDatabase);
                            throw th;
                        }
                    }
                    writableDatabase.setTransactionSuccessful();
                    writableDatabase.endTransaction();
                    c.a(writableDatabase);
                    return true;
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception e6) {
                e = e6;
            }
        } catch (Exception e7) {
            e = e7;
        }
        e.printStackTrace();
        return false;
    }

    @Override // p152x1.a
    public j a() {
        return new k(this.f24768a, this.f24769b);
    }

    @Override // p152x1.a
    public void b(Context context) {
        this.f24768a = context.getApplicationContext();
        this.f24769b = new b(this.f24768a);
    }

    public c c() {
        return h();
    }

    public boolean j(boolean z6) throws Throwable {
        String strC = p146w1.b.c(this.f24768a, "https://api.sec.miui.com/antivirus/viruses", e(z6), "94e6cec2-19dc-4bb6-823c-613f72d3ea16");
        g(this.f24768a);
        h.e(this.f24768a);
        if (TextUtils.isEmpty(strC)) {
            return false;
        }
        return i(strC);
    }
}
