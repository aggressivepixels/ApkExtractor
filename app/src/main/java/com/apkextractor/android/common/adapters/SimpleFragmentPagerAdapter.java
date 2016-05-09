package com.apkextractor.android.common.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.apkextractor.android.common.utils.RtlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple {@link FragmentPagerAdapter} that allows to add {@link Fragment Fragments}
 * directly without subclassing the {@link FragmentPagerAdapter}. It also has RTL support.
 *
 * @author Jonathan Hernández
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments = new ArrayList<>();
    private List<String> pageTitles = new ArrayList<>();

    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(RtlUtils.getPositionForTextDirection(position, fragments.size()));
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(RtlUtils.getPositionForTextDirection(position, pageTitles.size()));
    }

    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        pageTitles.add(title);
    }
}
