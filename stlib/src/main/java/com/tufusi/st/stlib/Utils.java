package com.tufusi.st.stlib;

/**
 * File: Utils.java
 * Author: leocheung
 * Version: V100R001C01
 * Create: 2020/6/22 6:43 AM
 * Description:
 * <p>
 * Changes (from 2020/6/22)
 * -----------------------------------------------------------------
 * 2020/6/22 : Create Utils.java (leocheung);
 * -----------------------------------------------------------------
 */
public class Utils {

    public static byte[] int2Bytes(int value) {
        byte[] src = new byte[4];
        src[3] = (byte) ((value >> 24) & 0xFF);
        src[2] = (byte) ((value >> 16) & 0xFF);
        src[1] = (byte) ((value >> 8) & 0xFF);
        src[0] = (byte) (value & 0xFF);
        return src;
    }

    public static int bytes2Int(byte[] src) {
        int value;
        value = (int) ((src[0] & 0xFF)
                | ((src[1] & 0xFF)<<8)
                | ((src[2] & 0xFF)<<16)
                | ((src[3] & 0xFF)<<24));
        return value;
    }
}
