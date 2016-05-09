package com.apkextractor.android.backedupapps;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.apkextractor.android.backedupapps.loader.BackedUpApp;
import com.apkextractor.android.main.AppManager;
import com.apkextractor.android.R;
import com.apkextractor.android.backedupapps.adapter.BackedUpAppsAdapter;
import com.apkextractor.android.common.utils.ListStateManager;
import com.apkextractor.android.common.utils.PreferencesUtils;
import com.apkextractor.android.common.utils.ViewUtils;
import com.apkextractor.android.backedupapps.loader.BackedUpAppsLoader;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class BackedUpAppsListFragment extends Fragment implements BackedUpAppsAdapter.OnApkInteractionListener, LoaderManager.LoaderCallbacks<List<BackedUpApp>> {

    private RecyclerView mRecyclerView;
    private BackedUpAppsAdapter mAdapter;
    private ListStateManager mStateManager;

    private AppManager mAppManager;

    public BackedUpAppsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_backup, container, false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mAppManager = (AppManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AppManager");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mAdapter = new BackedUpAppsAdapter(getActivity(), null, this);
        mAdapter.setHasStableIds(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = ViewUtils.findViewById(view, R.id.recycler_view);
        View empty = ViewUtils.findViewById(view, android.R.id.empty);

        mStateManager = new ListStateManager(mRecyclerView, null, empty);
        mStateManager.setState(ListStateManager.STATE_NORMAL);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new AnimatableLinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<List<BackedUpApp>> onCreateLoader(int id, Bundle args) {
        return new BackedUpAppsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<BackedUpApp>> loader, List<BackedUpApp> data) {
        onApkFilesLoaded(data);
    }

    @Override
    public void onLoaderReset(Loader<List<BackedUpApp>> loader) {
        onApkFilesLoaded(null);
    }

    private void onApkFilesLoaded(List<BackedUpApp> appsInfo) {
        mAdapter.setData(appsInfo);
        if (appsInfo != null && appsInfo.size() > 0) {
            mStateManager.setState(ListStateManager.STATE_NORMAL);
        } else {
            mStateManager.setState(ListStateManager.STATE_EMPTY);
        }
    }

    @Override
    public void shareApk(BackedUpApp backedUpApp) {
        mAppManager.shareApk(backedUpApp.getApplicationInfo(), false);
    }

    @Override
    public void deleteApk(BackedUpApp backedUpApp) {
        mAppManager.deleteApk(backedUpApp.getApplicationInfo());
    }

    @Override
    public void installApk(BackedUpApp backedUpApp) {
        mAppManager.installApk(backedUpApp.getApplicationInfo());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_apk_files_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sort_alphabetically:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    PreferencesUtils.setApkFilesListSortOrder(getActivity(), PreferencesUtils.SORT_ALPHABETICALLY);
                }
                return true;
            case R.id.action_sort_by_date:
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    PreferencesUtils.setApkFilesListSortOrder(getActivity(), PreferencesUtils.SORT_BY_DATE);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        int sortType = PreferencesUtils.getApkFilesListSortOrder(getActivity());
        switch (sortType) {
            case PreferencesUtils.SORT_ALPHABETICALLY:
                menu.findItem(R.id.action_sort_alphabetically).setChecked(true);
                break;
            case PreferencesUtils.SORT_BY_DATE:
                menu.findItem(R.id.action_sort_by_date).setChecked(true);
                break;
        }
    }
}
