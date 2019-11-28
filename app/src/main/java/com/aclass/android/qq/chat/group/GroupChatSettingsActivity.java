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
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.aclass.android.qq.MyDataActivity;
import com.aclass.android.qq.R;
import com.aclass.android.qq.chat.ChatSettingsActivity;
import com.aclass.android.qq.common.GraphicsUtil;
import com.aclass.android.qq.common.ThemeUtil;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.databinding.ActivityGroupChatSettingsBinding;
import com.aclass.android.qq.entity.Member;
import com.aclass.android.qq.tools.MyDateBase;

import java.util.List;


/**
 * QQ 群聊天设置页面
 */
public class GroupChatSettingsActivity extends ChatSettingsActivity implements Toolbar.OnMenuItemClickListener {

    public static String ARG_NUM = "groupNum";
    private String number;

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
        number = intent.getStringExtra(ARG_NUM);
        if (number == null) {
            finish();
            return;
        }

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
        chatSettingsViewModel = mViewModel;
        mViewModel.settings.observe(this, new Observer<GroupSettings>() {
            @Override
            public void onChanged(@Nullable GroupSettings groupSettings) {
                if (groupSettings == null) return;
                number = groupSettings.number;
                bindData(groupSettings);
            }
        });
        mViewModel.groupProfilePhoto.observe(this, new Observer<Bitmap>() {
            @Override
            public void onChanged(@Nullable Bitmap bitmap) {
                bindDataProfilePhoto(context, mViews.chatSettingsGroupInfo, bitmap);
            }
        });
        if (mViewModel.settings.getValue() == null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final GroupSettings groupSettings = GroupSettings.get(number, null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewModel.settings.setValue(groupSettings);
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    protected void consumeInsets(Rect insets) {
        consumeInsets(insets, mViews.chatSettingsGroupToolbar, mViews.chatSettingsGroupContainer);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.chatSettingsGroupToolbar: // more options
                return true;
        }
        return false;
    }

    private void bindData(final GroupSettings settings){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDateBase dateBase = new MyDateBase();
                final Bitmap profilePhoto = dateBase.getImageByQQ(settings.number);
                dateBase.Destory();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel.groupProfilePhoto.setValue(profilePhoto);
                    }
                });
            }
        }).start();
        mViews.chatSettingsGroupInfo.setText(settings.name);
        mViews.chatSettingsGroupName.setText(settings.name);
        mViews.chatSettingsGroupNum.setText(settings.number);
        mViews.chatSettingsGroupMemberName.setText(settings.memberName);
        mViews.chatSettingsGroupMemberName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE){
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputMethodManager != null) inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    changeMemberName(v.getText().toString());
                    return true;
                }
                return false;
            }
        });

        Switch[] switches = new Switch[]{
                mViews.chatSettingsGroupPinnedTop,
                mViews.chatSettingsGroupDND,
                mViews.chatSettingsGroupHidden
        };
        for (Switch aSwitch : switches) aSwitch.setOnCheckedChangeListener(null);
        mViews.chatSettingsGroupPinnedTop.setChecked(settings.isPinnedTop);
        mViews.chatSettingsGroupDND.setChecked(settings.isDND);
        mViews.chatSettingsGroupHidden.setChecked(settings.isHidden);
        for (Switch aSwitch : switches) aSwitch.setOnCheckedChangeListener(this);

        bindMembers(this);
    }

    private void bindMembers(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDateBase dateBase = new MyDateBase();
                List<Member> allMembers = dateBase.getMembersByID(number);
                if (allMembers == null) return;
                int total = allMembers.size();
                List<Member> members = allMembers.subList(0, total > 4 ? 3 : total);
                int size = members.size();
                boolean isEnd;
                bindMember(context, mViews.chatSettingsGroupMember1, members.get(0), dateBase);
                isEnd = bindMember(context, mViews.chatSettingsGroupMember2, size > 1 ? members.get(1) : null, dateBase);
                isEnd = bindMember(context, mViews.chatSettingsGroupMember3, size > 2 ? members.get(2) : null, isEnd ? null : dateBase);
                bindMember(context, mViews.chatSettingsGroupMember4, size > 3 ? members.get(3) : null, isEnd ? null : dateBase);
                bindMember(context, mViews.chatSettingsGroupMember5, null, null);
                dateBase.Destory();
            }
        }).start();
    }

    private boolean bindMember(final Context context, final TextView view, final Member member, MyDateBase dateBase){
        final boolean isEnd = member == null;
        boolean isBlank = isEnd && dateBase == null;
        if (isBlank) return true;
        final String text;
        final Drawable image;
        if (isEnd){
            text = context.getString(R.string.groupChatSettingsAddMember);
            image = context.getDrawable(R.drawable.chat_settings_add);
        } else {
            final Bitmap memberProfilePhoto = dateBase.getImageByQQ(member.getMemberQQ());
            text = member.getNiCheng();
            image = memberProfilePhoto == null ? null : new BitmapDrawable(context.getResources(), GraphicsUtil.round(memberProfilePhoto));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isEnd) return;
                        Intent intent = new Intent(context, MyDataActivity.class);
                        intent.putExtra("qqNum", member.getMemberQQ());
                        startActivity(intent);
                    }
                });
                if (isEnd) view.setTextColor(ThemeUtil.getColor(context, R.attr.mColorOptionGo));
                view.setText(text);
                if (image != null) {
                    int length = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, context.getResources().getDisplayMetrics()));
                    image.setBounds(0, 0, length, length);
                    view.setCompoundDrawablesRelative(null, image, null, null);
                }
            }
        });
        return isEnd;
    }

    @Override
    public void onClick(View v) {
        if (v == null) return;
        switch (v.getId()){
            case R.id.chatSettingsGroupQuitGroup:
                quitGroup();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == null) return;
        switch (buttonView.getId()){
            case R.id.chatSettingsGroupPinnedTop:
                changePinnedTop(isChecked);
                break;
            case R.id.chatSettingsGroupDND:
                changeDND(isChecked);
                break;
            case R.id.chatSettingsGroupHidden:
                changeHidden(isChecked);
                break;
        }
    }

    private void quitGroup(){
    }

    private void changeMemberName(String newValue){
        GroupSettings settings = mViewModel.settings.getValue();
        if (settings == null) return;
        if (settings.memberName.equals(newValue)) return;
        settings.memberName = newValue;
        updateData();
    }

    @Override
    protected void updateData(){
        final GroupSettings settings = mViewModel.settings.getValue();
        if (settings == null) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDateBase dateBase = new MyDateBase();
                int resultGroupAccount = dateBase.updateEntity(settings.toQun());
                int resultGroup = dateBase.updateEntity(settings.toMember());
                dateBase.Destory();
            }
        }).start();
    }
}
