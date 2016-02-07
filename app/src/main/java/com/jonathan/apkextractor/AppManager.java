package com.jonathan.apkextractor;

import android.content.pm.ApplicationInfo;

public interface AppManager {
    void backupApk(ApplicationInfo applicationInfo);

    void shareApk(ApplicationInfo applicationInfo, boolean backupFirst);
}
