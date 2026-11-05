package p079l1;

import android.content.Context;
import android.media.projection.IMediaProjection;
import android.media.projection.IMediaProjectionManager;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.system.Os;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;

/* JADX INFO: loaded from: classes.dex */
abstract class d {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final VarHandle f17308a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final MethodHandle f17309b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final MethodHandle f17310c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static final boolean f17311d;

    static {
        boolean z6 = Build.VERSION.SDK_INT >= 36;
        f17311d = z6;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            f17308a = MethodHandles.privateLookupIn(MediaProjectionManager.class, lookup).findVarHandle(MediaProjectionManager.class, "mService", IMediaProjectionManager.class);
            Class cls = Boolean.TYPE;
            Class cls2 = Integer.TYPE;
            f17309b = lookup.findVirtual(IMediaProjectionManager.class, "createProjection", z6 ? MethodType.methodType(IMediaProjection.class, cls2, String.class, cls2, cls, cls2) : MethodType.methodType(IMediaProjection.class, cls2, String.class, cls2, cls));
            f17310c = lookup.findConstructor(MediaProjection.class, MethodType.methodType(Void.TYPE, Context.class, IMediaProjection.class));
        } catch (Throwable th) {
            throw new ExceptionInInitializerError(th);
        }
    }

    static MediaProjection a(Context context) {
        Object obj = (Object) f17308a.get((MediaProjectionManager) context.getSystemService(MediaProjectionManager.class));
        return (MediaProjection) f17310c.invoke(context, f17311d ? (Object) f17309b.invoke(obj, Os.getuid(), context.getPackageName(), 0, false, 0) : (Object) f17309b.invoke(obj, Os.getuid(), context.getPackageName(), 0, false));
    }
}
