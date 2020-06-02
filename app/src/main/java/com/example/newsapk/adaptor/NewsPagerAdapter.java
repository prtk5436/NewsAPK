package com.example.newsapk.adaptor;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.newsapk.CoronaFragment;
import com.example.newsapk.GamesFragment;
import com.example.newsapk.PolyticsFragment;
import com.example.newsapk.TechFragment;

public class NewsPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 4;

    private String[] tabTitles = new String[]{
            CoronaFragment.NAME,
            GamesFragment.NAME,
            TechFragment.NAME,
            PolyticsFragment.NAME
    };

    private Context mContext;

    public NewsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = Fragment.instantiate(mContext, CoronaFragment.class.getName());
                break;
            case 1:
                fragment = Fragment.instantiate(mContext, GamesFragment.class.getName());
                break;
            case 2:
                fragment = Fragment.instantiate(mContext, TechFragment.class.getName());
                break;
            case 3:
                fragment = Fragment.instantiate(mContext, PolyticsFragment.class.getName());
                break;
        }
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
