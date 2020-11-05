package com.android.libs_common.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;


import com.android.libs_common.base.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.System.gc;

/**
 * Created by zhangqilu on 2017/9/6.
 */

public class  FileUtils {

    public static String getSaveAppPicDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 外部存储
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ScreenRecord" + "/" + "buff_share_app_pic" + ".jpg";

            KLog.e("rootDir " + rootDir);
            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }

            return rootDir;
        } else {
            return null;
        }
    }

    public static File getExternalCacheDir(final Context context) {
        if (hasExternalCacheDir())
            return context.getExternalCacheDir();

        // Before Froyo we need to construct the external cache dir ourselves
        final String cacheDir = "/Android/data/" + context.getPackageName() + "/cache/";
        return createFile(Environment.getExternalStorageDirectory().getPath() + cacheDir, "");
    }

    public static boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName);
    }


    public static String getSavePicDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 外部存储
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ScreenRecord" + "/" + "buff_share_pic" + ".jpg";

            KLog.e("rootDir " + rootDir);
            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }

            return rootDir;
        } else {
            return null;
        }
    }

    public static String getSaveVideoDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 外部存储
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Briefly" + "/" + "share_pic" + ".jpg";

            KLog.e("rootDir " + rootDir);
            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }

            return rootDir;
        } else {
            return null;
        }
    }

    public static String getFileDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 外部存储
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Briefly/";

            KLog.e("rootDir " + rootDir);
            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }

            return rootDir;
        } else {
            return null;
        }
    }


    public static String getSaveDayCardDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 外部存储
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "ScreenRecord" + "/" + "day_card_pic" + ".jpg";
            KLog.e("rootDir " + rootDir);
            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }

            return rootDir;
        } else {
            return null;
        }
    }


    public static String getSaveQiNiuDirectory() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "QiNiu" + "/";

            File file = new File(rootDir);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    return null;
                }
            }

            return rootDir;
        } else {
            return null;
        }
    }


    /**
     * 获取指定文件大小
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            size = 0;
        }
        KLog.e("size " + size);
        return size;
    }


    /**
     * 保存视频到相册
     */
    public static void saveVideoToAlbum(Context context, File file) {
        //是否添加到相册
        ContentResolver localContentResolver = BaseApplication.getInstance().getContentResolver();
        ContentValues localContentValues = getVideoContentValues(context, file, System.currentTimeMillis());
        Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
        KLog.e("localUri " + localUri.toString());
    }


    public static ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "video/mp4");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }

    /**
     * 合成的图片存到sd卡中
     *
     * @param bt
     * @param path
     */
    public static void saveBitmapToSD(Bitmap bt, String path) {
        KLog.e("saveBitmapToSD path " + path);
        if (bt == null || bt.isRecycled() || StringUtils.isBlank(path)) {
            return;
        }
        File file = new File(path);
        try {
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out = new FileOutputStream(file);
            if (bt == null || bt.isRecycled()) {
                return;
            }
            bt.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            KLog.d("kkkkkkkk", "saveBitmapToSD suc");
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError outOfMemoryError) {
            gc();
        }
    }

    /**
     * 合成的图片存到sd卡中
     *
     * @param bt
     * @param path
     */
    public static void saveBitmapToSD(Bitmap bt, String path, String fileName) {
        KLog.e("saveBitmapToSD path " + path);
        if (bt == null || bt.isRecycled() || StringUtils.isBlank(path)) {
            return;
        }
        File file = new File(path + fileName + ".jpg");
        try {
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out = new FileOutputStream(file);
            if (bt == null || bt.isRecycled()) {
                return;
            }
            bt.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            KLog.d("kkkkkkkk", "saveBitmapToSD suc");
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError outOfMemoryError) {
            gc();
        }
    }

    public static Bitmap getSDBitmap(String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        File mFile = new File(path);
        //若该文件存在
        if (mFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        }
        return null;
    }

}
