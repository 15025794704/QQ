package com.aclass.android.qq.main.contacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.aclass.android.qq.NewFriendsActivity;
import com.aclass.android.qq.R;
import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.custom.control.MyToolbar;
import com.aclass.android.qq.entity.Friend;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.internet.Receiver;
import com.aclass.android.qq.main.MainActivity;
import com.aclass.android.qq.main.MainFragment;
import com.aclass.android.qq.seek.SeekActivity;
import com.aclass.android.qq.tools.MyDateBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 应用“联系人”页面
 * 好友列表等
 */
public class MainContactsFragment extends Fragment implements MainFragment.MainPage, Toolbar.OnMenuItemClickListener {

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
        Attribute.agreeFriendClick=1;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Attribute.agreeFriendClick!=0)
        {
            initData();
        }
    }

    @Override
    public void onPageVisible(MyToolbar toolbar, TextView title) {
        title.setText(R.string.mainBottomNavContacts);
        toolbar.inflateMenu(R.menu.toolbar_main_contacts);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public void onVisiblyClick() {
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
        if(Attribute.msgList==null)
            Attribute.msgList=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyDateBase myDateBase=null;
                try {
                    if(Attribute.userHeadList==null)
                        Attribute.userHeadList=new HashMap<String, Bitmap>();
                    if(Attribute.userInfoList==null)
                        Attribute.userInfoList=new HashMap<String, User>();
                    if(Attribute.friendList==null)
                        Attribute.friendList=new HashMap<String, Friend>();

                    SystemClock.sleep(200);
                    List<GroupTitleInfo> groupList = new ArrayList<>();
                    List<String> list = new ArrayList<>();
                    List<Friend> specificFriends;

                    myDateBase = new MyDateBase();
                    List<Friend> friends = myDateBase.getFriends(Attribute.QQ);
                    if (friends != null)
                        Attribute.friendList.clear();
                        for (int i = 0; i < friends.size(); i++) {
                            Attribute.friendList.put(friends.get(i).getQQ2(),friends.get(i));
                            String GroupName = friends.get(i).getQQgroup();
                            if(!GroupName.equals(""))
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
                                    else{
                                        Attribute.userInfoList.remove(user.getQQNum());
                                        Attribute.userInfoList.put(user.getQQNum(),user);
                                    }

                                    //备注
                                    if(beizhu==null || beizhu.equals(""))
                                        beizhu=user.getNiCheng();

                                    String qianming = user.getQianMing();

                                    Bitmap headImage = Receiver.getImageBitmap(Attribute.urlHead+user.getQQNum()+".png");
                                    //添加 好友头像 到公共集合
                                    if (headImage != null) {
                                        if(!Attribute.userHeadList.containsKey(user.getQQNum())){
                                            Attribute.userHeadList.put(user.getQQNum(),headImage);
                                        }
                                        else{
                                            if( headImage.getByteCount()>Attribute.userHeadList.get(user.getQQNum()).getByteCount()){
                                                Attribute.userHeadList.remove(user.getQQNum());
                                                Attribute.userHeadList.put(user.getQQNum(),headImage);
                                            }
                                        }
                                    }


                                    //给ContentInfo赋值
                                    ContentInfo contentInfo = new ContentInfo();
                                    contentInfo.setQQ(user.getQQNum());
                                    contentInfo.setBeiZhu(beizhu);
                                    contentInfo.setQianming(qianming);
                                    contentInfo.setIcon(headImage);
                                    contentInfo.setIsHide(isHide);

                                    listContentInfos.add(contentInfo);
                                    SystemClock.sleep(30);
                                }
                            }
                            //单个组名和其成员信息存入到groupTitleInfo中
                            GroupTitleInfo groupTitleInfo = new GroupTitleInfo();
                            groupTitleInfo.setQQGroupName(newList.get(i));
                            groupTitleInfo.setInfo(listContentInfos);
                            groupList.add(groupTitleInfo);
                        }
                    }
                    final List<GroupTitleInfo> groupListTemp = groupList;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                adapter = new PinnedHeaderExpandableAdapter(groupListTemp, getActivity());
                                explistView.setAdapter(adapter);
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });
                    Attribute.agreeFriendClick=0;
                }
                catch (Exception e){
                    if(Attribute.agreeFriendClick!=0){
                        Attribute.agreeFriendClick=0;
                        initData();
                    }
                    e.printStackTrace();
                }
                finally {
                    myDateBase.Destory();
                    Log.d("system","朋友："+Attribute.friendList.size()+",用户："+Attribute.userInfoList.size()+",头像:"+Attribute.userHeadList.size());
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


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainToolbarContactsSeek:
                startActivity(new Intent(getContext(), SeekActivity.class));
                return true;
        }
        return false;
    }
}
