package com.android.apk.extractor.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.apk.extractor.loader.ApkFile;
import com.android.apkextractor.R;
import com.android.apk.extractor.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class ApkFilesAdapter extends RecyclerView.Adapter<ApkFilesAdapter.ApkViewHolder> {

    private LayoutInflater mInflater;
    private List<ApkFile> mApkFiles;
    private OnApkInteractionListener mListener;

    public ApkFilesAdapter(Context context, List<ApkFile> apkFiles, OnApkInteractionListener listener) {
        mInflater = LayoutInflater.from(context);
        if (apkFiles == null) {
            apkFiles = new ArrayList<>();
        }
        mApkFiles = apkFiles;
        mListener = listener;
    }

    @Override
    public ApkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ApkViewHolder(mInflater.inflate(R.layout.apk_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ApkViewHolder holder, int position) {
        holder.bindApk(mApkFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return mApkFiles.size();
    }

    public class ApkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView appName;
        ImageView appIcon;
        View more;
        ApkFile apkFile;
        PopupMenu popupMenu;

        public ApkViewHolder(View itemView) {
            super(itemView);
            appName = ViewUtils.findViewById(itemView, android.R.id.title);
            appIcon = ViewUtils.findViewById(itemView, android.R.id.icon);
            more = ViewUtils.findViewById(itemView, android.R.id.button1);

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
                            mListener.shareApk((ApkFile) more.getTag());
                            return true;
                        case R.id.action_delete:
                            mListener.deleteApk((ApkFile) more.getTag());
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

        public void bindApk(ApkFile apkFile) {
            this.apkFile = apkFile;
            appName.setText(apkFile.getLabel());
            appIcon.setImageDrawable(apkFile.getIcon());
            more.setTag(apkFile);
        }

        @Override
        public void onClick(View v) {
            mListener.installApk(apkFile);
        }

    }

    public void setData(List<ApkFile> apkFilesList) {
        if (apkFilesList == null) {
            apkFilesList = new ArrayList<>();
        }
        mApkFiles = apkFilesList;

        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return mApkFiles.get(position).hashCode();
    }

    public interface OnApkInteractionListener {
        void shareApk(ApkFile apkFile);

        void deleteApk(ApkFile apkFile);

        void installApk(ApkFile apkFile);
    }
}
