package com.apkextractor.android.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.apkextractor.android.R;
import com.apkextractor.android.loader.AppEntry;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.tonicartos.superslim.GridSLM;
import com.tonicartos.superslim.LinearSLM;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Jonathan Hernández
 */
public class InstalledAppsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    //View types that this Adapter can handle
    private static final int VIEW_TYPE_APP = 1;
    private static final int VIEW_TYPE_HEADER = 2;

    private LayoutInflater inflater;
    private List<SlimItem> items = new ArrayList<>();
    private OnAppClickListener listener;

    public InstalledAppsAdapter(Context context, OnAppClickListener listener) {
        inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getData() instanceof String) {
            return VIEW_TYPE_HEADER;
        } else if (items.get(position).getData() instanceof AppEntry) {
            return VIEW_TYPE_APP;
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(
                        inflater.inflate(
                                R.layout.installed_apps_section_header,
                                parent,
                                false));
            case VIEW_TYPE_APP:
                return new AppViewHolder(
                        inflater.inflate(
                                R.layout.item_app,
                                parent,
                                false),
                        listener);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object data = items.get(position).getData();
        if (data instanceof String && holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).setText((String) data);
        } else if (data instanceof AppEntry && holder instanceof AppViewHolder) {
            ((AppViewHolder) holder).bindApp((AppEntry) data);
        }
        final GridSLM.LayoutParams layoutParams = GridSLM.LayoutParams.from(holder.itemView.getLayoutParams());
        layoutParams.setSlm(LinearSLM.ID);
        layoutParams.setFirstPosition(items.get(position).getSectionFirstPosition());
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setApps(List<AppEntry> apps) {
        items.clear();
        //Insert headers into list of items.
        String lastHeader = "";
        int headerCount = 0;
        int sectionFirstPosition = 0;
        for (int i = 0; i < apps.size(); i++) {
            String header = apps.get(i).getLabel().substring(0, 1);
            if (!TextUtils.equals(lastHeader, header)) {
                // Insert new header view and update section data.
                sectionFirstPosition = i + headerCount;
                lastHeader = header;
                headerCount += 1;
                items.add(new SlimItem(header, sectionFirstPosition));
            }
            items.add(new SlimItem(apps.get(i), sectionFirstPosition));
        }
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        Object data = items.get(position).getData();
        if (data instanceof String) {
            return ((String) data).substring(0, 1).toUpperCase(Locale.getDefault());
        } else if (data instanceof AppEntry) {
            return ((AppEntry) data).getLabel().substring(0, 1).toUpperCase(Locale.getDefault());
        }
        return "";
    }

    public interface OnAppClickListener {
        void onAppClick(AppEntry appInfo);
    }
}
