package com.aclass.android.qq.internet;

import android.os.SystemClock;

import com.aclass.android.qq.entity.Request;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.tools.MyDateBase;

/**
 * Created by Administrator on 2019/11/6.
 */

public class Receiver {
    public static Thread ReceiverThread=null;

    public static void startReceiver(){
        if(ReceiverThread!=null)
            return;
        ReceiverThread=new Thread(new Runnable() {//发送服务器登录信息
            @Override
            public void run() {
                SystemClock.sleep(200);
                MyDateBase dateBase=new MyDateBase();
                User user = new User();
                user.setQQNum("1505249457");
                dateBase.UDPsend(new Request(0, "", user));
                for(;;){
                    Request request=(Request) dateBase.receiveObject();
                    if(request.getRequestType()==8){
//                        com.aclass.android.qq.entity.Message msg=(com.aclass.android.qq.entity.Message)request.getObj();
//                        QQfriend=msg.getSendQQ();
//                        friendAddress = dateBase.getSendAddress();
//                        video.start();
                    }
                }
            }
        });
        ReceiverThread.start();
    }
}
