package com.jonathan.apkextractor.observer;

import android.content.SharedPreferences;

import com.jonathan.apkextractor.loader.ApkFilesListLoader;
import com.jonathan.apkextractor.util.PreferencesUtils;

public class ApkFilesSortOrderObserver implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ApkFilesListLoader mLoader;

    public ApkFilesSortOrderObserver(ApkFilesListLoader loader) {
        mLoader = loader;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferencesUtils.PREF_KEY_APK_FILES_LIST_SORT_BY)) {
            mLoader.onContentChanged();
        }
    }
}
