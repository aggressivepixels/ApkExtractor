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
import com.apkextractor.android.util.ViewUtils;

import java.util.List;

public class ContributorsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Contributor> mContributors;

    public ContributorsListAdapter(Context context, List<Contributor> contributors) {
        mContext = context;
        mContributors = contributors;
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

        ViewUtils.setText(convertView, mContributors.get(position).getTitle(), R.id.contributor_contribution);
        ViewUtils.setText(convertView, mContributors.get(position).getPerson(), R.id.contributor_name);

        convertView.findViewById(R.id.contributor_google_plus_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mContributors.get(position).getGooglePlusUrl())));
            }
        });
        return convertView;
    }
}
