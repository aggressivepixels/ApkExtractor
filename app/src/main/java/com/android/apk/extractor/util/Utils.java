package com.android.apk.extractor.util;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.apk.extractor.contributors.Contributor;
import com.android.apk.extractor.contributors.LinearLayoutAdapterContainer;
import com.android.apk.extractor.fragment.LicensesDialogFragment;
import com.android.apkextractor.R;
import com.android.apk.extractor.contributors.ContributorsListAdapter;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    @SuppressLint("InflateParams")
    public static void showAboutDialog(final FragmentActivity context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.about_dialog, null);
        new LinearLayoutAdapterContainer(
                (LinearLayout) view.findViewById(android.R.id.list))
                .setAdapter(new ContributorsListAdapter(context, getContributorsList(context)));

        new AlertDialog.Builder(context)
                .setTitle(R.string.action_about)
                .setView(view)
                .setNeutralButton(R.string.licenses, new DialogInterface.OnClickListener() {
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
                        context.getString(R.string.app_developer),
                        context.getString(R.string.app_developer_name),
                        context.getString(R.string.app_developer_g_plus_url)));

        //Icon designer (Joaquín)
        contributors.add(
                new Contributor(
                        context.getString(R.string.icon_designer),
                        context.getString(R.string.icon_designer_name),
                        context.getString(R.string.icon_designer_g_plus_url)));

        return contributors;
    }
}
