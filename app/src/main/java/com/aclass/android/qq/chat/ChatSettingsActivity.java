package com.aclass.android.qq.chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aclass.android.qq.R;
import com.aclass.android.qq.common.GraphicsUtil;
import com.aclass.android.qq.common.ThemeUtil;
import com.aclass.android.qq.custom.GeneralActivity;

public abstract class ChatSettingsActivity extends GeneralActivity
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    protected ChatSettingsViewModel chatSettingsViewModel;

    protected void consumeInsets(Rect insets, Toolbar toolbar, LinearLayout container) {
        toolbar.setPadding(toolbar.getPaddingStart(), insets.top, toolbar.getPaddingEnd(), toolbar.getPaddingBottom());
        container.setPadding(container.getPaddingStart(), container.getPaddingTop(), container.getPaddingEnd(), insets.bottom);
    }

    protected void bindDataProfilePhoto(Context context, TextView view, Bitmap bitmap){
        int colorOption = ThemeUtil.getColor(context, R.attr.mColorOptionGo);
        Drawable[] drawables = view.getCompoundDrawablesRelative();
        drawables[2].setTint(colorOption);
        Drawable profilePhoto = null;
        if (bitmap != null) {
            int length = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50f, context.getResources().getDisplayMetrics()));
            profilePhoto = new BitmapDrawable(getResources(), GraphicsUtil.round(bitmap));
            profilePhoto.setBounds(0, 0, length, length);
        }
        view.setCompoundDrawablesRelative(profilePhoto, null, drawables[2], null);
    }

    protected void changePinnedTop(boolean newValue){
        Settings settings = chatSettingsViewModel.getSettings().getValue();
        if (settings == null) return;
        if (settings.isPinnedTop == newValue) return;
        settings.isPinnedTop = newValue;
        updateData();
    }

    protected void changeDND(boolean newValue){
        Settings settings = chatSettingsViewModel.getSettings().getValue();
        if (settings == null) return;
        if (settings.isDND == newValue) return;
        settings.isDND = newValue;
        updateData();
    }

    protected void changeHidden(boolean newValue){
        Settings settings = chatSettingsViewModel.getSettings().getValue();
        if (settings == null) return;
        if (settings.isHidden == newValue) return;
        settings.isHidden = newValue;
        updateData();
    }

    protected abstract void updateData();
}
