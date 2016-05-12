package com.apkextractor.android.contributors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.apkextractor.android.R;
import com.apkextractor.android.common.utils.ViewUtils;

import java.util.List;

public class ContributorsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Contribution> mContributors;

    public ContributorsListAdapter(Context context, List<Contribution> contributions) {
        mContext = context;
        mContributors = contributions;
    }

    @Override
    public int getCount() {
        return mContributors.size();
    }

    @Override
    public Object getItem(int position) {
        return mContributors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contributor, parent, false);

        ViewUtils.setText(convertView, mContributors.get(position).getName(), R.id.contribution_name);
        ViewUtils.setText(convertView, mContributors.get(position).getContributorName(), R.id.contribution_contributor_name);

        convertView.findViewById(R.id.contribution_contributor_link_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mContributors.get(position).getContributorLink())));
            }
        });
        return convertView;
    }
}
