package com.njcwking.share.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName : Share
 * @Author : 陈伟
 * @Date : 2018/11/26
 * @Description : say something
 */
public class Share {
    /**
     * 分享的文字内容
     */
    private String text;

    /**
     * 分享选择标题
     */
    private String chooserTitle;

    /**
     * 分享类型
     */
    private int shareType;

    /**
     * 分享的文件集合
     */
    private List<Uri> shareFiles;

    private Intent shareIntent = null;

    public Share(ShareBuilder builder) {
        text = builder.getText();
        chooserTitle = builder.getChooserTitle();
        shareType = builder.getShareType();
        shareFiles = builder.getShareFiles();
        shareIntent = initIntent();
    }

    private Intent initIntent() {
        Intent shareIntent = null;
        switch (shareType) {
            case ShareBuilder.SHARE_TEXT:
                shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                shareIntent.setType("text/plain");
                break;
            case ShareBuilder.SHARE_FILE:
                if (shareFiles != null && shareFiles.size() > 0) {
                    shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, shareFiles.get(0));
                    String extension = MimeTypeMap.getFileExtensionFromUrl(shareFiles.get(0).getPath());
                    String mimeType = "".equals(extension) ? null : MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    shareIntent.setType(mimeType == null ? "*/*" : mimeType);
                }
                break;
            case ShareBuilder.SHARE_MULTIPLE_FILES:
                if (shareFiles != null && shareFiles.size() > 1) {
                    shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, (ArrayList) shareFiles);
                    String mimeType = null;
                    String[] mimeTypeSplit = null;
                    for (Uri file : shareFiles) {
                        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getPath());
                        String fileMimeType = "".equals(extension) ? null : MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                        if (mimeType == null) {
                            mimeType = fileMimeType;
                            mimeTypeSplit = fileMimeType.split("/");
                        } else {
                            String[] typeSplit = fileMimeType.split("/");
                            if (typeSplit[0].equals(mimeTypeSplit[0]) && typeSplit[1].equals(mimeTypeSplit[1])) {
                                //相同类型且后缀相同则不改变mimType
                            } else if (typeSplit[0].equals(mimeTypeSplit[0]) && !typeSplit[1].equals(mimeTypeSplit[1])) {
                                //相同类型不同后缀，则保留类型
                                mimeType = typeSplit[0] + "/*";
                            } else if (!typeSplit[0].equals(mimeTypeSplit[0]) && !typeSplit[1].equals(mimeTypeSplit[1])) {
                                mimeType = "*/*";
                                break;
                            }
                        }
                    }
                    shareIntent.setType(mimeType);
                }
                break;
        }
        return shareIntent;
    }

    /**
     * 获取ShareIntent
     *
     * @return
     */
    public Intent getShareIntent() {
        return shareIntent;
    }

    /**
     * 调用分享页面
     *
     * @param activity
     */
    public void share(Activity activity) {
        activity.startActivity(Intent.createChooser(shareIntent, chooserTitle));
    }

    /**
     * 调用分享页面
     *
     * @param activity
     * @param requestCode
     */
    public void shareForResult(Activity activity, int requestCode) {
        activity.startActivityForResult(Intent.createChooser(shareIntent, chooserTitle), requestCode);
    }
}
