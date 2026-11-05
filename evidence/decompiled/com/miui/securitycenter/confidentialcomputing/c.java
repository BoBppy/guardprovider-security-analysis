package com.miui.securitycenter.confidentialcomputing;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.onetrack.api.ah;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Context f13335a;

    class a implements b {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        final /* synthetic */ String f13336b;

        a(c cVar, String str) {
            this.f13336b = str;
        }

        private static ParcelFileDescriptor e(byte[] bArr) throws IOException {
            File fileCreateTempFile = File.createTempFile("pipe", null);
            Files.write(fileCreateTempFile.toPath(), bArr, StandardOpenOption.DELETE_ON_CLOSE, StandardOpenOption.WRITE);
            return ParcelFileDescriptor.open(fileCreateTempFile, 268435456);
        }

        private byte[] f(byte[] bArr, String str) throws IOException {
            byte[] bArr2;
            N1.a.b bVar = new N1.a.b();
            bVar.f4445a = false;
            bVar.f4446b = bArr;
            N1.a.c cVarC = N1.a.c(str, bVar);
            if (cVarC == null || (bArr2 = cVarC.f4452e) == null) {
                throw new IOException("Returned value is null.");
            }
            return bArr2;
        }

        private byte[] h(byte[] bArr, String str) throws IOException {
            N1.a.b bVar = new N1.a.b();
            bVar.f4445a = true;
            bVar.f4447c = e(bArr);
            N1.a.c cVarC = N1.a.c(str, bVar);
            if (cVarC == null || cVarC.f4453f == null) {
                throw new IOException("Returned value is null.");
            }
            ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(cVarC.f4453f);
            try {
                byte[] allBytes = autoCloseInputStream.readAllBytes();
                autoCloseInputStream.close();
                return allBytes;
            } catch (Throwable th) {
                try {
                    autoCloseInputStream.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
                throw th;
            }
        }

        private EncryptedData m(byte[] bArr, String str) throws IOException {
            byte[] bArr2;
            N1.a.b bVar = new N1.a.b();
            bVar.f4445a = false;
            bVar.f4446b = bArr;
            N1.a.c cVarD = N1.a.d(str, bVar);
            if (cVarD == null || (bArr2 = cVarD.f4452e) == null) {
                throw new IOException("Returned value is null.");
            }
            try {
                return EncryptedData.createInstanceFrom(bArr2);
            } catch (JSONException e6) {
                throw new IOException(e6);
            }
        }

        private EncryptedData o(byte[] bArr, String str) throws IOException {
            N1.a.b bVar = new N1.a.b();
            bVar.f4445a = true;
            bVar.f4447c = e(bArr);
            N1.a.c cVarD = N1.a.d(str, bVar);
            if (cVarD == null || cVarD.f4453f == null) {
                throw new IOException("Returned value is null.");
            }
            try {
                ParcelFileDescriptor.AutoCloseInputStream autoCloseInputStream = new ParcelFileDescriptor.AutoCloseInputStream(cVarD.f4453f);
                try {
                    EncryptedData encryptedDataCreateInstanceFrom = EncryptedData.createInstanceFrom(autoCloseInputStream.readAllBytes());
                    autoCloseInputStream.close();
                    return encryptedDataCreateInstanceFrom;
                } catch (Throwable th) {
                    try {
                        autoCloseInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (JSONException e6) {
                throw new IOException(e6);
            }
        }

        @Override // com.miui.securitycenter.confidentialcomputing.c.b
        public EncryptedData a(byte[] bArr) throws com.miui.securitycenter.confidentialcomputing.a {
            Objects.requireNonNull(bArr);
            try {
                return bArr.length < 1048576 ? m(bArr, this.f13336b) : o(bArr, this.f13336b);
            } catch (Exception e6) {
                throw new com.miui.securitycenter.confidentialcomputing.a(e6);
            }
        }

        @Override // java.lang.AutoCloseable
        public void close() {
            int iB = N1.a.b(this.f13336b);
            if (iB != 0) {
                Log.w("CCL", "Fail to pcc close,error code:" + iB);
            }
        }

        @Override // com.miui.securitycenter.confidentialcomputing.c.b
        public byte[] n(EncryptedData encryptedData) throws com.miui.securitycenter.confidentialcomputing.a {
            Objects.requireNonNull(encryptedData);
            try {
                byte[] bytes = encryptedData.toJson().getBytes(StandardCharsets.UTF_8);
                return bytes.length < 1048576 ? f(bytes, this.f13336b) : h(bytes, this.f13336b);
            } catch (Exception e6) {
                throw new com.miui.securitycenter.confidentialcomputing.a(e6);
            }
        }
    }

    public interface b extends AutoCloseable {
        EncryptedData a(byte[] bArr);

        byte[] n(EncryptedData encryptedData);
    }

    public c(Context context) {
        this.f13335a = context;
        N1.a.a(context);
    }

    public b a(boolean z6) throws com.miui.securitycenter.confidentialcomputing.a {
        N1.a.C0060a c0060a = new N1.a.C0060a();
        c0060a.f4443b = "https://detection-aigc.engine.miui.com";
        c0060a.f4444c = "157a2c2f09b3ba4d31fbb21fadb4d36f";
        c0060a.f4442a = z6;
        String strE = N1.a.e(c0060a);
        if (TextUtils.isEmpty(strE)) {
            throw new com.miui.securitycenter.confidentialcomputing.a("Null session result");
        }
        try {
            JSONObject jSONObject = new JSONObject(strE);
            int iOptInt = jSONObject.optInt(ah.f13634K);
            if (iOptInt == 0) {
                return new a(this, jSONObject.optString("session_id"));
            }
            throw new com.miui.securitycenter.confidentialcomputing.a("Session error: " + iOptInt);
        } catch (JSONException e6) {
            throw new com.miui.securitycenter.confidentialcomputing.a("Unexpected json string:", e6);
        }
    }
}
