package com.jonathan.apkextractor.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jonathan.apkextractor.R;
import com.jonathan.apkextractor.adapter.AppsInfoAdapter;
import com.jonathan.apkextractor.loader.AppEntry;
import com.jonathan.apkextractor.loader.AppListLoader;
import com.jonathan.apkextractor.util.ListStateManager;
import com.jonathan.apkextractor.util.PreferencesUtils;
import com.jonathan.apkextractor.util.ViewUtils;

import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstalledAppsListFragment extends Fragment implements AppsInfoAdapter.OnAppInteractionListener, LoaderManager.LoaderCallbacks<List<AppEntry>> {

    private RecyclerView mRecyclerView;
    private AppsInfoAdapter mAdapter;
    private ListStateManager mStateManager;
    private TextView mStickySection;

    private RecyclerView.OnScrollListener mStickySectionScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (recyclerView != null && recyclerView.getChildCount() > 2) {

                View firstVisibleView = recyclerView.getChildAt(0);
                View secondVisibleView = recyclerView.getChildAt(1);

                TextView firstItemSection = ((AppsInfoAdapter.AppViewHolder) recyclerView.getChildViewHolder(firstVisibleView)).sectionHeader;
                TextView secondItemSection = ((AppsInfoAdapter.AppViewHolder) recyclerView.getChildViewHolder(secondVisibleView)).sectionHeader;

                int visibleItemCount = recyclerView.getChildCount();
                int firstVisibleItemPosition = recyclerView.getChildAdapterPosition(firstVisibleView);
                int secondVisibleItemPosition = firstVisibleItemPosition + 1;
                int lastVisibleItemPosition = firstVisibleItemPosition + visibleItemCount;

                mStickySection.setText(firstItemSection.getText().toString().toUpperCase(Locale.getDefault()));
                mStickySection.setVisibility(TextView.VISIBLE);

                if (dy > 0 && secondVisibleItemPosition <= lastVisibleItemPosition) {
                    if (isSectionHeader(firstItemSection.getText().toString(), secondItemSection.getText().toString())) {
                        firstItemSection.setVisibility(View.VISIBLE);
                        secondItemSection.setVisibility(View.VISIBLE);
                        mStickySection.setVisibility(View.INVISIBLE);
                    } else {
                        firstItemSection.setVisibility(View.INVISIBLE);
                        mStickySection.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (secondVisibleItemPosition <= lastVisibleItemPosition) {
                        firstItemSection.setVisibility(TextView.INVISIBLE);
                        if ((isSectionHeader(firstItemSection.getText().toString(), secondItemSection.getText().toString()) || ((!firstItemSection.getText().toString().equals(secondItemSection.getText().toString()))) && isSectionHeader(firstItemSection.getText().toString(), secondItemSection.getText().toString()))) {
                            mStickySection.setVisibility(TextView.INVISIBLE);
                            firstItemSection.setVisibility(TextView.VISIBLE);
                            secondItemSection.setVisibility(TextView.VISIBLE);
                        } else {
                            secondItemSection.setVisibility(TextView.INVISIBLE);
                        }
                    }
                }
            } else {
                mStickySection.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }


        public boolean isSectionHeader(String text1, String text2) {
            return !text1
                    .substring(0, 1)
                    .toUpperCase(Locale.getDefault())
                    .equals(text2
                            .substring(0, 1)
                            .toUpperCase(Locale.getDefault()));
        }
    };

    public InstalledAppsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_installed_apps_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new AppsInfoAdapter(getActivity(), null, this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = ViewUtils.findViewById(view, R.id.recycler_view);
        View loading = ViewUtils.findViewById(view, android.R.id.progress);
        View empty = ViewUtils.findViewById(view, android.R.id.empty);
        mStickySection = ViewUtils.findViewById(view, R.id.app_info_section_header);

        mStateManager = new ListStateManager(mRecyclerView, loading, empty);
        mStateManager.setState(ListStateManager.STATE_LOADING);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addOnScrollListener(mStickySectionScrollListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_installed_apps_list, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_show_system_apps).setChecked(PreferencesUtils.showSystemApps(getActivity()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_show_system_apps) {
            item.setChecked(!item.isChecked());
            boolean showSystemApps = PreferencesUtils.showSystemApps(getActivity());
            if (showSystemApps != item.isChecked()) {
                showSystemApps = item.isChecked();
                PreferencesUtils.setShowSystemApps(getActivity(), showSystemApps);
                getLoaderManager().restartLoader(0, null, this);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAppClick(AppEntry appInfo) {
        AppInfoDialogFragment.getInstance(appInfo.getApplicationInfo()).show(getFragmentManager(), "ApplicationInfoDialog");
    }

    @Override
    public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
        mStateManager.setState(ListStateManager.STATE_LOADING);
        ViewUtils.fadeOut(mStickySection);
        return new AppListLoader(getActivity(), PreferencesUtils.showSystemApps(getActivity()));
    }

    @Override
    public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {
        onAppsInfoLoaded(data);
    }

    @Override
    public void onLoaderReset(Loader<List<AppEntry>> loader) {
        onAppsInfoLoaded(null);
    }

    private void onAppsInfoLoaded(List<AppEntry> appsInfo) {
        mAdapter.setData(appsInfo);
        mRecyclerView.scrollToPosition(0);
        if (appsInfo != null && appsInfo.size() > 0) {
            mStateManager.setState(ListStateManager.STATE_NORMAL);
        } else {
            mStateManager.setState(ListStateManager.STATE_EMPTY);
        }
    }
}
