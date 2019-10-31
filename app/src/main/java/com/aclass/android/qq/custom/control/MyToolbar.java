package com.aclass.android.qq.custom.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;

import com.aclass.android.qq.R;

/**
 * Created by Administrator on 2019/10/30.
 */

public class MyToolbar extends Toolbar{
    private int menuStyle=0;
    private int NavigationIcon=0;

    public MyToolbar(Context context){
        super(context);
    }

    public MyToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void obtainStyledAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyToolbar, defStyleAttr, 0);
        menuStyle = a.hasValue(R.styleable.MyToolbar_menuStyle) ? a.getInt(R.styleable.MyToolbar_menuStyle, menuStyle) : menuStyle;
        NavigationIcon = a.hasValue(R.styleable.MyToolbar_navigationIcon) ? a.getInt(R.styleable.MyToolbar_navigationIcon, NavigationIcon) : NavigationIcon;
        a.recycle();
    }

    private void initBar() {
        if(menuStyle!=0)
            this.inflateMenu(menuStyle);
        if(NavigationIcon!=0)
            this.setNavigationIcon(NavigationIcon);
    }
}
