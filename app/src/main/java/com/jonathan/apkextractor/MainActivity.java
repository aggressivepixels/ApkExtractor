package com.jonathan.apkextractor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jonathan.apkextractor.adapter.AppsInfoAdapter;
import com.jonathan.apkextractor.loader.AppEntry;
import com.jonathan.apkextractor.loader.AppListLoader;
import com.jonathan.apkextractor.task.OnMessageListener;
import com.jonathan.apkextractor.utils.PreferencesUtils;
import com.jonathan.apkextractor.utils.ViewUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<AppEntry>>, AppsInfoAdapter.OnAppInteractionListener, OnMessageListener {

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private TextView mEmpty;

    //States for empty, loading, and normal
    private static final int STATE_NORMAL = 0;
    private static final int STATE_EMPTY = 1;
    private static final int STATE_LOADING = 2;

    //It'll start loading by default
    private int mCurrentState = STATE_LOADING;

    private boolean mRestoreSystemAppsState;

    private PermissionHelper mPermissionHelper;

    //TODO add loading and empty state
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRestoreSystemAppsState = true;

        mPermissionHelper = PermissionHelper.getInstance(this);

        Toolbar toolbar = ViewUtils.findViewById(this, R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = ViewUtils.findViewById(this, R.id.recycler_view);
        mRefreshLayout = ViewUtils.findViewById(this, R.id.swipe_refresh_layout);
        mEmpty = ViewUtils.findViewById(this, android.R.id.empty);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRefreshLayout.setEnabled(false);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);

        //Small hack, SwipeRefreshLayout wont show the indicator if it haven't
        //measured itself, with this I show the indicator after a small delay,
        //so I'm sure it's measured.
        if (savedInstanceState == null && mCurrentState == STATE_LOADING) {
            mRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                }
            }, 400);
        }

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mRestoreSystemAppsState) {
            mRestoreSystemAppsState = false;
            menu.findItem(R.id.action_show_system_apps).setChecked(PreferencesUtils.showSystemApps(this));
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else*/
        if (id == R.id.action_show_system_apps) {
            item.setChecked(!item.isChecked());
            boolean showSystemApps = PreferencesUtils.showSystemApps(this);
            if (showSystemApps != item.isChecked()) {
                showSystemApps = item.isChecked();
                PreferencesUtils.setShowSystemApps(this, showSystemApps);
                getSupportLoaderManager().restartLoader(0, null, this);
            }
            return true;
        } else if (id == R.id.action_about) {
            AppManager.showAboutDialog(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAppsInfoLoaded(List<AppEntry> appsInfo) {
        if (appsInfo != null && appsInfo.size() > 0) {
            mRecyclerView.swapAdapter(new AppsInfoAdapter(this, appsInfo, this), false);
            setState(STATE_NORMAL);
        } else {
            setState(STATE_EMPTY);
        }
    }

    @Override
    public void onAppClick(final AppEntry appInfo) {
        AppManager.showAppInfo(this, mPermissionHelper, this, appInfo);
    }

    @Override
    public void onMessage(String message) {
        Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onMessage(String message, String buttonText, final View.OnClickListener onClickListener) {
        Snackbar.make(mRecyclerView, message, Snackbar.LENGTH_SHORT).setAction(buttonText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onClick(v);
            }
        }).show();
    }

    public void setState(int state) {
        if (mCurrentState == state) {
            return;
        }
        mCurrentState = state;
        switch (mCurrentState) {
            case STATE_NORMAL:
                ViewUtils.fadeOut(mEmpty);
                mRefreshLayout.setRefreshing(false);
                break;
            case STATE_LOADING:
                ViewUtils.fadeOut(mEmpty);
                mRefreshLayout.setRefreshing(true);
                break;
            case STATE_EMPTY:
                ViewUtils.fadeIn(mEmpty);
                mRefreshLayout.setRefreshing(false);
                break;
        }
    }

    @Override
    public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
        setState(STATE_LOADING);
        return new AppListLoader(this, PreferencesUtils.showSystemApps(this));
    }

    @Override
    public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {
        onAppsInfoLoaded(data);
    }

    @Override
    public void onLoaderReset(Loader<List<AppEntry>> loader) {
        onAppsInfoLoaded(null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionHelper.onRequestPermissionResult(requestCode, permissions, grantResults);
    }
}
