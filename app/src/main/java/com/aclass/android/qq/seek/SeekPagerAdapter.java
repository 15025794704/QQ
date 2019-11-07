package com.aclass.android.qq.seek;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SeekPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private SeekPersonFragment fragmentSeekPerson;
    private SeekGroupFragment fragmentSeekGroup;

    public SeekPagerAdapter(Context context, FragmentManager manager){
        super(manager);
        mContext = context;
    }

    private SeekPersonFragment getSeekPerson(){
        if (fragmentSeekPerson == null) fragmentSeekPerson = SeekPersonFragment.newInstance();
        return fragmentSeekPerson;
    }

    private SeekGroupFragment getSeekGroup(){
        if (fragmentSeekGroup == null) fragmentSeekGroup = SeekGroupFragment.newInstance();
        return fragmentSeekGroup;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1: return getSeekGroup();
            case 0: default: return getSeekPerson();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 1: return "找群";
            case 0: default: return "找人";
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
