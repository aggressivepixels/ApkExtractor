package com.android.apk.extractor.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.apk.extractor.loader.AppEntry;
import com.android.apkextractor.R;
import com.android.apk.extractor.util.ViewUtils;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppsInfoAdapter extends RecyclerView.Adapter<AppsInfoAdapter.AppViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private LayoutInflater mInflater;
    private List<AppEntry> mAppsInfo;
    private OnAppInteractionListener mListener;

    public AppsInfoAdapter(Context context, List<AppEntry> appsInfo, OnAppInteractionListener listener) {
        mInflater = LayoutInflater.from(context);
        if (appsInfo == null) {
            appsInfo = new ArrayList<>();
        }
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

    @NonNull
    @Override
    public String getSectionName(int position) {
        return mAppsInfo.get(position).getLabel().substring(0, 1);
    }

    public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView appName, appPackage, sectionHeader;
        public ImageView appIcon;
        public AppEntry appInfo;

        public AppViewHolder(View itemView) {
            super(itemView);
            appName = ViewUtils.findViewById(itemView, android.R.id.title);
            appPackage = ViewUtils.findViewById(itemView, android.R.id.summary);
            sectionHeader = ViewUtils.findViewById(itemView, R.id.app_info_section_header);
            appIcon = ViewUtils.findViewById(itemView, android.R.id.icon);
            ViewUtils.findViewById(itemView, R.id.app_info_container).setOnClickListener(this);
        }

        public void bindApp(AppEntry appInfo) {
            this.appInfo = appInfo;
            appName.setText(appInfo.getLabel());
            appPackage.setText(appInfo.getApplicationInfo().packageName);
            appIcon.setImageDrawable(appInfo.getIcon());
            sectionHeader.setText(appInfo.getLabel().substring(0, 1));
            if (isSectionHeader(getAdapterPosition())) {
                sectionHeader.setVisibility(View.VISIBLE);
            } else {
                sectionHeader.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            dispatchAppClick(appInfo);
        }

    }

    private boolean isSectionHeader(int position) {
        return position == 0 || !mAppsInfo.get(position).getLabel().substring(0, 1).toUpperCase(Locale.getDefault()).equals(mAppsInfo.get(position - 1).getLabel().substring(0, 1).toUpperCase(Locale.getDefault()));
    }

    public void setData(List<AppEntry> appEntryList) {
        if (appEntryList == null) {
            appEntryList = new ArrayList<>();
        }
        mAppsInfo = appEntryList;

        notifyDataSetChanged();
    }


    protected List<AppEntry> getData() {
        return mAppsInfo;
    }

    private void dispatchAppClick(AppEntry appInfo) {
        mListener.onAppClick(appInfo);
    }

    public interface OnAppInteractionListener {
        void onAppClick(AppEntry appInfo);
    }
}
