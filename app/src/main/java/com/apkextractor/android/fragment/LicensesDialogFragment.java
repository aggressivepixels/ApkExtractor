package com.apkextractor.android.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.webkit.WebView;

import com.apkextractor.android.R;

/**
 * Simple {@link AlertDialog} subclass that shows the open sources licences for the app.
 */
public class LicensesDialogFragment extends DialogFragment {

    private static final String LICENSES_FILE_PATH = "file:///android_asset/open_source_licenses.html";

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        WebView view = (WebView) LayoutInflater.from(getActivity()).inflate(R.layout.dialog_licenses, null);
        view.loadUrl(LICENSES_FILE_PATH);
        return new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.licenses))
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }


}