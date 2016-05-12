package com.apkextractor.android.common.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.apkextractor.android.common.utils.PermissionHelper;

/**
 * Simple {@link AppCompatActivity} that has a {@link PermissionHelper} instance and
 * calls all the necessary methods to make it work.
 * To get the {@link PermissionHelper} instance just call {@link PermissionHelperActivity#getPermissionHelper()}.
 */
public class PermissionHelperActivity extends AppCompatActivity {

    //The actual PermissionHelper
    private PermissionHelper mPermissionHelper;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissionHelper = PermissionHelper.getInstance(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionHelper.onRequestPermissionResult(requestCode, permissions, grantResults);
    }

    /**
     * @return The {@link PermissionHelper} of this {@link PermissionHelperActivity}.
     */
    public PermissionHelper getPermissionHelper() {
        return mPermissionHelper;
    }
}
