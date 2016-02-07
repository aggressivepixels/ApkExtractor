package com.jonathan.apkextractor.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jonathan.apkextractor.R;
import com.jonathan.apkextractor.loader.ApkFile;
import com.jonathan.apkextractor.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

public class ApkFilesAdapter extends RecyclerView.Adapter<ApkFilesAdapter.ApkViewHolder> {

    private LayoutInflater mInflater;
    private List<ApkFile> mApkFiles;
    private OnApkInteractionListener mListener;

    private View.OnClickListener mShowPopupListener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    showPopupMenu(view);
                }
            });
        }
    };

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

        public ApkViewHolder(View itemView) {
            super(itemView);
            appName = ViewUtils.findViewById(itemView, android.R.id.title);
            appIcon = ViewUtils.findViewById(itemView, android.R.id.icon);
            more = ViewUtils.findViewById(itemView, android.R.id.button1);

            more.setOnClickListener(mShowPopupListener);
            itemView.setOnClickListener(this);
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

    private List<ApkFile> getData() {
        return mApkFiles;
    }

    private void showPopupMenu(View view) {
        // Retrieve the clicked item from view's tag
        final ApkFile item = (ApkFile) view.getTag();

        // Create a PopupMenu, giving it the clicked view for an anchor
        PopupMenu popup = new PopupMenu(view.getContext(), view);

        // Inflate our menu resource into the PopupMenu's Menu
        popup.getMenuInflater().inflate(R.menu.menu_apk_file_list_item, popup.getMenu());

        // Set a listener so we are notified if a menu item is clicked
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_share:
                        mListener.shareApk(item);
                        return true;
                    case R.id.action_delete:
                        mListener.deleteApk(item);
                        return true;
                }
                return false;
            }
        });

        // Finally show the PopupMenu
        popup.show();
    }

    public interface OnApkInteractionListener {
        void shareApk(ApkFile apkFile);

        void deleteApk(ApkFile apkFile);

        void installApk(ApkFile apkFile);
    }
}
