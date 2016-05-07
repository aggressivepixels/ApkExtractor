package com.apkextractor.android.observer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.apkextractor.android.BuildConfig;
import com.apkextractor.android.loader.AppListLoader;

/**
 * Used by the {@link AppListLoader}. An observer that intercepts system-wide
 * locale changes (and notifies the loader when these changes are detected).
 */
public class SystemLocaleObserver extends BroadcastReceiver {
    private static final String TAG = "SystemLocaleObserver";

    private AppListLoader mLoader;

    public SystemLocaleObserver(AppListLoader loader) {
        mLoader = loader;
        IntentFilter filter = new IntentFilter(Intent.ACTION_LOCALE_CHANGED);
        mLoader.getContext().registerReceiver(this, filter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (BuildConfig.DEBUG) Log.i(TAG, "The observer has detected a locale change!" +
                " Notifying Loader...");

        // Tell the loader about the change.
        mLoader.onContentChanged();
    }
}