package com.ckb.explorer.util;

public class ByteUtils {

    public static boolean hasLength(byte[] data) {
        if (data == null || data.length == 0) {
            return false;
        }
        return true;
    }
}
