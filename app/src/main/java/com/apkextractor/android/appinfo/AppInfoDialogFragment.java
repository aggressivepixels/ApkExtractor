package com.apkextractor.android.appinfo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.apkextractor.android.main.AppManager;
import com.apkextractor.android.R;
import com.apkextractor.android.common.utils.ViewUtils;

public class AppInfoDialogFragment extends DialogFragment {

    private static final String EXTRA_APPLICATION_INFO = "com.jonathan.apkextractor.fragment.EXTRA_APPLICATION_INFO";

    private ApplicationInfo mApplicationInfo;
    private PackageInfo mPackageInfo;

    private AppManager mAppManager;

    public static AppInfoDialogFragment getInstance(ApplicationInfo applicationInfo) {
        AppInfoDialogFragment fragment = new AppInfoDialogFragment();

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_APPLICATION_INFO, applicationInfo);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mAppManager = (AppManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AppManager");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplicationInfo = getArguments().getParcelable(EXTRA_APPLICATION_INFO);
        try {
            mPackageInfo = getActivity().getPackageManager().getPackageInfo(mApplicationInfo.packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.share_error_getting_app_info), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (mPackageInfo == null && mApplicationInfo == null) {
            return super.onCreateDialog(savedInstanceState);
        }

        LayoutInflater inflater = LayoutInflater.from(new ContextThemeWrapper(getActivity(), R.style.Theme_ApkExtractor_Dialog_Alert));

        View view = inflater.inflate(
                R.layout.dialog_app_info, null);

        ViewUtils.setText(view, mPackageInfo.packageName, R.id.app_info_package);
        ViewUtils.setText(view, mPackageInfo.versionName, R.id.app_info_version);

        final Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(mPackageInfo.packageName);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setIcon(mApplicationInfo.loadIcon(getActivity().getPackageManager()))
                .setTitle(mApplicationInfo.loadLabel(getActivity().getPackageManager()))
                .setView(view);

        if (launchIntent != null) {
            dialog.setNeutralButton(R.string.app_info_button_launch, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().startActivity(launchIntent);
                }
            });
        }

        dialog.setPositiveButton(R.string.app_info_button_backup, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAppManager.backupApk(mApplicationInfo);
            }
        }).setNegativeButton(R.string.app_info_button_share, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAppManager.shareApk(mApplicationInfo, true);
            }
        });

        return dialog.create();
    }
}
