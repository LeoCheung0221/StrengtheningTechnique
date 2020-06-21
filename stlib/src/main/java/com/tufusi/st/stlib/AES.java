package com.tufusi.st.stlib;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * File: AES.java
 * Author: leocheung
 * Version: V100R001C01
 * Create: 2020/6/22 6:43 AM
 * Description: AES对称加密工具类
 * <p>
 * Changes (from 2020/6/22)
 * -----------------------------------------------------------------
 * 2020/6/22 : Create AES.java (leocheung);
 * -----------------------------------------------------------------
 */
public class AES {

    /**
     * 默认密码
     */
    public static final String DEFAULT_PWD = "12345678900987654321abcdefghijklmnopq";

    /**
     * 密钥算法
     */
    public static final String ALGORITHM = "AES";

    /**
     * 加解密算法/工作模式/填充方法
     */
    public static final String ALGORITHM_STR = "AES/ECB/PKCS5Padding";

    /**
     * 密码器
     */
    private static Cipher encryptCipher;

    /**
     * 解码器
     */
    private static Cipher decryptCipher;


    /**
     * 初始化
     */
    public static void init(String password) {
        //生成一个实现指定转换的 Cipher 对象
        try {
            encryptCipher = Cipher.getInstance(ALGORITHM_STR);
            decryptCipher = Cipher.getInstance(ALGORITHM_STR);

            byte[] keyStr = password.getBytes();

            SecretKeySpec keySpec = new SecretKeySpec(keyStr, ALGORITHM);
            encryptCipher.init(Cipher.ENCRYPT_MODE, keySpec);
            decryptCipher.init(Cipher.DECRYPT_MODE, keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加密
     */
    public static byte[] encrypt(byte[] content) {
        try {
            byte[] result = encryptCipher.doFinal(content);
            return result;
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     */
    public static byte[] decrypt(byte[] content) {
        try {
            byte[] result = decryptCipher.doFinal(content);
            return result;
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws Exception {

        File zip = new File("/Users/xiang/develop/app-debug.apk");
        String absolutePath = zip.getAbsolutePath();
        File dir = new File(absolutePath.substring(0, absolutePath.lastIndexOf(".")));
        Zip.unZip(zip,dir);

        File zip2 = new File("/Users/xiang/develop/app-debug2.apk");
        Zip.zip(dir,zip2);

        String[] argv = {
                "jarsigner","-verbose", "-sigalg", "MD5withRSA",
                "-digestalg", "SHA1",
                "-keystore", "/Users/xiang/develop/debug.keystore",
                "-storepass","android",
                "-keypass", "android",
                "-signedjar", "/Users/xiang/develop/app-debug2-sign.apk",
                "/Users/xiang/develop/app-debug2.apk",
                "androiddebugkey"
        };
        Process pro = null;
        try {
            pro = Runtime.getRuntime().exec(argv);
            //destroy the stream
            try {
                pro.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            if (pro != null) {
                pro.destroy();
            }
        }
    }
}
