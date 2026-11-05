#!/usr/bin/env python3
"""
sign_request.py -- offline reproduction of the com.miui.guardprovider request signature.

Scheme (p146w1/b.java):

    params = {device, carrier, region, miuiVersion, versionType, appVersion, mi, ...}
    canonical = "&".join(f"{k}={v}" for k in sorted(params)) + "&" + SALT
    sign = MD5_hex_upper( base64(canonical) )
    POST body = form-urlencoded(params + sign)

The SALT is a constant compiled into the APK, so any holder of the APK can produce
a valid signature. This script prints the canonical string and the digest for a
synthetic device; it does not open a socket.

Usage:
    python sign_request.py
    python sign_request.py --android-id 0123456789abcdef
"""

import argparse
import base64
import hashlib

SALT = "94e6cec2-19dc-4bb6-823c-613f72d3ea16"  # hardcoded in p152x1/d.java call sites


def md5_upper(value: bytes) -> str:
    return hashlib.md5(value).hexdigest().upper()


def device_token(android_id: str) -> str:
    """p146w1/b.java: h() = MD5(Settings.Secure.ANDROID_ID + '-' + salt), upper MD5 again."""
    inner = md5_upper(f"{android_id}-{SALT}".encode())
    return md5_upper(inner.encode())


def sign(params: dict) -> tuple[str, str]:
    canonical = "&".join(f"{k}={params[k]}" for k in sorted(params)) + "&" + SALT
    b64 = base64.b64encode(canonical.encode()).decode()
    return canonical, hashlib.md5(b64.encode()).hexdigest().upper()


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("--android-id", default="0123456789abcdef")
    args = ap.parse_args()

    params = {
        "device": "dagu",
        "carrier": "unknown",
        "region": "cn",
        "miuiVersion": "MIUI-V816.0.1.0",
        "versionType": "stable",
        "appVersion": "1",
        "version": "1",
        "updateType": "incremental",
        "mi": device_token(args.android_id),
    }

    canonical, digest = sign(params)
    print("canonical string:")
    print("  " + canonical)
    print("sign:", digest)
    print()
    print("Interpretation: possession of the APK is sufficient to compute this value.")
    print("No request was transmitted.")


if __name__ == "__main__":
    main()
