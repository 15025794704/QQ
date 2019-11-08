package com.aclass.android.qq.main;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aclass.android.qq.SettingsActivity;
import com.aclass.android.qq.custom.GeneralFragment;
import com.aclass.android.qq.databinding.FragmentDrawerBinding;

public class DrawerFragment extends GeneralFragment {
    private FragmentDrawerBinding mViews;
    private FragmentActivity mActivity;

    public static DrawerFragment newInstance(){
        return new DrawerFragment();
    }

    public DrawerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViews = FragmentDrawerBinding.inflate(inflater, container, false);
        return mViews.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mViews.drawerSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, SettingsActivity.class));
            }
        });
    }

    @Override
    protected void consumeInsets(Rect insets) {
        mViews.drawerGB.setGuidelineEnd(insets.bottom);
    }

}
