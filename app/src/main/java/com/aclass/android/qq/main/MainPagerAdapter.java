package com.aclass.android.qq.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private MainFragment fragmentMain;
    private DrawerFragment fragmentDrawer;

    public MainPagerAdapter(Context context, FragmentManager manager){
        super(manager);
        mContext = context;
    }

    private MainFragment getMain(){
        if (fragmentMain == null) fragmentMain = MainFragment.newInstance();
        return fragmentMain;
    }

    private DrawerFragment getDrawer(){
        if (fragmentDrawer == null) fragmentDrawer = DrawerFragment.newInstance();
        return fragmentDrawer;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1: return getMain();
            case 0: default: return getDrawer();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
