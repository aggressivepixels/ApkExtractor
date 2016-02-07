package com.jonathan.apkextractor.observer;

import android.os.FileObserver;

import com.jonathan.apkextractor.loader.ApkFilesListLoader;
import com.jonathan.apkextractor.util.FileUtils;


public class ApkFilesObserver extends FileObserver {

    private ApkFilesListLoader mLoader;

    public ApkFilesObserver(ApkFilesListLoader loader) {
        super(FileUtils.getBackupFolder(loader.getContext()));
        mLoader = loader;
    }

    @Override
    public void onEvent(int event, String path) {
        mLoader.onContentChanged();
    }
}
