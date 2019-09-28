package com.example.cardviewgridviewsample.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.cardviewgridviewsample.tabs.TabListFragment;
import com.example.cardviewgridviewsample.tabs.TabStatFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int tabCount;

    public PagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int i) {
        //returning the current tabs
        switch (i){
            case 0:
                TabListFragment tab1 = new TabListFragment();
                return tab1;
            case 1:
                TabStatFragment tab2 = new TabStatFragment();
                return tab2;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
