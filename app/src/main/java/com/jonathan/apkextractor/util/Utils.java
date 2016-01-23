package com.jonathan.apkextractor.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.jonathan.apkextractor.fragment.LicensesDialogFragment;
import com.jonathan.apkextractor.R;
import com.jonathan.apkextractor.contributors.Contributor;
import com.jonathan.apkextractor.contributors.ContributorsListAdapter;
import com.jonathan.apkextractor.contributors.LinearLayoutAdapterContainer;

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
                .setTitle(R.string.about)
                .setView(view)
                .setNeutralButton(R.string.licenses, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new LicensesDialogFragment().show(context.getSupportFragmentManager(), "LicensesDialogFragment");
                    }
                }).setPositiveButton(android.R.string.ok, null)
                .show();
    }

    private static List<Contributor> getContributorsList(Context context) {
        List<Contributor> contributors = new ArrayList<>();

        //Developer (Me! :D)
        contributors.add(
                new Contributor(
                        context.getString(R.string.developer),
                        context.getString(R.string.developer_name),
                        context.getString(R.string.developer_g_plus_url)));

        //Icon designer (Joaquín)
        contributors.add(
                new Contributor(
                        context.getString(R.string.icon),
                        context.getString(R.string.icon_creator_name),
                        context.getString(R.string.icon_creator_g_plus_url)));

        return contributors;
    }
}
