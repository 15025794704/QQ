package com.aclass.android.qq.main.contacts;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aclass.android.qq.MessageWindowActivity;
import com.aclass.android.qq.R;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.main.MainFragment;
import com.aclass.android.qq.tools.MyDateBase;

import java.util.ArrayList;
import java.util.List;


/**
 * 应用“联系人”页面
 * 好友列表等
 */
public class MainContactsFragment extends Fragment implements MainFragment.MainPage {
 private PinnedHeaderExpandableListView explistView;
    private PinnedHeaderExpandableAdapter adapter;
    private int expandFlag=-1;//控制列表的展开
    public static MainContactsFragment newInstance(){
        return new MainContactsFragment();
    }

    public MainContactsFragment() {
    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            Bundle bundle = msg.getData();
            if (msg.what == 0x11) {

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view =inflater.inflate(R.layout.fragment_main_contacts, container, false);
      explistView=view.findViewById(R.id.explistview);
        initData();
        return view;
    }

    @Override
    public void onPageVisible(MyToolbar toolbar, TextView title) {
        title.setText(R.string.mainBottomNavContacts);
    }

    @Override
    public void onVisiblyClick() {

    }

    @Override
    public boolean onVisiblyDoubleClick() {
        return false;
    }

    /*
    * 初始化适配器
    * */
  /*  private void initAdapter(){
        adapter=new PinnedHeaderExpandableAdapter(groupList,getContext());
        mElistview.setAdapter(adapter);
    }*/

    /**
     * 初始化数据
     */
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<GroupTitleInfo> groupList = new ArrayList<>();
                    List<String> list = new ArrayList<>();
                    List<Friend> specificFriends;

                    MyDateBase myDateBase = new MyDateBase();
                    List<Friend> friends = myDateBase.getFriends(Attribute.QQ);
                    if (friends != null)
                        for (int i = 0; i < friends.size(); i++) {

                            String GroupName = friends.get(i).getQQgroup();
                            list.add(GroupName);

                        }
                    System.out.println("list的长度为" + list.size());//2

                    //去重并且按照自然顺序排列
                    List<String> newList = removeDuplicate(list);//组名集合
                    for (int i = 0; i < newList.size(); i++) {
                        List<ContentInfo> listContentInfos = new ArrayList<>();
                        if (!newList.isEmpty()) {

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
                        final List<GroupTitleInfo> groupListTemp = groupList;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    adapter = new PinnedHeaderExpandableAdapter(groupListTemp, getActivity());
                                    explistView.setAdapter(adapter);
                                }
                                catch (Exception e){}
                            }
                        });
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

    class GroupClickListener implements ExpandableListView.OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
            if (expandFlag == -1) {
                // 展开被选的group
                explistView.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistView.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            } else if (expandFlag == groupPosition) {
                explistView.collapseGroup(expandFlag);
                expandFlag = -1;
            } else {
                explistView.collapseGroup(expandFlag);
                // 展开被选的group
                explistView.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistView.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
            }
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
