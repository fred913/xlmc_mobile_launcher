package com.aof.mcinabox;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;

public class XcMinecraftApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FileDownloader.setupOnApplicationOnCreate(XcMinecraftApplication.this);
    }
}
