package com.sollian.bottomsheetdialog;

import android.app.Application;

import com.sollian.library.Util;

/**
 * @author admin on 2018/3/20.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Util.init(this);
    }
}
