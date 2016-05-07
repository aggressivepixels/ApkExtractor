package com.apkextractor.android.contributors;

import android.widget.LinearLayout;
import android.widget.ListAdapter;

public class LinearLayoutAdapterContainer {

    private LinearLayout mLinearLayout;

    public LinearLayoutAdapterContainer(LinearLayout linearLayout) {
        mLinearLayout = linearLayout;
    }

    public void setAdapter(ListAdapter listAdapter) {
        mLinearLayout.removeAllViews();
        if (listAdapter != null) {
            for (int i = 0; i < listAdapter.getCount(); i++) {
                mLinearLayout.addView(listAdapter.getView(i, null, mLinearLayout));
            }
        }
    }
}
