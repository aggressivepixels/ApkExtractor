package com.apkextractor.android.backedupapps.loader;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.apkextractor.android.common.utils.FileUtils;

import java.io.File;

public class BackedUpApp {

    private File mApkFile;
    private Drawable mIcon;
    private String mLabel;
    private ApplicationInfo mApplicationInfo;

    public BackedUpApp(BackedUpAppsLoader loader, String path) {
        mApkFile = new File(path);
        PackageInfo packageInfo = loader.getContext().getPackageManager().getPackageArchiveInfo(path, 0);
        if (packageInfo != null) {
            ApplicationInfo appInfo = packageInfo.applicationInfo;
            if (Build.VERSION.SDK_INT >= 8) {
                appInfo.sourceDir = path;
                appInfo.publicSourceDir = path;
            }
            mIcon = appInfo.loadIcon(loader.getContext().getPackageManager());

            mApplicationInfo = appInfo;
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

    public ApplicationInfo getApplicationInfo() {
        return mApplicationInfo;
    }

    @SuppressWarnings("SimplifiableIfStatement")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BackedUpApp)) return false;

        BackedUpApp backedUpApp = (BackedUpApp) o;

        if (mApkFile != null ? !mApkFile.equals(backedUpApp.mApkFile) : backedUpApp.mApkFile != null) return false;
        if (mIcon != null ? !mIcon.equals(backedUpApp.mIcon) : backedUpApp.mIcon != null) return false;
        if (mLabel != null ? !mLabel.equals(backedUpApp.mLabel) : backedUpApp.mLabel != null) return false;
        return mApplicationInfo != null ? mApplicationInfo.equals(backedUpApp.mApplicationInfo) : backedUpApp.mApplicationInfo == null;
    }

    @Override
    public int hashCode() {
        int result = mApkFile != null ? mApkFile.hashCode() : 0;
        result = 31 * result + (mIcon != null ? mIcon.hashCode() : 0);
        result = 31 * result + (mLabel != null ? mLabel.hashCode() : 0);
        result = 31 * result + (mApplicationInfo != null ? mApplicationInfo.hashCode() : 0);
        return result;
    }
}

