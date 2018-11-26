package com.njcwking.share.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * @ClassName : UriUtils
 * @Author : 陈伟
 * @Date : 2018/11/26
 * @Description : say something
 */
public class UriUtils {

    /**
     * 获取文件的Uri
     * @param context
     * @param filePath
     * @return
     */
    public static Uri getUriFromFile(Context context, String filePath) {
        Uri uri = null;
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        if (Build.VERSION.SDK_INT < 24) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);
        }
        return uri;
    }
}
