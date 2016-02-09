package com.jonathan.apkextractor.observer;

import android.os.FileObserver;
import android.os.Handler;

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
        if (event == CREATE || event == DELETE || event == MODIFY) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mLoader.onContentChanged();
        }
    }
}
