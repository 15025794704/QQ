package com.aclass.android.qq.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aclass.android.qq.MyDataActivity;
import com.aclass.android.qq.common.GraphicsUtil;
import com.aclass.android.qq.custom.GeneralFragment;
import com.aclass.android.qq.databinding.FragmentDrawerBinding;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.settings.SettingsActivity;

import java.util.HashMap;

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
        return mViews.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (MainActivity) getActivity();

        if (Attribute.isAccountInitialized){
            initData();
        } else {
            MainActivity.endowAccount(new Runnable() {
                @Override
                public void run() {
                    initData();
                }
            });
        }

        // 点击关闭按钮切换至应用主页面
        mViews.closeSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.setPagerItem(1);
            }
        });

        mViews.recordView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(Attribute.screen.getposHeight()*(6.0/121))));
        mViews.detailMenu.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(Attribute.screen.getposHeight()*(70.0/121))));

        mViews.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mActivity, MyDataActivity.class);
                intent.putExtra("qqNum",Attribute.QQ);
                startActivity(intent);
            }
        });
        // 进入设置页面
        mViews.install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mActivity, SettingsActivity.class));
            }
        });
    }

    private void initData() {
        //初始化-吴
        User account = Attribute.currentAccount;
        Attribute.userInfoList = new HashMap<>();
        Attribute.userHeadList = new HashMap<>();
        Attribute.userInfoList.put(Attribute.QQ, account);
        Attribute.userHeadList.put(Attribute.QQ, Attribute.currentAccountProfilePhoto);
        Bitmap navIcon = GraphicsUtil.round(Attribute.currentAccountProfilePhoto);
        mViews.sideHead.setImageBitmap(navIcon);
        mViews.name.setText(account.getNiCheng());
        mViews.myMessage.setText(account.getQianMing());
    }

    @Override
    protected void consumeInsets(Rect insets) {
        mViews.message.setPadding(mViews.message.getPaddingLeft(),insets.top,mViews.message.getPaddingRight(),mViews.message.getPaddingBottom());
    }

}
