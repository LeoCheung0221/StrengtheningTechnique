package com.tufusi.st.stlib;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * File: Zip.java
 * Author: leocheung
 * Version: V100R001C01
 * Create: 2020/6/22 6:43 AM
 * Description: 解压缩工具
 * <p>
 * Changes (from 2020/6/22)
 * -----------------------------------------------------------------
 * 2020/6/22 : Create Zip.java (leocheung);
 * -----------------------------------------------------------------
 */
public class Zip {

    /**
     * 压缩
     */
    public static void zip(File dir, File zip) throws IOException {
        if (zip.exists()) {
            zip.delete();
        }
        //对输出文件做CRC32校验
        CheckedOutputStream cos = new CheckedOutputStream(new FileOutputStream(zip), new CRC32());
        ZipOutputStream zos = new ZipOutputStream(cos);
        compress(dir, zos, "");
        zos.flush();
        zos.close();
    }


    /**
     * 解压
     */
    public static void unZip(File zip, File dir) {
        try {
            dir.delete();
            ZipFile zipFile = new ZipFile(zip);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry zipEntry = entries.nextElement();
                String name = zipEntry.getName();
                //如果文件名称包含签名文件，则过滤，加壳后的签名要重新签
                if (name.equals("META_INF/CERT.RSA") || name.equals("META_INF/CERT.SF") || name.
                        equals("META_INF/MANIFEST.MF")) {
                    continue;
                }
                //不是目录文件，那么就是dex文件，可以进行操作 解压到指定目录
                if (!zipEntry.isDirectory()) {
                    File file = new File(dir, name);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    InputStream is = zipFile.getInputStream(zipEntry);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    is.close();
                    fos.close();
                }
            }
            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void compress(File srcFile, ZipOutputStream zos, String basePath) throws IOException {
        if (srcFile.isDirectory()) {
            compressDir(srcFile, zos, basePath);
        } else {
            compressFile(srcFile, zos, basePath);
        }
    }

    /**
     * 压缩目录
     *
     * @param dir      源文件
     * @param zos      压缩输出流 写入到压缩文件
     * @param basePath 输出路径
     */
    private static void compressDir(File dir, ZipOutputStream zos, String basePath) throws IOException {
        File[] files = dir.listFiles();
        //构建空目录
        if (files.length < 1) {
            ZipEntry entry = new ZipEntry(basePath + dir.getName() + "/");
            zos.putNextEntry(entry);
            zos.closeEntry();
        }
        for (File file : files) {
            //递归压缩
            compress(file, zos, basePath + dir.getName() + "/");
        }
    }

    /**
     * 压缩文件
     *
     * @param file 源文件
     * @param zos  压缩输出流 写入到压缩文件
     * @param dir  输出路径
     */
    private static void compressFile(File file, ZipOutputStream zos, String dir) throws IOException {
        String dirName = dir + file.getName();
        String[] dirNameNew = dirName.split("/");
        StringBuffer buffer = new StringBuffer();
        if (dirNameNew.length > 1) {
            for (String cdir : dirNameNew) {
                buffer.append("/");
                buffer.append(cdir);
            }
        } else {
            buffer.append("/");
        }

        ZipEntry entry = new ZipEntry(buffer.toString().substring(1));
        zos.putNextEntry(entry);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(
                file
        ));
        int count;
        byte data[] = new byte[1024];
        while ((count = bis.read(data, 0, 1024)) != -1) {
            zos.write(data, 0, count);
        }
        bis.close();
        zos.closeEntry();
    }
}
