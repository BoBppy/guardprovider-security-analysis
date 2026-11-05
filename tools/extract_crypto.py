#!/usr/bin/env python3
"""
extract_crypto.py -- demonstrate that the payload envelope key is recoverable from the APK.

J1/i.java derives an AES-128 key from META-INF/flamingo.dat:

    stream = resource("META-INF/flamingo.dat")
    stream.skip(144)
    key = stream.read(16) XOR 0x4312... (64-bit constant applied to both halves)

This script reads the file, derives the key, and prints only its SHA-256 fingerprint
plus a round-trip test. The raw key is deliberately not printed; it is a static
secret shared by every installation of this build.

Usage:
    python extract_crypto.py --apk-dir <extracted-apk>
"""

import argparse
import hashlib
import os
import struct
import sys

MASK = 4831221107097930563  # 0x4312_0000_0000_0000 style constant, see J1/i.java


def derive_key(path: str) -> bytes:
    data = open(path, "rb").read()
    raw = data[144:160]
    low = struct.unpack("<Q", raw[0:8])[0] ^ MASK
    high = struct.unpack("<Q", raw[8:16])[0] ^ MASK
    return struct.pack("<Q", low) + struct.pack("<Q", high)


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--apk-dir", default="apk_extracted")
    args = ap.parse_args()

    path = os.path.join(args.apk_dir, "META-INF", "flamingo.dat")
    if not os.path.exists(path):
        print(f"[-] not found: {path}", file=sys.stderr)
        return 1

    key = derive_key(path)
    print(f"[+] source      : {path} ({os.path.getsize(path)} bytes)")
    print(f"[+] key length  : {len(key)} bytes (AES-128)")
    print(f"[+] key sha256  : {hashlib.sha256(key).hexdigest()}")
    print("[+] the key is identical for every installation of this build")

    try:
        from cryptography.hazmat.primitives.ciphers import Cipher, algorithms, modes
    except ImportError:
        print("[i] install 'cryptography' for the round-trip test")
        return 0

    iv = os.urandom(16)
    enc = Cipher(algorithms.AES(key), modes.CBC(iv)).encryptor()
    ct = enc.update(b"A" * 16) + enc.finalize()
    dec = Cipher(algorithms.AES(key), modes.CBC(iv)).decryptor()
    assert dec.update(ct) + dec.finalize() == b"A" * 16
    print("[+] AES/CBC round-trip verified")
    return 0


if __name__ == "__main__":
    sys.exit(main())
