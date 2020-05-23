package com.aof.mcinabox.utils.downloader;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import androidx.annotation.Nullable;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.DownloadTask;
import com.liulishuo.filedownloader.FileDownloader;

import com.aof.mcinabox.MainActivity;


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

    public static void downloadWithProgressDialog(Context con, String Url, String filePath, String tip) {
        // 定义一个dialog为Activity的成员变量
        ProgressDialog mProgressDialog;

// 在OnCreate()方法里面初始化
        mProgressDialog = new ProgressDialog(con);
        mProgressDialog.setMessage(tip);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

// 执行下载器
        final DownloadTask downloadTask = new DownloadTask(Url);
        downloadTask.setPath(filePath);
        downloadTask.start();

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                downloadTask.cancel();
            }
        });
    }

}
