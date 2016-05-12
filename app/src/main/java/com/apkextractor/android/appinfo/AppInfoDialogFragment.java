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

import com.apkextractor.android.R;
import com.apkextractor.android.common.utils.ViewUtils;
import com.apkextractor.android.main.AppManager;

/**
 * {@link DialogFragment} that shows information provided by an
 * {@link ApplicationInfo}. It also shows options to backup, share
 * and launch it (if the app provides a launch {@link Intent}).
 */
public class AppInfoDialogFragment extends DialogFragment {

    private static final String EXTRA_APPLICATION_INFO = "com.jonathan.apkextractor.appinfo.EXTRA_APPLICATION_INFO";

    private ApplicationInfo applicationInfo;
    private PackageInfo packageInfo;

    private AppManager appManager;

    public static AppInfoDialogFragment newInstance(ApplicationInfo applicationInfo) {
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
            appManager = (AppManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AppManager");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicationInfo = getArguments().getParcelable(EXTRA_APPLICATION_INFO);
        try {
            packageInfo = getActivity().getPackageManager().getPackageInfo(applicationInfo.packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            //TODO: Show something better than a Toast.
            Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.share_error_getting_app_info), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (packageInfo == null || applicationInfo == null) {
            throw new IllegalArgumentException(
                    "Neither the ApplicationInfo or the " +
                            "PackageInfo object can be null.");
        }
        //For some reason, if I use the default theme for the AlertDialog,
        //it seems to ignore "?listPreferredItemPaddingLeft" and related
        //attributes, that's why I'm using the ContextThemeWrapper to
        //be sure it doesn't ignores it.
        LayoutInflater inflater = LayoutInflater.from(
                new ContextThemeWrapper(
                        getActivity(),
                        R.style.Theme_ApkExtractor_Dialog_Alert));

        View view = inflater.inflate(
                R.layout.dialog_app_info, null);

        ViewUtils.setText(view, packageInfo.packageName, R.id.app_info_package);
        ViewUtils.setText(view, packageInfo.versionName, R.id.app_info_version);

        PackageManager packageManager =  getActivity().getPackageManager();

        final Intent launchIntent = packageManager.getLaunchIntentForPackage(packageInfo.packageName);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                .setIcon(applicationInfo.loadIcon(packageManager))
                .setTitle(applicationInfo.loadLabel(packageManager))
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
                appManager.backupApk(applicationInfo);
            }
        }).setNegativeButton(R.string.app_info_button_share, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                appManager.shareApk(applicationInfo, true);
            }
        });

        return dialog.create();
    }
}
