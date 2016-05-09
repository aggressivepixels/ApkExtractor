package com.apkextractor.android.installedapps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apkextractor.android.R;
import com.apkextractor.android.installedapps.loader.AppEntry;
import com.apkextractor.android.common.utils.ViewUtils;

public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView appName, appPackage;
    private ImageView appIcon;
    private AppEntry appEntry;
    private InstalledAppsAdapter.OnAppClickListener listener;

    public AppViewHolder(View itemView, InstalledAppsAdapter.OnAppClickListener listener) {
        super(itemView);
        appName = ViewUtils.findViewById(itemView, R.id.app_name);
        appPackage = ViewUtils.findViewById(itemView, R.id.app_package);
        appIcon = ViewUtils.findViewById(itemView, R.id.app_icon);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    public void bindApp(AppEntry appInfo) {
        this.appEntry = appInfo;
        appName.setText(appInfo.getLabel());
        appPackage.setText(appInfo.getApplicationInfo().packageName);
        appIcon.setImageDrawable(appInfo.getIcon());
    }

    @Override
    public void onClick(View v) {
        listener.onAppClick(appEntry);
    }
}
