package com.apkextractor.android.main;

import android.content.pm.ApplicationInfo;

//TODO: Were you high when writing this? Make it a central repo or a ContentProvider
public interface AppManager {
    void deleteApk(ApplicationInfo applicationInfo);

    void backupApk(ApplicationInfo applicationInfo);

    void shareApk(ApplicationInfo applicationInfo, boolean backupFirst);

    void installApk(ApplicationInfo applicationInfo);
}
