package com.jonathan.apkextractor;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

public class PermissionHelper {

    /**
     * A listener for the permission requests.
     */
    public interface PermissionListener {

        /**
         * Called whenever the permissions were allowed.
         *
         * @param requestCode The request code.
         * @param permissions The permissions asked.
         */
        void onPermissionsAllowed(int requestCode, String[] permissions);


        /**
         * Called whenever the permissions are denied.
         *
         * @param requestCode  The request code.
         * @param permissions  The permissions asked.
         * @param grantResults The permissions results.
         */
        void onPermissionsDenied(int requestCode, String[] permissions, int[] grantResults);
    }

    /**
     * A helper method for checking if all results are granted.
     *
     * @param grantResults The grant results.
     * @return Whether all the permissions are granted or not.
     */
    private static boolean areOnlyGrantedResults(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PermissionChecker.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;

    }

    /**
     * A helper method to check if all the permissions are granted.
     *
     * @param context     A context instance.
     * @param permissions The permissions requested.
     * @return Whether all the permissions are granted or not.
     */
    private static boolean arePermissionsGranted(Context context, String[] permissions) {
        int values[] = new int[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            values[i] = ContextCompat.checkSelfPermission(context, permissions[i]);
        }
        return areOnlyGrantedResults(values);

    }

    /**
     * A static method for getting instances of the Permission helper.
     * <p/>
     * We use this to facilitate potential future refactoring.
     *
     * @param activity The activity.
     * @return A {@link PermissionHelper} instance.
     */
    public static PermissionHelper getInstance(Activity activity) {
        return new PermissionHelper(activity);
    }

    /**
     * An activity instance.
     */
    private Activity mActivity;

    /**
     * A permission listener.
     */
    private PermissionListener mListener;


    /**
     * Default constructor.
     *
     * @param activity An activity instance.
     */
    private PermissionHelper(Activity activity) {
        mActivity = activity;
        mListener = null;
    }


    /**
     * A method for checking/asking the permissions.
     *
     * @param requestCode A request code.
     * @param permissions The asked permissions.
     * @param listener    A listener.
     */
    public void checkOrAskPermissions(int requestCode, String[] permissions, PermissionListener listener) {
        mListener = listener;
        if (arePermissionsGranted(mActivity, permissions)) {
            mListener.onPermissionsAllowed(requestCode, permissions);
        } else {
            ActivityCompat.requestPermissions(mActivity, permissions, requestCode);
        }
    }


    /**
     * Method to call in the activity.
     *
     * @param requestCode  The request code.
     * @param permissions  The permissions.
     * @param grantResults The grant results.
     */
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (arePermissionsGranted(mActivity, permissions)) {
            mListener.onPermissionsAllowed(requestCode, permissions);
        } else {
            mListener.onPermissionsDenied(requestCode, permissions, grantResults);
            Log.w(this.getClass().getName(), "Permissions denied.");
        }
    }
}