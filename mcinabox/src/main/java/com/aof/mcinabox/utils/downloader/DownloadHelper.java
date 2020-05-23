package com.aof.mcinabox.utils.downloader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.Nullable;

import com.aof.mcinabox.MainActivity;
import com.aof.mcinabox.launcher.uis.LauncherSettingUI;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.DownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import static com.aof.sharedmodule.Data.DataPathManifest.MCINABOX_TEMP;


public class DownloadHelper {

    public static BaseDownloadTask createDownloadTask(String filepath, String url, @Nullable Integer tag) {
        Log.e("DownloadHelper", "Url: " + url + " Filepath: " + filepath);
        if (tag == null) {
            //filepath是下载文件的绝对路径而不是目录
            return FileDownloader.getImpl().create(url).setPath(filepath);
        } else {
            return FileDownloader.getImpl().create(url).setPath(filepath).setTag(tag);
        }
    }

    public static BaseDownloadTask createDownloadTask(String filename, String dirpath, String url, @Nullable Integer tag) {
        return createDownloadTask(dirpath + "/" + filename, url, tag);
    }

    public static void cancleAllDownloadTask() {
        FileDownloader.getImpl().pauseAll();
    }

    public static void downloadWithProgressDialog(final Context con, final String Url, String filePath, String tip, final LauncherSettingUI con2) {
        // 定义一个dialog为Activity的成员变量
        final ProgressDialog mProgressDialog;

        // 在OnCreate()方法里面初始化
        mProgressDialog = new ProgressDialog(con);
        mProgressDialog.setMessage(tip);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        // 执行下载器
        final DownloadTask downloadTask = (DownloadTask) FileDownloader.getImpl().create(Url);
        downloadTask.setPath(filePath);
        downloadTask.setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                mProgressDialog.setMax(totalBytes);
                mProgressDialog.setProgress(soFarBytes);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                mProgressDialog.dismiss();
                con2.installRuntimeFromPath(MCINABOX_TEMP + "/env.tar.xz");
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {

            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }


        });
        downloadTask.start();

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel();
            }
        });
        mProgressDialog.show();
    }

}
