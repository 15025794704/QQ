package com.aclass.android.qq.main.contacts;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aclass.android.qq.MessageWindowActivity;
import com.aclass.android.qq.NewFriendsActivity;
import com.aclass.android.qq.R;
import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.main.MainActivity;
import com.aclass.android.qq.main.MainFragment;
import com.aclass.android.qq.tools.MyDateBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 应用“联系人”页面
 * 好友列表等
 */
public class MainContactsFragment extends Fragment implements MainFragment.MainPage {

 private PinnedHeaderExpandableListView explistView;
    private PinnedHeaderExpandableAdapter adapter;
    private int expandFlag=-1;//控制列表的展开
    private MainActivity mActivity;
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
        mActivity=(MainActivity) getActivity();
        view.findViewById(R.id.into_newFriend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转好友申请页面
                ActivityOpreation.jumpActivity(mActivity, NewFriendsActivity.class);
            }
        });
        initData();
        return view;
    }

    @Override
    public void onPageVisible(MyToolbar toolbar, TextView title) {
        title.setText(R.string.mainBottomNavContacts);
        if(Attribute.agreeFriendClick==1)
        {
            Attribute.agreeFriendClick=0;
            initData();
        }
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
                    if(Attribute.userHeadList==null)
                        Attribute.userHeadList=new HashMap<String, Bitmap>();
                    if(Attribute.userInfoList==null)
                        Attribute.userInfoList=new HashMap<String, User>();
                    if(Attribute.friendList==null)
                        Attribute.friendList=new HashMap<String, Friend>();

                    List<GroupTitleInfo> groupList = new ArrayList<>();
                    List<String> list = new ArrayList<>();
                    List<Friend> specificFriends;

                    MyDateBase myDateBase = new MyDateBase();
                    List<Friend> friends = myDateBase.getFriends(Attribute.QQ);
                    if (friends != null)
                        Attribute.friendList.clear();
                        for (int i = 0; i < friends.size(); i++) {
                            Attribute.friendList.put(friends.get(i).getQQ2(),friends.get(i));
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
                                if (!specificFriends.isEmpty() && specificFriends.get(j).getIsAgree()!=0) {
                                    //从数据库中获取好友信息
                                    String beizhu = specificFriends.get(j).getBeiZhu();
                                    int isHide = specificFriends.get(j).getIsHide();
                                    User user = myDateBase.getUser(specificFriends.get(j).getQQ2());
                                    //添加 好友信息 到公共集合
                                    if(!Attribute.userInfoList.containsKey(user.getQQNum())){
                                        Attribute.userInfoList.put(user.getQQNum(),user);
                                    }

                                    String qianming = user.getQianMing();
                                    Bitmap headImage = myDateBase.getImageByQQ(user.getQQNum());
                                    //添加 好友头像 到公共集合
                                    if(!Attribute.userHeadList.containsKey(user.getQQNum())){
                                        Attribute.userHeadList.put(user.getQQNum(),headImage);
                                    }


                                    //给ContentInfo赋值
                                    ContentInfo contentInfo = new ContentInfo();
                                    contentInfo.setQQ(user.getQQNum());
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
                finally {
                    Log.d("TAG","朋友："+Attribute.friendList.size()+",用户："+Attribute.userInfoList.size()+",头像:"+Attribute.userHeadList.size());
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
