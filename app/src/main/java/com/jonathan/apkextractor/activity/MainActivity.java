package com.jonathan.apkextractor.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.jonathan.apkextractor.AppManager;
import com.jonathan.apkextractor.Common;
import com.jonathan.apkextractor.R;
import com.jonathan.apkextractor.loader.AppEntry;
import com.jonathan.apkextractor.util.FileUtils;
import com.jonathan.apkextractor.util.PermissionHelper;
import com.jonathan.apkextractor.util.Utils;
import com.jonathan.apkextractor.util.ViewUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends PermissionHelperActivity implements AppManager {

    private static final int PERMISSIONS_REQUEST_CODE = 3;

    private CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCoordinatorLayout = ViewUtils.findViewById(this, R.id.coordinator_layout);

        Toolbar toolbar = ViewUtils.findViewById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else*/
        if (id == R.id.action_about) {
            Utils.showAboutDialog(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void backupApk(final ApplicationInfo applicationInfo) {
        getPermissionHelper().checkOrAskPermissions(PERMISSIONS_REQUEST_CODE, Common.APP_PERMISSIONS, new PermissionHelper.PermissionListener() {
            @Override
            public void onPermissionsAllowed(int requestCode, String[] permissions) {
                new BackupApkTask().execute(applicationInfo);
            }

            @Override
            public void onPermissionsDenied(int requestCode, String[] permissions, int[] grantResults) {
                showMessage(
                        getResources().getString(R.string.permission_error_message),
                        getResources().getString(R.string.action_settings),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.showAppSettingsScreen(MainActivity.this);
                            }
                        });
            }
        });
    }

    @Override
    public void shareApk(final ApplicationInfo applicationInfo, final boolean backupFirst) {
        getPermissionHelper().checkOrAskPermissions(PERMISSIONS_REQUEST_CODE, Common.APP_PERMISSIONS, new PermissionHelper.PermissionListener() {
            @Override
            public void onPermissionsAllowed(int requestCode, String[] permissions) {
                new ShareApkTask(backupFirst).execute(applicationInfo);
            }

            @Override
            public void onPermissionsDenied(int requestCode, String[] permissions, int[] grantResults) {
                showMessage(
                        getResources().getString(R.string.permission_error_message),
                        getResources().getString(R.string.action_settings),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.showAppSettingsScreen(MainActivity.this);
                            }
                        });
            }
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public File backupApkFile(ApplicationInfo appInfo) throws IOException {
        File file = null;
        if (FileUtils.isExternalStorageWritable()) {
            File dir = new File(FileUtils.getBackupFolder(this));
            dir.mkdirs();

            file = new File(dir + File.separator + FileUtils.getFormattedName(this, appInfo));
            FileUtils.copy(new File(appInfo.sourceDir), file);
        }
        return file;
    }

    private void showMessage(String message) {
        showMessage(message, null, null);
    }

    private void showMessage(String message, String action, View.OnClickListener actionListener) {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_SHORT);
        if (action != null) {
            snackbar.setAction(action, actionListener);
        }
        snackbar.show();
    }

    public class BackupApkTask extends AsyncTask<ApplicationInfo, Void, Void> {

        private AlertDialog mDialog;

        public BackupApkTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new AlertDialog.Builder(MainActivity.this).setView(R.layout.loading_dialog).show();
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
        protected Void doInBackground(ApplicationInfo... params) {
            ApplicationInfo appInfo = params[0];
            try {
                backupApkFile(appInfo);
                showMessage(getResources().getString(R.string.backed_up_app, appInfo.loadLabel(getPackageManager())));
            } catch (IOException e) {
                showMessage(getResources().getString(R.string.error_backing_up_app, appInfo.loadLabel(getPackageManager())));
            }
            return null;
        }
    }

    public class ShareApkTask extends AsyncTask<ApplicationInfo, Void, Void> {

        private AlertDialog mDialog;
        private boolean mBackupApkFirst;

        public ShareApkTask(boolean backupApkFirst) {
            mBackupApkFirst = backupApkFirst;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mBackupApkFirst) {
                mDialog = new AlertDialog.Builder(MainActivity.this).setView(R.layout.loading_dialog).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }

        @SuppressWarnings("TryWithIdenticalCatches")
        @Override
        protected Void doInBackground(ApplicationInfo... params) {
            ApplicationInfo appInfo = params[0];
            try {
                File apk = mBackupApkFirst ? backupApkFile(appInfo) : new File(appInfo.sourceDir);

                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }

                Uri uri = Uri.fromFile(apk);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                i.setType("application/vnd.android.package-archive");
                i.putExtra(Intent.EXTRA_STREAM, uri);

                startActivity(Intent.createChooser(i, getResources().getString(R.string.share_app, appInfo.loadLabel(getPackageManager()))));

            } catch (IOException e) {
                showMessage(getResources().getString(R.string.error_sharing_app, appInfo.loadLabel(getPackageManager())));
            } catch (ActivityNotFoundException e) {
                //TODO: Notify the user no activity can share apks
            }
            return null;
        }
    }
}