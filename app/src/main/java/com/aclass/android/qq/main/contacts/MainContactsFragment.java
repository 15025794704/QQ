package com.aclass.android.qq.main.contacts;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.aclass.android.qq.R;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.tools.MyDateBase;

import java.util.ArrayList;
import java.util.List;

/**
 * 应用“联系人”页面
 * 好友列表等
 */
public class MainContactsFragment extends Fragment {
//    private PinnedHeaderExpandableListView explistview;
    private int expandFlag = -1;//控制列表的展开
    private PinnedHeaderExpandableAdapter adapter;

    public static MainContactsFragment newInstance(){
        return new MainContactsFragment();
    }

    public MainContactsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initView();
        initData();
        return inflater.inflate(R.layout.fragment_main_contacts, container, false);
    }
    /**
     * 初始化VIEW
     */
    private void initView() {
//        explistview = (PinnedHeaderExpandableListView) explistview.findViewById(R.layout.fragment_main_contacts);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        List<GroupTitleInfo> groupList= new ArrayList<>();
        List<String> list=new ArrayList<>();
        List<Friend> specificFriends ;

        MyDateBase myDateBase=new MyDateBase();
        List<Friend> friends=myDateBase.getFriends("0987654321");
        for(int i=0;i<friends.size();i++) {

            String GroupName = friends.get(i).getQQgroup();
            list.add(GroupName);


        }
        System.out.println("list的长度为"+list.size());//2

        //去重并且按照自然顺序排列
        List<String> newList = removeDuplicate(list);//组名集合
        for(int i=0;i<newList.size();i++)
        {
            List<ContentInfo> listContentInfos=new ArrayList<>();
            if(!newList.isEmpty()) {

                specificFriends = myDateBase.getFriendsByqqGroup(newList.get(i));//获取特定组名下的好友列表
                for (int j = 0; j < specificFriends.size(); j++) {
                    if (!specificFriends.isEmpty()) {
                        //从数据库中获取好友信息
                        String beizhu = specificFriends.get(j).getBeiZhu();
                        int isHide = specificFriends.get(j).getIsHide();
                        User user = myDateBase.getUser(specificFriends.get(j).getQQ2());
                        String qianming = user.getQianMing();
                        Bitmap headImage = myDateBase.getImageByQQ(user.getQQNum());
                        //给ContentInfo赋值
                        ContentInfo contentInfo = new ContentInfo();
                        contentInfo.setBeiZhu(beizhu);
                        contentInfo.setQianming(qianming);
                        contentInfo.setIcon(headImage);
                        contentInfo.setIsHide(isHide);
                        listContentInfos.add(contentInfo);
                    }
                }
                //单个组名和其成员信息存入到groupTitleInfo中
                GroupTitleInfo groupTitleInfo = new GroupTitleInfo();
                groupTitleInfo.setQQGroupName(newList.get(i));
                groupTitleInfo.setInfo(listContentInfos);
                groupList.add(groupTitleInfo);
            }

        }
/*
        //设置悬浮头部VIEW
        explistview.setHeaderView(getLayoutInflater().inflate(R.layout.group_head,
                explistview, false));
                */
//        adapter = new PinnedHeaderExpandableAdapter(groupList,getContext(),explistview);
//        explistview.setAdapter(adapter);


    }

    class GroupClickListener implements ExpandableListView.OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
//            if (expandFlag == -1) {
//                // 展开被选的group
//                explistview.expandGroup(groupPosition);
//                // 设置被选中的group置于顶端
//                explistview.setSelectedGroup(groupPosition);
//                expandFlag = groupPosition;
//            } else if (expandFlag == groupPosition) {
//                explistview.collapseGroup(expandFlag);
//                expandFlag = -1;
//            } else {
//                explistview.collapseGroup(expandFlag);
//                // 展开被选的group
//                explistview.expandGroup(groupPosition);
//               /* // 设置被选中的group置于顶端
//                explistview.setSelectedGroup(groupPosition);*/
//                expandFlag = groupPosition;
//            }
            return true;
        }
    }
    /*
    * 去重
    * */
    public      List<String>  removeDuplicate(List<String> list)  {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).equals(list.get(i)))  {
                    list.remove(j);
                }
            }
        }
        return list;
    }


}
