package com.apkextractor.android.backedupapps.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkextractor.android.R;
import com.apkextractor.android.backedupapps.loader.BackedUpApp;
import com.apkextractor.android.common.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class BackedUpAppsAdapter extends RecyclerView.Adapter<BackedUpAppsAdapter.BackedUpAppViewHolder> {

    private LayoutInflater mInflater;
    private List<BackedUpApp> mBackedUpApps;
    private OnApkInteractionListener mListener;

    public BackedUpAppsAdapter(Context context, List<BackedUpApp> backedUpApps, OnApkInteractionListener listener) {
        mInflater = LayoutInflater.from(context);
        if (backedUpApps == null) {
            backedUpApps = new ArrayList<>();
        }
        mBackedUpApps = backedUpApps;
        mListener = listener;
    }

    @Override
    public BackedUpAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BackedUpAppViewHolder(mInflater.inflate(R.layout.item_backed_up_app, parent, false));
    }

    @Override
    public void onBindViewHolder(BackedUpAppViewHolder holder, int position) {
        holder.bindApk(mBackedUpApps.get(position));
    }

    @Override
    public int getItemCount() {
        return mBackedUpApps.size();
    }

    public class BackedUpAppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView appName;
        ImageView appIcon;
        View more;
        BackedUpApp backedUpApp;
        PopupMenu popupMenu;

        public BackedUpAppViewHolder(View itemView) {
            super(itemView);
            appName = ViewUtils.findViewById(itemView, R.id.backed_up_app_name);
            appIcon = ViewUtils.findViewById(itemView, R.id.backed_up_app_icon);
            more = ViewUtils.findViewById(itemView, R.id.backed_up_app_options);

            setupPopupMenu();

            itemView.setOnClickListener(this);
        }

        private void setupPopupMenu() {
            popupMenu = new PopupMenu(more.getContext(), more);
            popupMenu.getMenuInflater().inflate(R.menu.menu_apk_file_list_item, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.action_share:
                            mListener.shareApk((BackedUpApp) more.getTag());
                            return true;
                        case R.id.action_delete:
                            mListener.deleteApk((BackedUpApp) more.getTag());
                            return true;
                    }
                    return false;
                }
            });


            more.setOnTouchListener(popupMenu.getDragToOpenListener());
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupMenu.show();
                }
            });
        }

        public void bindApk(BackedUpApp backedUpApp) {
            this.backedUpApp = backedUpApp;
            appName.setText(backedUpApp.getLabel());
            appIcon.setImageDrawable(backedUpApp.getIcon());
            more.setTag(backedUpApp);
        }

        @Override
        public void onClick(View v) {
            mListener.installApk(backedUpApp);
        }

    }

    public void setData(List<BackedUpApp> apkFilesList) {
        if (apkFilesList == null) {
            apkFilesList = new ArrayList<>();
        }
        mBackedUpApps = apkFilesList;

        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return mBackedUpApps.get(position).hashCode();
    }

    public interface OnApkInteractionListener {
        void shareApk(BackedUpApp backedUpApp);

        void deleteApk(BackedUpApp backedUpApp);

        void installApk(BackedUpApp backedUpApp);
    }
}
