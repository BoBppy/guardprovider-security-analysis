package com.miui.securitycenter.confidentialcomputing;

import java.util.Locale;
import java.util.Objects;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public final class EncryptedData extends p024d2.a {
    private final String data_bytes;
    private final String digest;
    private final String key_id;
    private final String secure_channel_session_public_key;
    private final int version;

    public EncryptedData(int i6, String str, String str2, String str3, String str4) {
        this.version = i6;
        this.secure_channel_session_public_key = str;
        this.key_id = str2;
        this.digest = str3;
        this.data_bytes = str4;
    }

    private /* synthetic */ boolean a(Object obj) {
        if (!(obj instanceof EncryptedData)) {
            return false;
        }
        EncryptedData encryptedData = (EncryptedData) obj;
        return this.version == encryptedData.version && Objects.equals(this.secure_channel_session_public_key, encryptedData.secure_channel_session_public_key) && Objects.equals(this.key_id, encryptedData.key_id) && Objects.equals(this.digest, encryptedData.digest) && Objects.equals(this.data_bytes, encryptedData.data_bytes);
    }

    static EncryptedData createInstanceFrom(byte[] bArr) {
        Objects.requireNonNull(bArr);
        return createInstanceFrom(new JSONObject(new String(bArr)));
    }

    public String data_bytes() {
        return this.data_bytes;
    }

    public String digest() {
        return this.digest;
    }

    public final boolean equals(Object obj) {
        return a(obj);
    }

    public final int hashCode() {
        return d.a(this.version, this.secure_channel_session_public_key, this.key_id, this.digest, this.data_bytes);
    }

    public String key_id() {
        return this.key_id;
    }

    public String secure_channel_session_public_key() {
        return this.secure_channel_session_public_key;
    }

    public String toJson() {
        return String.format(Locale.getDefault(), "{\"version\":%d,\"secure_channel_session_public_key\":\"%s\", \"key_id\":\"%s\", \"digest\":\"%s\", \"data_bytes\":\"%s\"}\n", Integer.valueOf(this.version), this.secure_channel_session_public_key, this.key_id, this.digest, this.data_bytes);
    }

    public String toString() {
        return toJson();
    }

    public int version() {
        return this.version;
    }

    public static EncryptedData createInstanceFrom(JSONObject jSONObject) {
        Objects.requireNonNull(jSONObject);
        return createInstanceFrom(jSONObject.getInt("version"), jSONObject.getString("secure_channel_session_public_key"), jSONObject.getString("key_id"), jSONObject.getString("digest"), jSONObject.getString("data_bytes"));
    }

    public static EncryptedData createInstanceFrom(int i6, String str, String str2, String str3, String str4) {
        return new EncryptedData(i6, str, str2, str3, str4);
    }
}
