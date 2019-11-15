package com.aclass.android.qq.internet;

import android.app.Activity;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.ScrollView;

import com.aclass.android.qq.MessageWindowActivity;
import com.aclass.android.qq.VideoWindowActivity;
import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.entity.Message;
import com.aclass.android.qq.entity.Request;
import com.aclass.android.qq.entity.User;
import com.aclass.android.qq.tools.MyDateBase;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by Administrator on 2019/11/6.
 */

/**
 * 由主进程调用
 * 消息接收器
 */
public class Receiver {
    public static Thread ReceiverThread=null;
    /*
    public static void startReceiver(final Activity activity，Handler handler){
        Attribute.restartMessageReceive=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        SystemClock.sleep(3*60*1000);
                        Attribute.mainMessageReceive.stop();
                    }
                    catch (Exception e){}
                    finally {
                        try {
                            Attribute.mainMessageReceive.start();
                        }
                        catch (Exception e){
                        }
                    }
                }
            }
        });

        Attribute.mainMessageReceive=new Thread(new Runnable() {//发送服务器登录信息
            @Override
            public void run() {
                DatagramSocket receiveSocket;
                try {
                    receiveSocket = new DatagramSocket();
                    byte[] buf;
                    User user = new User();
                    user.setQQNum(Attribute.QQ);
                    Request request = new Request(0,"",user);
                    buf=MyDateBase.toByteArray(request);
                    receiveSocket.send(new DatagramPacket(buf, buf.length, InetAddress.getByName("47.107.138.4"),890));
                    DatagramPacket dpReceive;

                    while (true) {
                        buf = new byte[1024*60];
                        dpReceive = new DatagramPacket(buf, buf.length);
//                        lock.acquire();
                        receiveSocket.receive(dpReceive);
                        request = (Request) MyDateBase.toObject(buf, dpReceive.getLength());
                        if (request.getRequestType() == 8) {
                            if(!Attribute.isInVideo) {
                                String send = ((Message) request.getObj()).getSendQQ();
                                Attribute.friendVideoRequest = request;
                                ActivityOpreation.jumpActivity(activity, VideoWindowActivity.class, new String[]{"receive", send});
                            }
                        }
                        else if(request.getRequestType()==5){
                            Attribute.friendMessageRequest=request;
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Message msg=(Message) Attribute.friendMessageRequest.getObj();
                                    addMsg(true,msg.getContext());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            scrollListView.fullScroll(ScrollView.FOCUS_DOWN);
                                        }
                                    });
                                }
                            });
                        }
//                        lock.release();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    ActivityOpreation.updateUI(handler, 0x12, "开启端口失败");
                }
            }
        });

        Attribute.restartMessageReceive.start();
        Attribute.mainMessageReceive.start();
    }
    */
}
