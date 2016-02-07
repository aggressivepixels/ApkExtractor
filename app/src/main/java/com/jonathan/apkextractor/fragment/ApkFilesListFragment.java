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
import android.view.View;
import android.view.ViewGroup;

import com.jonathan.apkextractor.R;
import com.jonathan.apkextractor.adapter.AppsInfoAdapter;
import com.jonathan.apkextractor.loader.AppEntry;
import com.jonathan.apkextractor.loader.AppListLoader;
import com.jonathan.apkextractor.util.PreferencesUtils;
import com.jonathan.apkextractor.util.ViewUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApkFilesListFragment extends Fragment implements AppsInfoAdapter.OnAppInteractionListener, LoaderManager.LoaderCallbacks<List<AppEntry>>{

    //The views of this fragment
    private RecyclerView mRecyclerView;
    private View mLoading;
    private View mEmpty;

    //States for empty, loading, and normal
    private static final int STATE_NORMAL = 0;
    private static final int STATE_EMPTY = 1;
    private static final int STATE_LOADING = 2;

    //It'll start loading by default
    private int mCurrentState = STATE_LOADING;

    private AppsInfoAdapter mAdapter;

    public ApkFilesListFragment() {
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
        mLoading = ViewUtils.findViewById(view, android.R.id.progress);
        mEmpty = ViewUtils.findViewById(view, android.R.id.empty);

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAppClick(AppEntry appInfo) {
        // AppManager.showAppInfo(getActivity(), mPermissionHelper, this, appInfo);
    }

    @Override
    public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
        setState(STATE_LOADING);
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

    public void onAppsInfoLoaded(List<AppEntry> appsInfo) {
        if (appsInfo != null && appsInfo.size() > 0) {
            mAdapter.setData(appsInfo);
            setState(STATE_NORMAL);
        } else {
            setState(STATE_EMPTY);
        }
    }

    public void setState(int state) {
        if (mCurrentState == state) {
            return;
        }
        mCurrentState = state;
        switch (mCurrentState) {
            case STATE_NORMAL:
                ViewUtils.fadeOut(mEmpty);
                ViewUtils.fadeOut(mLoading);
                break;
            case STATE_LOADING:
                ViewUtils.fadeOut(mEmpty);
                ViewUtils.fadeIn(mLoading);
                mAdapter.setData(null);
                break;
            case STATE_EMPTY:
                ViewUtils.fadeIn(mEmpty);
                ViewUtils.fadeOut(mLoading);
                break;
        }
    }
}
