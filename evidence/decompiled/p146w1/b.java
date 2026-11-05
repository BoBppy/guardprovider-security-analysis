package p146w1;

import A4.c;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import com.miui.guardprovider.GuardApplication;
import com.xiaomi.onetrack.api.ah;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;
import p122s1.d;

/* JADX INFO: loaded from: classes.dex */
public abstract class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f24510a = d.a("ro.product.device", "unknown");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f24511b = d.a("ro.carrier.name", "unknown");

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final String f24512c = d.a("ro.product.mod_device", "unknown");

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final String f24513d = "MIUI-" + Build.VERSION.INCREMENTAL;

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static final String f24514e = String.valueOf(1);

    class a implements Comparator {
        a() {
        }

        @Override // java.util.Comparator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(String str, String str2) {
            return str.compareTo(str2);
        }
    }

    private static void a(HttpURLConnection httpURLConnection, String str) throws IOException {
        if (str != null) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.addRequestProperty("Content-Type", "application/json");
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.writeBytes(str);
            dataOutputStream.close();
        }
    }

    private static void b(HttpURLConnection httpURLConnection, Map map) throws IOException {
        byte[] bytes = (map == null || map.size() <= 0) ? null : e(map).getBytes();
        if (bytes != null) {
            httpURLConnection.setDoOutput(true);
            httpURLConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(bytes);
            dataOutputStream.close();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r2v1, types: [java.util.Map] */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v10 */
    /* JADX WARN: Type inference failed for: r3v3, types: [java.io.OutputStream] */
    public static String c(Context context, String str, Map map, String str2) throws Throwable {
        ?? r6;
        ?? r7;
        ByteArrayOutputStream byteArrayOutputStream;
        ?? r8 = 0;
        if (!GuardApplication.isAllowConnectNetwork(context) || TextUtils.isEmpty(str)) {
            return null;
        }
        ?? M5 = m(context, map, str2);
        try {
            try {
                HttpURLConnection httpURLConnectionN = n(str);
                httpURLConnectionN.setRequestMethod("POST");
                b(httpURLConnectionN, M5);
                if (httpURLConnectionN.getResponseCode() != 200) {
                    c.b(null);
                    c.c(null);
                    return null;
                }
                M5 = httpURLConnectionN.getInputStream();
                try {
                    byteArrayOutputStream = new ByteArrayOutputStream();
                    try {
                        byte[] bArr = new byte[4096];
                        while (true) {
                            int i6 = M5.read(bArr);
                            if (i6 == -1) {
                                String string = byteArrayOutputStream.toString();
                                c.b(M5);
                                c.c(byteArrayOutputStream);
                                return string;
                            }
                            byteArrayOutputStream.write(bArr, 0, i6);
                        }
                    } catch (Exception e6) {
                        e = e6;
                        r7 = M5;
                    }
                } catch (Exception e7) {
                    e = e7;
                    byteArrayOutputStream = null;
                    r7 = M5;
                } catch (Throwable th) {
                    th = th;
                    str = null;
                    r8 = M5;
                    r6 = str;
                    c.b(r8);
                    c.c(r6);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception e8) {
            e = e8;
            r7 = 0;
            byteArrayOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            r6 = 0;
            c.b(r8);
            c.c(r6);
            throw th;
        }
        Log.e("NetworkApiHelper", "exception when do post request", e);
        c.b(r7);
        c.c(byteArrayOutputStream);
        return null;
    }

    public static String d(Context context, String str, JSONObject jSONObject, String str2) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream;
        InputStream inputStream;
        InputStream inputStream2 = null;
        if (!GuardApplication.isAllowConnectNetwork(context) || TextUtils.isEmpty(str)) {
            return null;
        }
        String strI = i(l(jSONObject), str2);
        try {
            HttpURLConnection httpURLConnectionN = n(str);
            httpURLConnectionN.setRequestMethod("POST");
            jSONObject.put("sign", strI);
            a(httpURLConnectionN, jSONObject.toString());
            if (httpURLConnectionN.getResponseCode() != 200) {
                c.b(null);
                c.c(null);
                return null;
            }
            inputStream = httpURLConnectionN.getInputStream();
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    try {
                        byte[] bArr = new byte[4096];
                        while (true) {
                            int i6 = inputStream.read(bArr);
                            if (i6 == -1) {
                                String string = byteArrayOutputStream.toString();
                                c.b(inputStream);
                                c.c(byteArrayOutputStream);
                                return string;
                            }
                            byteArrayOutputStream.write(bArr, 0, i6);
                            th = th;
                            inputStream2 = inputStream;
                            c.b(inputStream2);
                            c.c(byteArrayOutputStream);
                            throw th;
                        }
                    } catch (Exception e6) {
                        e = e6;
                        Log.e("NetworkApiHelper", "exception when do post request", e);
                        c.b(inputStream);
                        c.c(byteArrayOutputStream);
                        return null;
                    }
                } catch (Throwable th) {
                    th = th;
                }
            } catch (Exception e7) {
                e = e7;
                byteArrayOutputStream = null;
            } catch (Throwable th2) {
                th = th2;
                byteArrayOutputStream = null;
            }
        } catch (Exception e8) {
            e = e8;
            inputStream = null;
            byteArrayOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            byteArrayOutputStream = null;
        }
        Log.e("NetworkApiHelper", "exception when do post request", e);
        c.b(inputStream);
        c.c(byteArrayOutputStream);
        return null;
    }

    private static String e(Map map) {
        StringBuilder sb = new StringBuilder();
        try {
            boolean z6 = true;
            for (String str : map.keySet()) {
                if (!z6) {
                    sb.append('&');
                }
                sb.append(URLEncoder.encode(str, "UTF-8"));
                sb.append('=');
                sb.append(URLEncoder.encode((String) map.get(str), "UTF-8"));
                z6 = false;
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e6) {
            throw new RuntimeException("Encoding not supported: " + ((Object) sb), e6);
        }
    }

    public static Map f(Context context, String str) {
        HashMap map = new HashMap();
        map.put("device", f24510a);
        map.put("carrier", f24511b);
        map.put("region", f24512c);
        map.put("miuiVersion", f24513d);
        map.put("versionType", k());
        map.put("appVersion", f24514e);
        map.put("mi", h(context, str));
        return map;
    }

    private static byte[] g(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException unused) {
            return str.getBytes();
        }
    }

    public static String h(Context context, String str) {
        String string;
        try {
            string = Settings.Secure.getString(context.getContentResolver(), ah.f13624A);
        } catch (Exception e6) {
            e6.printStackTrace();
            string = null;
        }
        if (TextUtils.isEmpty(string)) {
            return com.xiaomi.onetrack.util.a.f14247c;
        }
        return p069j5.a.a(p027d5.a.b(string + "-" + str, "MD5"));
    }

    public static String i(Map map, String str) {
        ArrayList<String> arrayList = new ArrayList(map.keySet());
        Collections.sort(arrayList, new a());
        StringBuilder sb = new StringBuilder();
        boolean z6 = true;
        for (String str2 : arrayList) {
            if (!z6) {
                sb.append("&");
            }
            sb.append(str2);
            sb.append("=");
            sb.append((String) map.get(str2));
            z6 = false;
        }
        sb.append("&");
        sb.append(str);
        return j(Base64.encodeToString(g(sb.toString()), 2));
    }

    private static String j(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(g(str));
            return String.format("%1$032X", new BigInteger(1, messageDigest.digest()));
        } catch (NoSuchAlgorithmException e6) {
            throw new RuntimeException(e6);
        }
    }

    public static String k() {
        if (miui.os.Build.IS_STABLE_VERSION) {
            return "stable";
        }
        return miui.os.Build.IS_DEVELOPMENT_VERSION ? "development" : "alpha";
    }

    public static Map l(JSONObject jSONObject) {
        HashMap map = new HashMap();
        Iterator<String> itKeys = jSONObject.keys();
        while (itKeys.hasNext()) {
            String next = itKeys.next();
            if (!next.equals("sign")) {
                if (next.equals("content")) {
                    map.put(next, jSONObject.optJSONArray(next).toString());
                } else {
                    map.put(next, jSONObject.optString(next));
                }
            }
        }
        return map;
    }

    public static Map m(Context context, Map map, String str) {
        Map mapF = f(context, str);
        mapF.putAll(map);
        mapF.put("sign", i(mapF, str));
        return mapF;
    }

    public static HttpURLConnection n(String str) {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
        httpURLConnection.setConnectTimeout(15000);
        httpURLConnection.setReadTimeout(15000);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoInput(true);
        return httpURLConnection;
    }
}
