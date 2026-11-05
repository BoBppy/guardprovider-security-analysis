package com.miui.guardprovider.aiguard.service;

import java.util.Objects;
import p024d2.a;
import p086m1.b;

/* JADX INFO: loaded from: classes.dex */
public final class EncryptedResponse extends a {
    private final int code;
    private final String data;
    private final String message;
    private final String traceId;

    public EncryptedResponse(int i6, String str, String str2, String str3) {
        this.code = i6;
        this.message = str;
        this.data = str2;
        this.traceId = str3;
    }

    private /* synthetic */ boolean a(Object obj) {
        if (!(obj instanceof EncryptedResponse)) {
            return false;
        }
        EncryptedResponse encryptedResponse = (EncryptedResponse) obj;
        return this.code == encryptedResponse.code && Objects.equals(this.message, encryptedResponse.message) && Objects.equals(this.data, encryptedResponse.data) && Objects.equals(this.traceId, encryptedResponse.traceId);
    }

    private /* synthetic */ Object[] b() {
        return new Object[]{Integer.valueOf(this.code), this.message, this.data, this.traceId};
    }

    public int code() {
        return this.code;
    }

    public String data() {
        return this.data;
    }

    public final boolean equals(Object obj) {
        return a(obj);
    }

    public final int hashCode() {
        return b.a(this.code, this.message, this.data, this.traceId);
    }

    public String message() {
        return this.message;
    }

    public final String toString() {
        return p086m1.a.a(b(), EncryptedResponse.class, "code;message;data;traceId");
    }

    public String traceId() {
        return this.traceId;
    }
}
