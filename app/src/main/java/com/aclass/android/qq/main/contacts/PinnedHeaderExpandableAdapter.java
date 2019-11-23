package com.aclass.android.qq.main.contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aclass.android.qq.MyDataActivity;
import com.aclass.android.qq.R;
import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.internet.Attribute;

import java.util.List;

/**
 * Created by 24015 on 2019/11/14.
 */

public class PinnedHeaderExpandableAdapter extends BaseExpandableListAdapter implements HeaderAdapter {
    private List<GroupTitleInfo> list;
    private Context mcontext;
    private LayoutInflater inflater;
    private PinnedHeaderExpandableListView listView;
    public PinnedHeaderExpandableAdapter(List<GroupTitleInfo> list
            , Context context){
        this.list=list;
        this.mcontext = context;
        inflater=LayoutInflater.from(mcontext);

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
        final  ContentInfo contentInfo=list.get(groupPosition).getInfo().get(childPosition);
        TextView tv_beizhu= (TextView) view.findViewById(R.id.child_beizhu);
        TextView tv_isHide= (TextView) view.findViewById(R.id.child_isHide);
        TextView tv_qianming= (TextView) view.findViewById(R.id.child_qianming);
        ImageView iv_icon=view.findViewById(R.id.child_icon);
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
       iv_icon.setImageBitmap(contentInfo.getIcon());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext, MyDataActivity.class);
                intent.putExtra("qqNum", contentInfo.getQQ());
                mcontext.startActivity(intent);
            }
        });

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
        if (convertView == null)
            convertView = View.inflate(mcontext, R.layout.group, null);
        ImageView iv = (ImageView)convertView.findViewById(R.id.groupIcon);

        int expandIconResId = isExpanded ? R.drawable.ic_arrow_down_24 : R.drawable.ic_arrow_right_24;
        Drawable expandIcon = mcontext.getDrawable(expandIconResId);
        if (expandIcon != null) expandIcon.setTint(Color.parseColor("#FFC5C5C5"));
        iv.setImageDrawable(expandIcon);

        TextView text = (TextView)convertView.findViewById(R.id.groupto);
        TextView groupFriendIsHide=convertView.findViewById(R.id.group_FriendIsHide);//好友在线人数
        text.setText(list.get(groupPosition).getQQGroupName());
        int counts=list.get(groupPosition).getInfo().size();//总人数
        int zaixian=0;
        for(int i=0;i<list.get(groupPosition).getInfo().size();i++)
        {
            if(list.get(groupPosition).getInfo().get(i).getIsHide()==0)
            {
                zaixian++;
            }
        }
        groupFriendIsHide.setText(zaixian+"/"+counts);


        return convertView;
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



    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        /*final int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_PUSHED_UP;
        } else if (childPosition == -1
                && !listView.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }*/
        return 0;
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

   /* *//*
    * 设置判断是否点击多个组对象的监听
    * *//*
    public void setOnGroupExPanded(OnGroupExpanded listenter)
    {
        this.listenter=listenter;
    }

        interface OnGroupExpanded{
            void onGroupExpanded(int groupPosition);
        }
    OnGroupExpanded listenter;*/


}
