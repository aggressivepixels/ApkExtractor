package com.jonathan.apkextractor.fragment;


import android.app.Activity;
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

import com.jonathan.apkextractor.AnimatableLinearLayoutManager;
import com.jonathan.apkextractor.AppManager;
import com.jonathan.apkextractor.R;
import com.jonathan.apkextractor.adapter.ApkFilesAdapter;
import com.jonathan.apkextractor.loader.ApkFile;
import com.jonathan.apkextractor.loader.ApkFilesListLoader;
import com.jonathan.apkextractor.util.ListStateManager;
import com.jonathan.apkextractor.util.ViewUtils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApkFilesListFragment extends Fragment implements ApkFilesAdapter.OnApkInteractionListener, LoaderManager.LoaderCallbacks<List<ApkFile>> {

    private RecyclerView mRecyclerView;
    private ApkFilesAdapter mAdapter;
    private ListStateManager mStateManager;

    private AppManager mAppManager;

    public ApkFilesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_apk_files_list, container, false);
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
        mAdapter = new ApkFilesAdapter(getActivity(), null, this);
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
    public Loader<List<ApkFile>> onCreateLoader(int id, Bundle args) {
        return new ApkFilesListLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<ApkFile>> loader, List<ApkFile> data) {
        onApkFilesLoaded(data);
    }

    @Override
    public void onLoaderReset(Loader<List<ApkFile>> loader) {
        onApkFilesLoaded(null);
    }

    public void onApkFilesLoaded(List<ApkFile> appsInfo) {
        mAdapter.setData(appsInfo);
        if (appsInfo != null && appsInfo.size() > 0) {
            mStateManager.setState(ListStateManager.STATE_NORMAL);
        } else {
            mStateManager.setState(ListStateManager.STATE_EMPTY);
        }
    }

    @Override
    public void shareApk(ApkFile apkFile) {
        mAppManager.shareApk(apkFile.getApplicationInfo(), false);
    }

    @Override
    public void deleteApk(ApkFile apkFile) {
        mAppManager.deleteApk(apkFile.getApplicationInfo());
    }

    @Override
    public void installApk(ApkFile apkFile) {
        mAppManager.installApk(apkFile.getApplicationInfo());
    }
}
