package com.aclass.android.qq.main;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aclass.android.qq.R;
import com.aclass.android.qq.custom.GeneralFragment;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.FragmentMainBinding;
import com.aclass.android.qq.seek.SeekActivity;

public class MainFragment extends GeneralFragment implements Toolbar.OnMenuItemClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private FragmentMainBinding mViews;
    private FragmentActivity mActivity;

    public static MainFragment newInstance(){
        return new MainFragment();
    }

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViews = FragmentMainBinding.inflate(inflater, container, false);
        return mViews.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        mViews.mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mViews.mainToolbar.setOnMenuItemClickListener(this);
        mViews.mainBottomNav.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar toolbar = mViews.mainToolbar;
        toolbar.setPadding(toolbar.getPaddingStart(), insets.top, toolbar.getPaddingEnd(), toolbar.getPaddingBottom());
        BottomNavigationView bottomNav= mViews.mainBottomNav;
        bottomNav.setPadding(bottomNav.getPaddingStart(), bottomNav.getPaddingTop(), bottomNav.getPaddingEnd(), insets.bottom);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainToolbarMessagesMore: // more options
                startActivity(new Intent(mActivity, SeekActivity.class));
                return true;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.mainBottomNavMessages:
                return true;
            case R.id.mainBottomNavContacts:
                return true;
            case R.id.mainBottomNavExplore:
                return true;
        }
        return true;
    }

}
