package com.jonathan.apkextractor.task;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;

import com.jonathan.apkextractor.util.AppManager;
import com.jonathan.apkextractor.R;
import com.jonathan.apkextractor.loader.AppEntry;

import java.io.File;
import java.io.IOException;

public class ShareApkTask extends AsyncTask<AppEntry, Void, Void> {

    private AlertDialog mDialog;
    private Context mContext;
    private OnMessageListener mListener;

    public ShareApkTask(Context context, OnMessageListener listener) {
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
            File apk = AppManager.backupApk(mContext, appInfo);
            mDialog.dismiss();
            Uri uri = Uri.fromFile(apk);
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            i.setType("application/vnd.android.package-archive");
            i.putExtra(Intent.EXTRA_STREAM, uri);
            mContext.startActivity(Intent.createChooser(i, mContext.getResources().getString(R.string.share_app, appInfo.getLabel())));
        } catch (IOException e) {
            mListener.onMessage(mContext.getResources().getString(R.string.error_sharing_app, appInfo.getLabel()));
        } catch (PackageManager.NameNotFoundException e) {
            mListener.onMessage(mContext.getResources().getString(R.string.error_sharing_app, appInfo.getLabel()));
        } catch (ActivityNotFoundException e) {
            //TODO: Notify the user no activity can share apks
        }
        return null;
    }
}