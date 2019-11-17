package com.aclass.android.qq.main.contacts;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

/**
 * Created by 24015 on 2019/11/14.
 */

public class PinnedHeaderExpandableListView extends ExpandableListView implements AbsListView.OnScrollListener,ExpandableListView.OnGroupClickListener {
    public PinnedHeaderExpandableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        registerListener();
    }

    public PinnedHeaderExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        registerListener();
    }

    public PinnedHeaderExpandableListView(Context context) {
        super(context);
        registerListener();
    }
    private static final int MAX_ALPHA=255;
    private HeaderAdapter mAdapter;
    /*
    * 用于在列表头显示的View,mHeaderViewVisible为true才可见
    * */
    private View mHeaderView;
    /*
    * 列表头是否可见
    * */
    private boolean mHeaderViewVisible;
    private int mHeaderViewWidth;//列表头的宽度
    private int mHeaderViewHeight;//列表头的高度

    public void setHeaderView(View view){
        mHeaderView=view;
        //
        AbsListView.LayoutParams lp=new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        if(mHeaderView!=null)
        {
            setFadingEdgeLength(0);
        }
        requestLayout();//

    }
    private void registerListener()
    {
        setOnScrollListener(this);//
        setOnGroupClickListener(this);//
    }
    /*
    * 点击HeaderView触发的事件
    * */
    private void headerViewClick()
    {
        long packedPosition =getExpandableListPosition(this.getFirstVisiblePosition());//
        int groupPosition= ExpandableListView.getPackedPositionGroup(packedPosition);
        if(mAdapter.getGroupClickStatus(groupPosition)==1){
            this.collapseGroup(groupPosition);
            mAdapter.setGroupClickStatus(groupPosition,0);
        }
        else
        {
            this.expandGroup(groupPosition);
            mAdapter.setGroupClickStatus(groupPosition,1);

        }
        this.setSelectedGroup(groupPosition);
    }
    private float mDownX;
    private float mDownY;

    /*
    * 假设HeaderView是可见的，此函数用于推断是否点击了HeaderView,并做对应的处理，
    * 由于HeaderView是画上去的，所以设置事件监听是无效的，仅仅有自行控制.
    * */
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if(mHeaderViewVisible){
            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mDownX=ev.getX();
                    mDownY=ev.getY();
                    if(mDownX<=mHeaderViewWidth&&mDownY<=mHeaderViewHeight){
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    float x=ev.getX();
                    float y=ev.getY();
                    float offsetX= Math.abs(x-mDownX);
                    float  offsetY= Math.abs(y-mDownY);
                    //假设HeaderView是可见的，点击在HeaderView内，那么触发headerClick()事件
                    if (x <= mHeaderViewWidth && y <= mHeaderViewHeight
                            && offsetX <= mHeaderViewWidth && offsetY <= mHeaderViewHeight) {
                        if (mHeaderView != null) {
                            headerViewClick();
                        }

                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void setAdapter(ExpandableListAdapter adapter) {
        super.setAdapter(adapter);
        mAdapter = (HeaderAdapter) adapter;
    }

    /**
     *
     * 点击了 Group 触发的事件 , 要依据依据当前点击 Group 的状态来
     */
    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        if (mAdapter.getGroupClickStatus(groupPosition) == 0) {
            mAdapter.setGroupClickStatus(groupPosition, 1);
            parent.expandGroup(groupPosition);
            //Header自己主动置顶
            //parent.setSelectedGroup(groupPosition);

        } else if (mAdapter.getGroupClickStatus(groupPosition) == 1) {
            mAdapter.setGroupClickStatus(groupPosition, 0);
            parent.collapseGroup(groupPosition);
        }

        // 返回 true 才干够弹回第一行 , 不知道为什么
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        }
    }

    private int mOldState = -1;

    /*@Override
    protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
      *//*  super.onLayout(changed, left, top, right, bottom);
        final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
        final int groupPos = ExpandableListView.getPackedPositionGroup(flatPostion);
        final int childPos = ExpandableListView.getPackedPositionChild(flatPostion);
        *//**//*int state = mAdapter.getHeaderState(groupPos, childPos);
        if (mHeaderView != null && mAdapter != null && state != mOldState) {
            mOldState = state;
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
        }
*//*
       *//* configureHeaderView(groupPos, childPos);*//*
    }*/

    public void configureHeaderView(int groupPosition, int childPosition) {
        if (mHeaderView == null || mAdapter == null
                || ((ExpandableListAdapter) mAdapter).getGroupCount() == 0) {
            return;
        }

        int state = mAdapter.getHeaderState(groupPosition, childPosition);

        switch (state) {
            case HeaderAdapter.PINNED_HEADER_GONE: {
                mHeaderViewVisible = false;
                break;
            }

            case HeaderAdapter.PINNED_HEADER_VISIBLE: {
                mAdapter.configureHeader(mHeaderView, groupPosition,childPosition, MAX_ALPHA);

                if (mHeaderView.getTop() != 0){
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }

                mHeaderViewVisible = true;

                break;
            }

            case HeaderAdapter.PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);
                int bottom = firstView.getBottom();

                // intitemHeight = firstView.getHeight();
                int headerHeight = mHeaderView.getHeight();

                int y;

                int alpha;

                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);
                    alpha = MAX_ALPHA * (headerHeight + y) / headerHeight;
                } else {
                    y = 0;
                    alpha = MAX_ALPHA;
                }

                mAdapter.configureHeader(mHeaderView, groupPosition,childPosition, alpha);

                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
                }

                mHeaderViewVisible = true;
                break;
            }
        }
    }

    @Override
    /**
     * 列表界面更新时调用该方法(如滚动时)
     */
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            //分组栏是直接绘制到界面中。而不是增加到ViewGroup中
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }




    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        final long flatPos = getExpandableListPosition(firstVisibleItem);
        int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
        int childPosition = ExpandableListView.getPackedPositionChild(flatPos);
        configureHeaderView(groupPosition,childPosition);
    }


}
