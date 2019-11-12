package com.aclass.android.qq;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TestMainActivity extends ListActivity {
     SharedPreferences sharedPreferences;
    String account;
    boolean state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.activity_test_main);*/
        sharedPreferences=this.getSharedPreferences("loginInfo",MODE_PRIVATE);
        account=sharedPreferences.getString("loginUserName", null);
        state=sharedPreferences.getBoolean("isLogin",false);

        //设置适配器
        /*SimpleAdapter的参数说明
         * 第一个参数 表示访问整个android应用程序接口，基本上所有的组件都需要
         * 第二个参数表示生成一个Map(String ,Object)列表选项
         * 第三个参数表示界面布局的id  表示该文件作为列表项的组件
         * 第四个参数表示该Map对象的哪些key对应value来生成列表项
         * 第五个参数表示来填充的组件 Map对象key对应的资源一依次填充组件 顺序有对应关系
         * 注意的是map对象可以key可以找不到 但组件的必须要有资源填充  因为 找不到key也会返回null 其实就相当于给了一个null资源
         * */


        SimpleAdapter adapter = new SimpleAdapter(this, getData(account),R.layout.activity_test_main,new String[]{"name"},new int[]{R.id.accountID});
      setListAdapter(adapter);

    }
    //得到数据
    private List<Map<String, Object>> getData(String account){
        List<Map<String,Object>> datas = new ArrayList<Map<String, Object>>();
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", (Object) account);
       /* data.put("state",state);*/
        datas.add(data);
        return datas;
    }



}
