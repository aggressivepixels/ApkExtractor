package com.jonathan.apkextractor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.jonathan.apkextractor.loader.AppEntry;
import com.jonathan.apkextractor.task.BackupApkTask;
import com.jonathan.apkextractor.task.OnMessageListener;
import com.jonathan.apkextractor.task.ShareApkTask;
import com.jonathan.apkextractor.utils.FileUtils;
import com.jonathan.apkextractor.utils.ViewUtils;

import java.io.File;
import java.io.IOException;

public class AppManager {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static File backupApk(Context context, AppEntry appInfo) throws IOException, PackageManager.NameNotFoundException {
        File file = null;
        if (FileUtils.isExternalStorageWritable()) {
            File externalStorage = Environment.getExternalStorageDirectory();
            File dir = new File(externalStorage.getAbsolutePath() + File.separator + Common.FOLDER_NAME);
            dir.mkdirs();

            PackageInfo info = context.getPackageManager().getPackageInfo(appInfo.getApplicationInfo().packageName, 0);
            file = new File(dir + File.separator + appInfo.getLabel() + " " + info.versionName + ".apk");
            FileUtils.copy(appInfo.getApkFile(), file);
        }
        return file;
    }

    @SuppressLint("InflateParams")
    public static void showAppInfo(final Activity context, final OnMessageListener listener, final AppEntry appInfo) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(appInfo.getApplicationInfo().packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            listener.onMessage(context.getResources().getString(R.string.error_getting_app_info));
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.app_info_dialog, null);
        ViewUtils.setText(view, info.packageName, android.R.id.text1);
        ViewUtils.setText(view, info.versionName, android.R.id.text2);
        final PackageInfo finalInfo = info;
        new AlertDialog.Builder(context)
                .setIcon(appInfo.getIcon())
                .setTitle(appInfo.getLabel())
                .setView(view)
                .setNeutralButton(R.string.launch, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(context.getPackageManager().getLaunchIntentForPackage(finalInfo.packageName));
                    }
                }).setPositiveButton(R.string.backup, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                backupApk(context, listener, appInfo);
            }
        })
                .setNegativeButton(R.string.share, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shareApk(context, listener, appInfo);
                    }
                }).show();
    }


    public static void backupApk(final Activity context,  final OnMessageListener listener, final AppEntry appInfo) {
        new BackupApkTask(context, listener).execute(appInfo);
    }

    public static void shareApk(final Activity context, final OnMessageListener listener, final AppEntry appInfo) {
        new ShareApkTask(context, listener).execute(appInfo);
    }

    /**
     * A helper method for showing the app's settings screen.
     *
     * @param activity An activity instance.
     * @return Whether the app settings were opened or not.
     */
    public static boolean showAppSettingsScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts(
                            "package",
                            activity.getPackageName(),
                            null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }
}
