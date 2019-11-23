package com.aclass.android.qq.chat.group;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.aclass.android.qq.BuildConfig;
import com.aclass.android.qq.R;
import com.aclass.android.qq.common.GraphicsUtil;
import com.aclass.android.qq.common.ThemeUtil;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivityGroupChatSettingsBinding;
import com.aclass.android.qq.tools.MyDateBase;


/**
 * QQ 群聊天设置页面
 */
public class GroupChatSettingsActivity extends GeneralActivity implements Toolbar.OnMenuItemClickListener {

    public static String ARG_NUM = "groupNum";
    private String groupNum;

    // DataBinding 对象
    private ActivityGroupChatSettingsBinding mViews;
    private GroupChatSettingsViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DataBinding，摆脱 findViewById
        mViews = ActivityGroupChatSettingsBinding.inflate(getLayoutInflater());
        // 设置页面界面
        setContentView(mViews.getRoot());
        final Context context = this;

        Intent intent = getIntent();
        groupNum = BuildConfig.DEBUG ? "" : intent.getStringExtra(ARG_NUM);

        MyToolbar toolbar = mViews.chatSettingsGroupToolbar;
        // 工具栏选项点击监听器
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mViewModel = ViewModelProviders.of(this).get(GroupChatSettingsViewModel.class);
        mViewModel.groupSettings.observe(this, new Observer<GroupSettings>() {
            @Override
            public void onChanged(@Nullable GroupSettings groupSettings) {
                if (groupSettings == null) return;
                groupNum = groupSettings.groupNum;
                bindData(groupSettings);
            }
        });
        mViewModel.groupProfilePhoto.observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable Bitmap bitmap) {
                if (bitmap == null) return;
                int colorOption = ThemeUtil.getColor(context, R.attr.mColorOptionGo);
                Drawable[] drawables = mViews.chatSettingsGroupInfo.getCompoundDrawablesRelative();
                drawables[2].setTint(colorOption);
                int length = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, context.getResources().getDisplayMetrics()));
                Drawable profilePhoto = new BitmapDrawable(getResources(), GraphicsUtil.round(bitmap));
                profilePhoto.setBounds(0, 0, length, length);
                mViews.chatSettingsGroupInfo.setCompoundDrawablesRelative(profilePhoto, null, drawables[2], null);
            }
        });
        if (mViewModel.groupSettings.getValue() == null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final GroupSettings groupSettings = GroupSettings.get(groupNum, null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewModel.groupSettings.setValue(groupSettings);
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    protected void consumeInsets(Rect insets) {
        MyToolbar tb = mViews.chatSettingsGroupToolbar;
        tb.setPadding(tb.getPaddingStart(), insets.top, tb.getPaddingEnd(), tb.getPaddingBottom());
        LinearLayout container = mViews.chatSettingsGroupContainer;
        container.setPadding(container.getPaddingStart(), container.getPaddingTop(), container.getPaddingEnd(), insets.bottom);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.chatSettingsGroupToolbar: // more options
                return true;
        }
        return false;
    }

    private void bindData(final GroupSettings groupSettings){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDateBase dateBase = new MyDateBase();
                final Bitmap profilePhoto = dateBase.getImageByQQ(groupSettings.groupNum);
                dateBase.Destory();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel.groupProfilePhoto.setValue(profilePhoto);
                    }
                });
            }
        }).start();
        mViews.chatSettingsGroupInfo.setText(groupSettings.groupName);
    }
}
