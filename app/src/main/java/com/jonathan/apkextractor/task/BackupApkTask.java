package com.jonathan.apkextractor.task;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.jonathan.apkextractor.util.AppManager;
import com.jonathan.apkextractor.R;
import com.jonathan.apkextractor.loader.AppEntry;

import java.io.IOException;

public class BackupApkTask extends AsyncTask<AppEntry, Void, Void> {

    private AlertDialog mDialog;
    private Context mContext;
    private OnMessageListener mListener;

    public BackupApkTask(Context context, OnMessageListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new AlertDialog.Builder(mContext).setView(R.layout.loading_dialog).show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    @Override
    protected Void doInBackground(AppEntry... params) {
        AppEntry appInfo = params[0];
        try {
            AppManager.backupApk(mContext, appInfo);
            mListener.onMessage(mContext.getResources().getString(R.string.backed_up_app, appInfo.getLabel()));
        } catch (IOException e) {
            mListener.onMessage(mContext.getResources().getString(R.string.error_backing_up_app, appInfo.getLabel()));
        } catch (PackageManager.NameNotFoundException e) {
            mListener.onMessage(mContext.getResources().getString(R.string.error_backing_up_app, appInfo.getLabel()));
        }
        return null;
    }
}
