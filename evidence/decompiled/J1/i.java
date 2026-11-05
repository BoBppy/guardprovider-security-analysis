package J1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import p045g2.AbstractC0955j;
import p135u2.m;

/* JADX INFO: loaded from: classes.dex */
public interface i {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final a f3234a = a.f3235b;

    public static final class a implements i {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        static final /* synthetic */ a f3235b = new a();

        private a() {
        }

        @Override // J1.i
        public /* bridge */ byte[] a(byte[] bArr) {
            return super.a(bArr);
        }

        @Override // J1.i
        public /* bridge */ byte[] b(byte[] bArr) {
            return super.b(bArr);
        }

        @Override // J1.i
        public /* bridge */ Key c() {
            return super.c();
        }

        @Override // J1.i
        public /* bridge */ String d() {
            return super.d();
        }
    }

    default byte[] a(byte[] bArr) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, InvalidAlgorithmParameterException {
        m.e(bArr, "bytes");
        byte[] bArrGenerateSeed = new SecureRandom().generateSeed(16);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, c(), new IvParameterSpec(bArrGenerateSeed));
        m.b(bArrGenerateSeed);
        CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(bArr), cipher);
        try {
            byte[] bArrC = p111q2.a.c(cipherInputStream);
            p111q2.b.a(cipherInputStream, null);
            return AbstractC0955j.w(bArrGenerateSeed, bArrC);
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                p111q2.b.a(cipherInputStream, th);
                throw th2;
            }
        }
    }

    default byte[] b(byte[] bArr) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, InvalidAlgorithmParameterException {
        m.e(bArr, "bytes");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        Key keyC = c();
        byte[] bArrCopyOf = Arrays.copyOf(bArr, 16);
        m.d(bArrCopyOf, "copyOf(...)");
        cipher.init(2, keyC, new IvParameterSpec(bArrCopyOf));
        CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(AbstractC0955j.p(bArr, 16, bArr.length)), cipher);
        try {
            byte[] bArrC = p111q2.a.c(cipherInputStream);
            p111q2.b.a(cipherInputStream, null);
            return bArrC;
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                p111q2.b.a(cipherInputStream, th);
                throw th2;
            }
        }
    }

    default Key c() throws IOException {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(16);
        ClassLoader classLoader = i.class.getClassLoader();
        m.b(classLoader);
        InputStream resourceAsStream = classLoader.getResourceAsStream("META-INF/flamingo.dat");
        try {
            resourceAsStream.skip(144L);
            resourceAsStream.read(byteBufferAllocate.array());
            p111q2.b.a(resourceAsStream, null);
            byteBufferAllocate.putLong(0, byteBufferAllocate.getLong() ^ 4831221107097930563L);
            byteBufferAllocate.putLong(8, byteBufferAllocate.getLong() ^ 4831221107097930563L);
            return new SecretKeySpec(byteBufferAllocate.array(), "AES");
        } catch (Throwable th) {
            try {
                throw th;
            } catch (Throwable th2) {
                p111q2.b.a(resourceAsStream, th);
                throw th2;
            }
        }
    }

    default String d() {
        return "0";
    }
}
