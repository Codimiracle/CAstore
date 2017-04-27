package cn.com.sise.ca.castore.common;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

import java.io.File;

/**
 * Created by Codimiracle on 2017/4/7.
 */

public class Application {
    public static final String APK_MIMETYPE = "application/vnd.android.package-archive";
    private static Context context;
    private static Storages storages;

    public static void setContext(Context context) {
        Application.context = context;
        Application.storages = new Storages(context);
    }
    public static Context getContext() {
        return context;
    }

    public static void installAPK(File apk) {
        Uri uri = Uri.fromFile(apk);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, APK_MIMETYPE);
        context.startActivity(intent);
    }

    public static Storages.StorageItem[] getStorages() {
        return storages.getStorages();
    }
}
