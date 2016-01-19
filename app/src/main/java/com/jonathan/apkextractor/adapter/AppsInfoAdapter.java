package com.jonathan.apkextractor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jonathan.apkextractor.R;
import com.jonathan.apkextractor.loader.AppEntry;
import com.jonathan.apkextractor.utils.ViewUtils;

import java.util.List;

public class AppsInfoAdapter extends RecyclerView.Adapter<AppsInfoAdapter.AppViewHolder> {

    private LayoutInflater mInflater;
    private List<AppEntry> mAppsInfo;
    private OnAppInteractionListener mListener;

    public AppsInfoAdapter(Context context, List<AppEntry> appsInfo, OnAppInteractionListener listener) {
        mInflater = LayoutInflater.from(context);
        mAppsInfo = appsInfo;
        mListener = listener;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppViewHolder(mInflater.inflate(R.layout.app_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        holder.bindApp(mAppsInfo.get(position));
    }

    @Override
    public int getItemCount() {
        return mAppsInfo.size();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView appName, appPackage;
        ImageView appIcon;
        AppEntry appInfo;

        public AppViewHolder(View itemView) {
            super(itemView);
            appName = ViewUtils.findViewById(itemView, android.R.id.title);
            appPackage = ViewUtils.findViewById(itemView, android.R.id.summary);
            appIcon = ViewUtils.findViewById(itemView, android.R.id.icon);
            itemView.setOnClickListener(this);
        }

        public void bindApp(AppEntry appInfo) {
            this.appInfo = appInfo;
            appName.setText(appInfo.getLabel());
            appPackage.setText(appInfo.getApplicationInfo().packageName);
            appIcon.setImageDrawable(appInfo.getIcon());
        }

        @Override
        public void onClick(View v) {
            dispatchAppClick(appInfo);
        }

    }

    private void dispatchAppClick(AppEntry appInfo) {
        mListener.onAppClick(appInfo);
    }

    public interface OnAppInteractionListener {
        void onAppClick(AppEntry appInfo);
    }
}
