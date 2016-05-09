package com.apkextractor.android.backedupapps.loader;

import java.io.File;
import java.io.FilenameFilter;

class BackedUpAppsFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String filename) {
        return filename.endsWith(".apk") || filename.endsWith(".APK");
    }
}
