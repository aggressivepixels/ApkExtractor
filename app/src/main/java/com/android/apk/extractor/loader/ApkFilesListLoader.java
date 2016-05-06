package com.android.apk.extractor.loader;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import com.android.apk.extractor.observer.ApkFilesObserver;
import com.android.apk.extractor.observer.ApkFilesSortOrderObserver;
import com.android.apk.extractor.util.FileUtils;
import com.android.apk.extractor.util.PreferencesUtils;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ApkFilesListLoader extends AsyncTaskLoader<List<ApkFile>> {

    private List<ApkFile> mApkFiles;
    private ApkFilesObserver mObserver;
    private ApkFilesSortOrderObserver mOrderObserver;

    public ApkFilesListLoader(Context context) {
        super(context);
    }

    @Override
    public List<ApkFile> loadInBackground() {
        //The array that holds the files
        List<ApkFile> apkFiles = new ArrayList<>();

        //The folder that contains the APKs
        File apkFolder = new File(FileUtils.getBackupFolder(getContext()));

        //All the APKs in the folder
        File[] apkFilesInFolder = apkFolder.listFiles(new ApkFileFilter());

        if (apkFilesInFolder == null) {
            apkFilesInFolder = new File[]{};
        }

        //Now create the actual ApkFiles and return them
        for (File apkFile : apkFilesInFolder) {
            apkFiles.add(new ApkFile(this, apkFile.getPath()));
        }

        if (PreferencesUtils.getApkFilesListSortOrder(getContext()) == PreferencesUtils.SORT_ALPHABETICALLY) {
            //Sort the APKs
            Collections.sort(apkFiles, ALPHA_COMPARATOR);
        }

        return apkFiles;
    }

    @Override
    public void deliverResult(List<ApkFile> data) {
        if (isReset()) {
            // The Loader has been reset; ignore the result and invalidate the data.
            releaseResources(data);
            return;
        }

        // Hold a reference to the old data so it doesn't get garbage collected.
        // We must protect it until the new data has been delivered.
        List<ApkFile> oldData = mApkFiles;
        mApkFiles = data;

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
        if (mApkFiles != null) {
            // Deliver any previously loaded data immediately.
            deliverResult(mApkFiles);
        }

        // Begin monitoring the underlying data source.
        if (mObserver == null) {
            mObserver = new ApkFilesObserver(this);
        }
        mObserver.startWatching();

        if (mOrderObserver == null) {
            mOrderObserver = new ApkFilesSortOrderObserver(this);
        }
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(mOrderObserver);

        if (takeContentChanged() || mApkFiles == null) {
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

        // At this point we can release the resources associated with 'mApkFiles'.
        if (mApkFiles != null) {
            releaseResources(mApkFiles);
            mApkFiles = null;
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
    public void onCanceled(List<ApkFile> data) {
        // Attempt to cancel the current asynchronous load.
        super.onCanceled(data);

        // The load has been canceled, so we should release the resources
        // associated with 'data'.
        releaseResources(data);
    }

    private void releaseResources(List<ApkFile> data) {
        // For a simple List, there is nothing to do. For something like a Cursor, we 
        // would close it in this method. All resources associated with the Loader
        // should be released here.
    }

    private static final Comparator<ApkFile> ALPHA_COMPARATOR = new Comparator<ApkFile>() {
        Collator sCollator = Collator.getInstance();

        @Override
        public int compare(ApkFile object1, ApkFile object2) {
            return sCollator.compare(object1.getLabel(), object2.getLabel());
        }
    };
}
