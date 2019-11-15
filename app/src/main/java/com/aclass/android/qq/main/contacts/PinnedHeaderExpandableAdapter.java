package com.aclass.android.qq.main.contacts;

import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aclass.android.qq.R;

import java.util.List;

/**
 * Created by 24015 on 2019/11/14.
 */

public class PinnedHeaderExpandableAdapter extends BaseExpandableListAdapter implements HeaderAdapter {
    private List<GroupTitleInfo> list;
    private Context mcontext;
    private PinnedHeaderExpandableListView listView;
    private LayoutInflater inflater;

    public PinnedHeaderExpandableAdapter(List<GroupTitleInfo> list
            , Context context, PinnedHeaderExpandableListView listView){
        this.list=list;
        this.mcontext = context;
        this.listView = listView;
        inflater = LayoutInflater.from(this.mcontext);
    }
//子的对象
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).getInfo().get(childPosition);
    }
/*
*
* */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
/*
* isLastChild子条目内容
* */
    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

     View view=null;
        String isZaiXian=null;
        if(convertView!=null)
        {
            view=convertView;
        }
        else{
            view=createChildrenView();
        }
        ContentInfo contentInfo=list.get(groupPosition).getInfo().get(childPosition);
        TextView tv_beizhu= (TextView) view.findViewById(R.id.child_beizhu);
        TextView tv_isHide= (TextView) view.findViewById(R.id.child_isHide);
        TextView tv_qianming= (TextView) view.findViewById(R.id.child_qianming);
        tv_beizhu.setText(contentInfo.getBeiZhu());
        if(contentInfo.getIsHide()==0)
        {
            isZaiXian="[在线]";
        }
        else
        {
            isZaiXian="[离线]";
        }

        tv_isHide.setText(isZaiXian);
        tv_qianming.setText(contentInfo.getQianming());


        return view;
    }
/*根据分组类别获取成员数目
* */
    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).getInfo().size();
    }
/*
* 组的对象,包括组名和组里面成员
* */
    @Override
    public Object getGroup(int groupPosition) {

        return list.get(groupPosition);
    }
//组数
    @Override
    public int getGroupCount()
    {
        return list.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
/*
* is Extandade展开列表
* */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = createGroupView();
        }

        ImageView iv = (ImageView)view.findViewById(R.id.groupIcon);

        if (isExpanded) {
            iv.setImageResource(R.drawable.btn_browser2);
        }
        else{
            iv.setImageResource(R.drawable.btn_browser);
        }

        TextView text = (TextView)view.findViewById(R.id.groupto);
        text.setText(list.get(groupPosition).getQQGroupName());
        return view;
    }
//当子条目ID相同时是否复用
    @Override
    public boolean hasStableIds() {
        return true;
    }
//子条目是否可以被点击
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View createChildrenView() {
        return inflater.inflate(R.layout.child, null);
    }

    private View createGroupView() {
        return inflater.inflate(R.layout.group, null);
    }

    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        final int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_PUSHED_UP;
        } else if (childPosition == -1
                && !listView.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }
    }

    @Override
    public void configureHeader(View header, int groupPosition,
                                int childPosition, int alpha) {
        String groupData = this.list.get(groupPosition).getQQGroupName();
        ((TextView) header.findViewById(R.id.groupto)).setText(groupData);

    }

    private SparseIntArray groupStatusMap = new SparseIntArray();

    @Override
    public void setGroupClickStatus(int groupPosition, int status) {
        groupStatusMap.put(groupPosition, status);
    }

    @Override
    public int getGroupClickStatus(int groupPosition) {
        if (groupStatusMap.keyAt(groupPosition)>=0) {
            return groupStatusMap.get(groupPosition);
        } else {
            return 0;
        }
    }

}
