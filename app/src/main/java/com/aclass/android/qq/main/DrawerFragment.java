package com.aclass.android.qq.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aclass.android.qq.common.GraphicsUtil;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.settings.SettingsActivity;
import com.aclass.android.qq.custom.GeneralFragment;
import com.aclass.android.qq.databinding.FragmentDrawerBinding;

/**
 * 应用抽屉页面，从主页面右滑进入
 */
public class DrawerFragment extends GeneralFragment {
    private FragmentDrawerBinding mViews;
    private MainActivity mActivity;

    public static DrawerFragment newInstance(){
        return new DrawerFragment();
    }

    public DrawerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViews = FragmentDrawerBinding.inflate(inflater, container, false);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(Attribute.currentAccount!=null) {
                    Bitmap navIcon = GraphicsUtil.round(Attribute.currentAccountProfilePhoto);
                    mViews.sideHead.setImageBitmap(navIcon);
                    mViews.name.setText(Attribute.currentAccount.getNiCheng());
                    mViews.myMessage.setText(Attribute.currentAccount.getQianMing());
                }
            }
        }, 600);

        return mViews.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (MainActivity) getActivity();

        // 点击关闭按钮切换至应用主页面
        mViews.closeSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.setPagerItem(1);
            }
        });

        mViews.recordView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(Attribute.screen.getposHeight()*(6.0/121))));
        mViews.detailMenu.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(Attribute.screen.getposHeight()*(70.0/121))));

        // 进入设置页面
        mViews.install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, SettingsActivity.class));
            }
        });
    }

    @Override
    protected void consumeInsets(Rect insets) {

    }

}
