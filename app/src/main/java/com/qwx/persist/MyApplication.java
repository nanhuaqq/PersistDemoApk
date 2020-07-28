package com.qwx.persist;

import android.app.Application;

import com.qwx.persist.utils.CrashHandlerUtils;

import java.io.File;

/**
 * Created by qqin on 2020/7/28
 * <p>
 * email qqin@finbtc.net
 */
public class MyApplication extends Application {
    public static final String CACHE_PATH = "persist";

    @Override
    public void onCreate() {
        super.onCreate();

        File file = new File(CrashHandlerUtils.getDiskCacheDir(this, CACHE_PATH));
        if (!file.exists()) {
            file.mkdir();
        }

    }
}
