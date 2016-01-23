package com.jonathan.apkextractor.contributors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.jonathan.apkextractor.R;
import com.jonathan.apkextractor.util.ViewUtils;

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
        convertView = LayoutInflater.from(mContext).inflate(R.layout.contributor_list_item, parent, false);

        ViewUtils.setText(convertView, mContributors.get(position).getTitle(), android.R.id.text1);
        ViewUtils.setText(convertView, mContributors.get(position).getPerson(), android.R.id.text2);

        convertView.findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mContributors.get(position).getGooglePlusUrl())));
            }
        });
        return convertView;
    }
}
