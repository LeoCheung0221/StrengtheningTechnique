package com.tufusi.st.stlib;

import android.app.Application;
import android.content.Context;

import java.io.File;

/**
 * File: ShellApplication.java
 * Author: leocheung
 * Version: V100R001C01
 * Create: 2020/6/22 6:42 AM
 * Description: 壳dex的启动程序 这也是为何使用Tinker的时候需要继承其Application的原因所在
 * <p>
 * Changes (from 2020/6/22)
 * -----------------------------------------------------------------
 * 2020/6/22 : Create ShellApplication.java (leocheung);
 * -----------------------------------------------------------------
 */
public class ShellApplication extends Application {

    public static String getPassword() {
        return AES.DEFAULT_PWD;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        AES.init(getPassword());

        File apkFile = new File(getApplicationInfo().sourceDir);
        //拿到 //data/data/包名/files/fake_apk/
        File unZipFile = getDir("fake_app", MODE_PRIVATE);
    }
}
