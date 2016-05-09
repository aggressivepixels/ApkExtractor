package com.apkextractor.android.backedupapps.observer;

import android.content.SharedPreferences;

import com.apkextractor.android.backedupapps.loader.BackedUpAppsLoader;
import com.apkextractor.android.common.utils.PreferencesUtils;

public class BackedUpAppsSortOrderObserver implements SharedPreferences.OnSharedPreferenceChangeListener {

    private BackedUpAppsLoader mLoader;

    public BackedUpAppsSortOrderObserver(BackedUpAppsLoader loader) {
        mLoader = loader;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PreferencesUtils.PREF_KEY_APK_FILES_LIST_SORT_BY)) {
            mLoader.onContentChanged();
        }
    }
}
