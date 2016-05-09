package com.apkextractor.android.installedapps.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    public HeaderViewHolder(View itemView) {
        super(itemView);
    }

    public void setText(String text) {
        ((TextView) itemView).setText(text);
    }
}