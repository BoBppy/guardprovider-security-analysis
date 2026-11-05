package com.miui.guardprovider.aiguard;

import android.content.Context;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import p038f2.D;
import p135u2.AbstractC1069h;
import p135u2.m;

/* JADX INFO: loaded from: classes.dex */
public final class i {

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final a f12542d = new a(null);

    /* JADX INFO: renamed from: e, reason: collision with root package name */
    private static volatile i f12543e;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Path f12544a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final StringBuffer f12545b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final Path f12546c;

    public static final class a {
        public /* synthetic */ a(AbstractC1069h abstractC1069h) {
            this();
        }

        public final Path a(Context context, String str) {
            m.e(context, "context");
            m.e(str, "filename");
            Path path = Paths.get(context.getFilesDir().toString(), str);
            m.d(path, "get(...)");
            return path;
        }

        public final i b(Context context) {
            m.e(context, "context");
            if (i.f12543e == null) {
                synchronized (i.class) {
                    try {
                        if (i.f12543e == null) {
                            Context applicationContext = context.getApplicationContext();
                            m.d(applicationContext, "getApplicationContext(...)");
                            i.f12543e = new i(applicationContext, null);
                        }
                        D d6 = D.f14789a;
                    } catch (Throwable th) {
                        throw th;
                    }
                }
            }
            i iVar = i.f12543e;
            m.b(iVar);
            return iVar;
        }

        private a() {
        }
    }

    public /* synthetic */ i(Context context, AbstractC1069h abstractC1069h) {
        this(context);
    }

    public static final i h(Context context) {
        return f12542d.b(context);
    }

    public final void c(String str) {
        m.e(str, "text");
        this.f12545b.append(str);
    }

    public final void d(byte[] bArr) throws IOException {
        m.e(bArr, "audio");
        Files.write(this.f12544a, bArr, (OpenOption[]) Arrays.copyOf(new OpenOption[]{StandardOpenOption.WRITE, StandardOpenOption.CREATE}, 2));
    }

    public final void e(Path path) {
        m.e(path, "clip");
        m.d(Files.copy(path, this.f12546c, (CopyOption[]) Arrays.copyOf(new CopyOption[]{StandardCopyOption.REPLACE_EXISTING}, 1)), "copy(...)");
    }

    public final void f() throws IOException {
        Files.deleteIfExists(this.f12544a);
        Files.deleteIfExists(this.f12546c);
    }

    public final StringBuffer g() {
        return this.f12545b;
    }

    public final Path i() {
        return this.f12544a;
    }

    public final Path j() {
        return this.f12546c;
    }

    private i(Context context) {
        a aVar = f12542d;
        this.f12544a = aVar.a(context, "feedback01.tmp");
        this.f12545b = new StringBuffer();
        this.f12546c = aVar.a(context, "clip01.tmp");
    }
}
