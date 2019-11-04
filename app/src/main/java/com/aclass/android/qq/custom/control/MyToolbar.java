package com.aclass.android.qq.custom.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;

import com.aclass.android.qq.R;

/**
 * Created by Administrator on 2019/10/30.
 */


//自定义的Toolbar
public class MyToolbar extends Toolbar{
    /**
     * 选项资源
     */
    private int mMenuRes = 0;
    /**
     * 导航图标颜色
     */
    private int mNavIconTint = Color.BLACK;
    /**
     * 选项图标颜色
     */
    private int mMenuIconTint = Color.BLACK;

    public MyToolbar(Context context){
        super(context);
    }

    public MyToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        obtainStyledAttrs(context, attrs, defStyleAttr);
        initBar();
    }

    private void obtainStyledAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyToolbar, defStyleAttr, 0);
        mMenuRes = a.hasValue(R.styleable.MyToolbar_menu) ? a.getResourceId(R.styleable.MyToolbar_menu, mMenuRes) : mMenuRes;
        mNavIconTint = a.hasValue(R.styleable.MyToolbar_navigationIconTint) ? a.getColor(R.styleable.MyToolbar_navigationIconTint, mNavIconTint) : mNavIconTint;
        mMenuIconTint = a.hasValue(R.styleable.MyToolbar_menuIconTint) ? a.getColor(R.styleable.MyToolbar_menuIconTint, mMenuIconTint) : mMenuIconTint;
        a.recycle();
    }

    private void initBar() {
        // 设置导航按钮图标颜色
        if (mNavIconTint != Color.BLACK){
            Drawable navIcon = getNavigationIcon();
            if (navIcon != null) navIcon.setTint(mNavIconTint);
        }
        // 工具栏
        if(mMenuRes != 0) this.inflateMenu(mMenuRes);
        // 设置选项按钮图标颜色
        if (mMenuIconTint != Color.BLACK){
            Menu menu = getMenu();
            for (int i = 0; i < menu.size(); i++){
                MenuItem item = menu.getItem(i);
                Drawable icon = item.getIcon();
                if (icon == null) continue;
                icon.setTint(mMenuIconTint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 设置最小高度以使导航按钮能够正常地居中显示
        setMinimumHeight(getMeasuredHeight() - getPaddingTop() - getPaddingBottom());
    }
}
