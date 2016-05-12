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

    /**
     * {@inheritDoc}
     */
    public SimpleFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Fragment getItem(int position) {
        return fragments.get(RtlUtils.getPositionForTextDirection(position, fragments.size()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(RtlUtils.getPositionForTextDirection(position, pageTitles.size()));
    }

    /**
     * Adds a new {@link Fragment} to this {@link FragmentPagerAdapter}.
     * @param fragment The {@link Fragment} to add.
     * @param title The title of the {@link Fragment}.
     */
    public void addFragment(Fragment fragment, String title) {
        fragments.add(fragment);
        pageTitles.add(title);
    }
}
