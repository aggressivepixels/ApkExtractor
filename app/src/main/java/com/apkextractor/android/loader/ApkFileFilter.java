package com.apkextractor.android.loader;

import java.io.File;
import java.io.FilenameFilter;

class ApkFileFilter implements FilenameFilter {
    @Override
    public boolean accept(File dir, String filename) {
        return filename.endsWith(".apk") || filename.endsWith(".APK");
    }
}
