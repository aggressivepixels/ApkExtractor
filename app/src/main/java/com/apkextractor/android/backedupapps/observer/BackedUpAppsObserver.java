package com.apkextractor.android.backedupapps.observer;

import android.os.FileObserver;

import com.apkextractor.android.common.utils.FileUtils;
import com.apkextractor.android.backedupapps.loader.BackedUpAppsLoader;

/**
 * Used by the {@link BackedUpAppsLoader}. A {@link FileObserver} that watches for
 * files creation, removal and modification (and notifies the loader when
 * these changes are detected).
 */
public class BackedUpAppsObserver extends FileObserver {

    private BackedUpAppsLoader mLoader;

    public BackedUpAppsObserver(BackedUpAppsLoader loader) {
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
