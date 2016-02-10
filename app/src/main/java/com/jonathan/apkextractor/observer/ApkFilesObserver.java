package com.jonathan.apkextractor.observer;

import android.os.FileObserver;

import com.jonathan.apkextractor.loader.ApkFilesListLoader;
import com.jonathan.apkextractor.util.FileUtils;

/**
 * Used by the {@link ApkFilesListLoader}. A {@link FileObserver} that watches for
 * files creation, removal and modification (and notifies the loader when
 * these changes are detected).
 */
public class ApkFilesObserver extends FileObserver {

    private ApkFilesListLoader mLoader;

    public ApkFilesObserver(ApkFilesListLoader loader) {
        super(FileUtils.getBackupFolder(loader.getContext()));
        mLoader = loader;
    }

    @Override
    public void onEvent(int event, String path) {
        /*We wait a few millis to trigger the change, if we notify the loader
         *right away it can cause a crash.
         */
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
