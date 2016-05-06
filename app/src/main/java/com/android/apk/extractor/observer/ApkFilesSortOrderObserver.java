package com.android.apk.extractor.observer;

import android.content.SharedPreferences;

import com.android.apk.extractor.loader.ApkFilesListLoader;
import com.android.apk.extractor.util.PreferencesUtils;

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
