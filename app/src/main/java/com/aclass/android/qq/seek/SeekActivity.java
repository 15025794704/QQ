package com.aclass.android.qq.seek;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivitySeekBinding;

/**
 * 搜索 QQ 好友，搜索群组
 */
public class SeekActivity extends GeneralActivity {
    private ActivitySeekBinding mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViews = ActivitySeekBinding.inflate(getLayoutInflater());
        setContentView(mViews.getRoot());

        MyToolbar toolbar = mViews.seekToolbar;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViews.seekPager.setAdapter(new SeekPagerAdapter(this, getSupportFragmentManager()));
        mViews.seekTabs.setupWithViewPager(mViews.seekPager);
    }

    @Override
    protected void consumeInsets(Rect insets) {
        Toolbar tb = mViews.seekToolbar;
        tb.setPadding(tb.getPaddingStart(), insets.top, tb.getPaddingEnd(), tb.getPaddingBottom());
    }
}
