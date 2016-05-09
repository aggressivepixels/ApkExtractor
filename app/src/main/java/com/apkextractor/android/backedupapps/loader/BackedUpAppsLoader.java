package com.apkextractor.android.backedupapps.loader;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import com.apkextractor.android.backedupapps.observer.BackedUpAppsObserver;
import com.apkextractor.android.backedupapps.observer.BackedUpAppsSortOrderObserver;
import com.apkextractor.android.common.utils.FileUtils;
import com.apkextractor.android.common.utils.PreferencesUtils;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BackedUpAppsLoader extends AsyncTaskLoader<List<BackedUpApp>> {

    private List<BackedUpApp> mBackedUpApps;
    private BackedUpAppsObserver mObserver;
    private BackedUpAppsSortOrderObserver mOrderObserver;

    public BackedUpAppsLoader(Context context) {
        super(context);
    }

    @Override
    public List<BackedUpApp> loadInBackground() {
        //The array that holds the files
        List<BackedUpApp> backedUpApps = new ArrayList<>();

        //The folder that contains the APKs
        File apkFolder = new File(FileUtils.getBackupFolder(getContext()));

        //All the APKs in the folder
        File[] apkFilesInFolder = apkFolder.listFiles(new BackedUpAppsFilter());

        if (apkFilesInFolder == null) {
            apkFilesInFolder = new File[]{};
        }

        //Now create the actual ApkFiles and return them
        for (File apkFile : apkFilesInFolder) {
            backedUpApps.add(new BackedUpApp(this, apkFile.getPath()));
        }

        if (PreferencesUtils.getApkFilesListSortOrder(getContext()) == PreferencesUtils.SORT_ALPHABETICALLY) {
            //Sort the APKs
            Collections.sort(backedUpApps, ALPHA_COMPARATOR);
        }

        return backedUpApps;
    }

    @Override
    public void deliverResult(List<BackedUpApp> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<BackedUpApp> oldData = mBackedUpApps;
        mBackedUpApps = data;

        if (isStarted()) {
            // If the Loader is in a started state, deliver the results to the
            // client. The superclass method does this for us.
            super.deliverResult(data);
        }

        // Invalidate the old data as we don't need it any more.
        if (oldData != null && oldData != data) {
            releaseResources(oldData);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mBackedUpApps != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mBackedUpApps);
        }

        // Begin monitoring the underlying data source.
        if (mObserver == null) {
            mObserver = new BackedUpAppsObserver(this);
        }
        mObserver.startWatching();

        if (mOrderObserver == null) {
            mOrderObserver = new BackedUpAppsSortOrderObserver(this);
        }
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(mOrderObserver);

        if (takeContentChanged() || mBackedUpApps == null) {
            // When the observer detects a change, it should call onContentChanged()
            // on the Loader, which will cause the next call to takeContentChanged()
            // to return true. If this is ever the case (or if the current data is
            // null), we force a new load.
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // The Loader is in a stopped state, so we should attempt to cancel the 
        // current load (if there is one).
        cancelLoad();

        // Note that we leave the observer as is. Loaders in a stopped state
        // should still monitor the data source for changes so that the Loader
        // will know to force a new load if it is ever started again.
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mBackedUpApps'.
        if (mBackedUpApps != null) {
            releaseResources(mBackedUpApps);
            mBackedUpApps = null;
        }

        // The Loader is being reset, so we should stop monitoring for changes.
        if (mObserver != null) {
            mObserver.stopWatching();
        }
        if (mOrderObserver != null) {
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .unregisterOnSharedPreferenceChangeListener(mOrderObserver);
        }
    }

    @Override
    public void onCanceled(List<BackedUpApp> data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }

    private void releaseResources(List<BackedUpApp> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we 
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }

    private static final Comparator<BackedUpApp> ALPHA_COMPARATOR = new Comparator<BackedUpApp>() {
        Collator sCollator = Collator.getInstance();

        @Override
        public int compare(BackedUpApp object1, BackedUpApp object2) {
            return sCollator.compare(object1.getLabel(), object2.getLabel());
        }
    };
}
