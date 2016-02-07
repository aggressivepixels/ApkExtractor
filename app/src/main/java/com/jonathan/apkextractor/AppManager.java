package com.jonathan.apkextractor;

import android.content.pm.ApplicationInfo;

public interface AppManager {
    void deleteApk(ApplicationInfo applicationInfo);

    void backupApk(ApplicationInfo applicationInfo);

    void shareApk(ApplicationInfo applicationInfo, boolean backupFirst);

    void installApk(ApplicationInfo applicationInfo);
}
