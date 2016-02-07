package com.jonathan.apkextractor.loader;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.jonathan.apkextractor.util.FileUtils;

import java.io.File;

public class ApkFile {

    private File mApkFile;
    private Drawable mIcon;
    private String mLabel;

    public ApkFile(ApkFilesListLoader loader, String path) {
        mApkFile = new File(path);
        PackageInfo packageInfo = loader.getContext().getPackageManager().getPackageArchiveInfo(path, 0);
        if (packageInfo != null) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            if (Build.VERSION.SDK_INT >= 8) {
                appInfo.sourceDir = path;
                appInfo.publicSourceDir = path;
            }
            mIcon = appInfo.loadIcon(loader.getContext().getPackageManager());
        }
        mLabel = FileUtils.removeExtension(path);
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public File getApkFile() {
        return mApkFile;
    }

    public String getLabel() {
        return mLabel;
    }
}
