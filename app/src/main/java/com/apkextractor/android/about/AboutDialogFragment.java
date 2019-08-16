package com.apkextractor.android.about;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.apkextractor.android.R;
import com.apkextractor.android.about.adapters.ContributionsAdapter;
import com.apkextractor.android.about.models.Contribution;
import com.apkextractor.android.common.itemdecorations.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link DialogFragment} subclass that shows the info of the
 * contributors of the app.
 *
 * @author Jonathan Hernández
 */
public class AboutDialogFragment extends DialogFragment {

    /**
     * @return A new {@link AboutDialogFragment}.
     */
    public static AboutDialogFragment newInstance() {
        return new AboutDialogFragment();
    }

    public AboutDialogFragment() {
        // Required empty public constructor
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //First, create and setup the custom view
        View view = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.dialog_about, null);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.about_recycler_view);
        final View topDivider = view.findViewById(R.id.about_top_divider);
        final View bottomDivider = view.findViewById(R.id.about_bottom_divider);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                topDivider.setVisibility(ViewCompat.canScrollVertically(recyclerView, -1) ? View.VISIBLE : View.INVISIBLE);
                bottomDivider.setVisibility(ViewCompat.canScrollVertically(recyclerView, 1) ? View.VISIBLE : View.INVISIBLE);
            }
        });

        //Now setup the contributors
        List<Contribution> contributions = buildContributionsList();

        //Setup the RecyclerView
        Context themedContext = new ContextThemeWrapper(getActivity(), R.style.Theme_ApkExtractor_Dialog_Alert);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setAdapter(new ContributionsAdapter(themedContext, contributions));

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.action_about)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null)
                .setNeutralButton(R.string.about_button_licenses, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LicensesDialogFragment.newInstance().show(getFragmentManager(), "LicensesDialog");
                    }
                })
                .create();
    }

    /**
     * @return The {@link List} of {@link Contribution Contributions} for this app.
     */
    private List<Contribution> buildContributionsList() {
        List<Contribution> contributions = new ArrayList<>();

        contributions.add(
                new Contribution(
                        getResources().getString(R.string.contributors_app_developer),
                        getResources().getString(R.string.contributors_app_developer_name),
                        getResources().getString(R.string.contributors_app_developer_g_plus_url)));

        contributions.add(
                new Contribution(
                        getResources().getString(R.string.contributors_icon_designer),
                        getResources().getString(R.string.contributors_icon_designer_name),
                        getResources().getString(R.string.contributors_icon_designer_g_plus_url)));
        return contributions;
    }
}