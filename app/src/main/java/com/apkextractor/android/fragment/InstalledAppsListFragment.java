package com.apkextractor.android.fragment;


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

import com.apkextractor.android.R;
import com.apkextractor.android.adapter.InstalledAppsAdapter;
import com.apkextractor.android.loader.AppEntry;
import com.apkextractor.android.loader.AppListLoader;
import com.apkextractor.android.util.ListStateManager;
import com.apkextractor.android.util.PreferencesUtils;
import com.apkextractor.android.util.ViewUtils;
import com.tonicartos.superslim.LayoutManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstalledAppsListFragment extends Fragment implements InstalledAppsAdapter.OnAppClickListener, LoaderManager.LoaderCallbacks<List<AppEntry>> {

    private RecyclerView mRecyclerView;
    private InstalledAppsAdapter mAdapter;
    private ListStateManager mStateManager;

    public InstalledAppsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_installed_apps, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new InstalledAppsAdapter(getActivity(), this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = ViewUtils.findViewById(view, R.id.recycler_view);
        View loading = ViewUtils.findViewById(view, android.R.id.progress);
        View empty = ViewUtils.findViewById(view, android.R.id.empty);

        mStateManager = new ListStateManager(mRecyclerView, loading, empty);
        mStateManager.setState(ListStateManager.STATE_LOADING);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
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
        mAdapter.setApps(appsInfo);
        mRecyclerView.scrollToPosition(0);
        if (appsInfo != null && appsInfo.size() > 0) {
            mStateManager.setState(ListStateManager.STATE_NORMAL);
        } else {
            mStateManager.setState(ListStateManager.STATE_EMPTY);
        }
    }
}
