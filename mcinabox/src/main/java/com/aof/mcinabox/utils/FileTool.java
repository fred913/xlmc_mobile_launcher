package com.aof.mcinabox.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class FileTool {

    /**
     * 【检查文件目录是否存在，不存在就创建新的目录】
     **/
    public static void checkFilePath(File file, boolean isDir) {
        if (file != null) {
            if (!isDir) {
                //如果是文件就返回父目录
                file = file.getParentFile();
            }
            if (file != null && !file.exists()) {
                file.mkdirs();
            }
        }
    }

    /**
     * 【删除文件】
     **/
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                boolean isSucess = file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
                boolean isSucess = file.delete();
            }
        }
    }

    /**
     * 【重写数据到文件】
     **/
    public static void writeData(String path, String fileData) {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(path);
                FileOutputStream out = new FileOutputStream(file, false);
                //将数据写入到文件中
                out.write(fileData.getBytes("UTF-8"));
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 【续写数据到文件】
     **/
    public static void writtenFileData(String path, String data) {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(path);
                //按读写方式
                RandomAccessFile raf = new RandomAccessFile(file, "rw");
                //将文件指针移到文件尾
                raf.seek(file.length());
                //将数据写入到文件中
                raf.write(data.getBytes("UTF-8"));
                raf.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 【读取文件内容】
     **/
    public static String readFileContent(String path) {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                File file = new File(path);
                byte[] buffer = new byte[32 * 1024];
                FileInputStream fis = new FileInputStream(file);
                int len = 0;
                StringBuffer sb = new StringBuffer("");
                while ((len = fis.read(buffer)) > 0) {
                    sb.append(new String(buffer, 0, len));
                }
                fis.close();
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 【判断文件是否存在】
     **/
    public static boolean isFileExists(String fileName) {
        File file = new File(fileName);
        return file.exists();
    }

    /**
     * 【判断文件夹是否存在】
     **/
    public static boolean isFolderExists(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        }
        File dire = new File(directoryPath);
        //如果是文件夹并且文件夹存在则返回true
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * 【获取文件夹名称】
     **/
    public static String getFolderName(String folderName) {
        if (TextUtils.isEmpty(folderName)) {
            return folderName;
        }
        int filePosi = folderName.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : folderName.substring(0, filePosi);
    }

    /**
     * 【重命名文件】
     **/
    public static boolean renameFile(String oldFileName, String newFileName) {
        File oldName = new File(oldFileName);
        File newName = new File(newFileName);
        return oldName.renameTo(newName);
    }

    /**
     * 【判断文件夹里是否有文件】
     **/
    public static boolean hasFileExists(String folderPath) {
        File file = new File(folderPath);
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 【复制文件】参数为：String
     **/
    public static int copyFile(String fromFile, String toFile) {
        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream outto = new FileOutputStream(toFile);
            byte[] bt = new byte[1024];
            int len = fosfrom.read(bt);
            if (len > 0) {
                outto.write(bt, 0, len);
            }
            fosfrom.close();
            outto.close();
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FileTool", "Copy Failed");
            return -1;
        }
    }

    public static void copyFile(String from, String to, Boolean rewrite) {
        File fromFile = new File(from);
        File toFile = new File(to);

        if (!fromFile.exists()) {
            return;
        }
        if (!fromFile.isFile()) {
            return;
        }
        if (!fromFile.canRead()) {
            return;
        }
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }
        if (toFile.exists() && rewrite) {
            toFile.delete();
        }

        try {
            java.io.FileInputStream fosfrom = new java.io.FileInputStream(fromFile);
            java.io.FileOutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                //将内容写到新文件当中
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();

        } catch (Exception ex) {
            Log.e("readfile", ex.getMessage());
        }

    }

    /**
     * 【复制文件夹】
     **/
    public static int copyDir(String fromFolder, String toFolder) {
        File[] currentFiles;
        File root = new File(fromFolder);
        if (!root.exists()) {
            return -1;
        }
        currentFiles = root.listFiles();
        File targetDir = new File(toFolder);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory()) {
                copyDir(currentFiles[i].getPath() + "/", currentFiles[i].getName() + "/");
            } else {
                copyFile(currentFiles[i].getPath(), toFolder + currentFiles[i].getName());
            }
        }
        return 0;
    }

    /**
     * 【获取文件夹下的所有文件夹名称(不包括子文件夹内)】
     **/
    public static ArrayList<String> listChildDirFromTargetDir(String targetFolder) {
        File folder = new File(targetFolder);
        ArrayList<String> dirsname = new ArrayList<String>();
        File[] dirs = folder.listFiles();
        for (File file : dirs) {
            if (!file.isFile()) {
                dirsname.add(file.getName());
            }
        }
        return dirsname;
    }

    /**
     * 【获取文件夹下的所有文件名称(不包括子文件夹内(不包括子文件夹内)】
     **/
    public static ArrayList<String> listChildFilesFromTargetDir(String targetFolder) {
        File folder = new File(targetFolder);
        ArrayList<String> filesname = new ArrayList<String>();
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                filesname.add(file.getName());
            }
        }
        return filesname;
    }

    /**
     * 【删除某一文件夹和其下的所有文件及文件夹】
     **/
    public static void deleteDir(String dirPath) {
        File file = new File(dirPath);
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                file.delete();
            } else {
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }

    public static boolean makeFloder(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            return dir.mkdir();
        } else {
            return false;
        }
    }

    public static void moveFile(String fromFile, String toFile) {
        copyFile(fromFile, toFile);
        if (new File(fromFile).exists()) {
            new File(fromFile).delete();
        }
    }

    /**
     * 【创建文件】
     **/
    public static void addFile(String filePath) {
        File file = new File(filePath);
        try {
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}