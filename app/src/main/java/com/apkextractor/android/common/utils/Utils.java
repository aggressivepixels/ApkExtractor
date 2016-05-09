package com.apkextractor.android.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.apkextractor.android.R;
import com.apkextractor.android.contributors.Contributor;
import com.apkextractor.android.contributors.ContributorsListAdapter;
import com.apkextractor.android.contributors.LinearLayoutAdapterContainer;
import com.apkextractor.android.contributors.LicensesDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    @SuppressLint("InflateParams")
    public static void showAboutDialog(final FragmentActivity context) {
        Context themedContext = new ContextThemeWrapper(context, R.style.Theme_ApkExtractor_Dialog_Alert);

        LayoutInflater inflater = LayoutInflater.from(themedContext);
        View view = inflater.inflate(R.layout.dialog_about, null);
        new LinearLayoutAdapterContainer(
                (LinearLayout) view.findViewById(android.R.id.list))
                .setAdapter(new ContributorsListAdapter(themedContext, getContributorsList(context)));

        //Adding the style again, just in case
        new AlertDialog.Builder(themedContext, R.style.Theme_ApkExtractor_Dialog_Alert)
                .setTitle(R.string.action_about)
                .setView(view)
                .setNeutralButton(R.string.about_button_licenses, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new LicensesDialogFragment().show(context.getSupportFragmentManager(), "LicensesDialogFragment");
                    }
                }).setPositiveButton(android.R.string.ok, null)
                .show();
    }


    /**
     * A helper method for showing the app's settings screen.
     *
     * @param activity An activity instance.
     * @return Whether the app settings were opened or not.
     */
    public static boolean showAppSettingsScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts(
                            "package",
                            activity.getPackageName(),
                            null));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    private static List<Contributor> getContributorsList(Context context) {
        List<Contributor> contributors = new ArrayList<>();

        //Developer (Me! :D)
        contributors.add(
                new Contributor(
                        context.getString(R.string.contributors_app_developer),
                        context.getString(R.string.contributors_app_developer_name),
                        context.getString(R.string.contributors_app_developer_g_plus_url)));

        //Icon designer (Joaquín)
        contributors.add(
                new Contributor(
                        context.getString(R.string.contributors_icon_designer),
                        context.getString(R.string.contributors_icon_designer_name),
                        context.getString(R.string.contributors_icon_designer_g_plus_url)));

        return contributors;
    }
}
