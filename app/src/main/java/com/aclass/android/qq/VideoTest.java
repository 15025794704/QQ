package com.aclass.android.qq;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.ImageFormat;
        import android.graphics.Rect;
        import android.graphics.SurfaceTexture;
        import android.graphics.YuvImage;
        import android.hardware.Camera;
        import android.net.wifi.WifiManager;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.os.SystemClock;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.TextureView;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.Toast;

        import com.aclass.android.qq.common.ActivityOpreation;
        import com.aclass.android.qq.common.MyBitMapOperation;
        import com.aclass.android.qq.entity.Request;
        import com.aclass.android.qq.entity.User;
        import com.aclass.android.qq.internet.Attribute;
        import com.aclass.android.qq.tools.MyDateBase;

        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.net.DatagramPacket;
        import java.net.DatagramSocket;
        import java.net.InetAddress;
        import java.net.SocketAddress;
        import java.net.SocketTimeoutException;
        import java.util.ArrayList;
        import java.util.List;

public class VideoTest extends AppCompatActivity {

    private Button button;
    private ImageView videoView;
    private DatagramSocket receiveSocket = null;
    private static int receive_port = 9999;
    private WifiManager.MulticastLock lock=null;
    private SocketAddress friendAddrss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_test);
        button=(Button)findViewById(R.id.button) ;
        videoView=(ImageView) findViewById(R.id.video_test_imageView);

        WifiManager manager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        lock= manager.createMulticastLock("test wifi");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {//发送服务器登录信息
                    @Override
                    public void run() {
                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    sendVideoRequest("1234567890");
                                }
                            }).start();
                        }
                        catch (Exception e){}
                    }
                }).start();
            }
        });
        startThreadStartVideo();
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (msg.what == 0x11) {
                byte[] b= bundle.getByteArray("msg");
                Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                bmp= MyBitMapOperation.rotateBitmap(bmp,270);
                bmp=MyBitMapOperation.flipBitmap(bmp);
                videoView.setImageBitmap(bmp);
            }
            else if(msg.what==0x12){
                Toast.makeText(VideoTest.this,bundle.getString("msg"),Toast.LENGTH_LONG).show();
            }
            else if(msg.what == 0x13){
                finish();
            }
        }
    };

    private void sendVideoRequest(String QQFriend){
        try {
            com.aclass.android.qq.entity.Message msg = new com.aclass.android.qq.entity.Message();
            msg.setSendQQ(Attribute.QQ);
            msg.setReceiveNum(QQFriend);
            msg.setContext("request");
            Request request = new Request(8, "", msg);

            final MyDateBase myDateBase = new MyDateBase();
            myDateBase.setTimeout(10000);
            myDateBase.UDPsend(request);

            request=(Request) myDateBase.receiveObject();
            if(request.getRequestType()==9){
                ActivityOpreation.updateUI(handler, 0x12, "好友已拒绝");
                myDateBase.Destory();
                return;
            }
            else if(request.getRequestType()==10){
                ActivityOpreation.updateUI(handler, 0x12, "好友不在线");
                myDateBase.Destory();
                return;
            }

            byte[] b= myDateBase.receiveData();
            final  int port=Integer.parseInt(new String(b));//服务器视频端口
            ActivityOpreation.updateUI(handler, 0x12, "好友接受，服务器视频端口"+port);

            myDateBase.UDPsend(port,"");


            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(1000);
                    for(;;) {
                        if(!Attribute.isInVideo) {
                            myDateBase.Destory();
                            return;
                        }
                        myDateBase.UDPsend(port, "123");
                        SystemClock.sleep(500);
                    }
                }
            }).start();

            for (; ; ) {
                byte[] da = myDateBase.receiveData();
                ActivityOpreation.updateUI(handler, 0x12, da.length + ":leng");
            }
        }
        catch (Exception e){
            ActivityOpreation.updateUI(handler, 0x12, "已关闭");
            Attribute.isInVideo=false;
        }
    }

    private void startThreadStartVideo(){

        final Thread videoThread= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (; ; ) {
                        Request request=Attribute.friendVideoRequest;
                        com.aclass.android.qq.entity.Message msg=(com.aclass.android.qq.entity.Message)request.getObj();
                        String sendQQ=msg.getSendQQ();

                        final MyDateBase myDateBase=new MyDateBase();
                        myDateBase.setTimeout(10000);
                        msg.setContext("2>1");//接受
                        request.setObj(msg);
                        myDateBase.UDPsend(request);    //发送拒绝或接受

                        byte[] b= myDateBase.receiveData();
                        final int port=Integer.parseInt(new String(b));//服务器视频端口
                        ActivityOpreation.updateUI(handler, 0x12, "服务器视频端口"+port);

                        SystemClock.sleep(300);
                        myDateBase.UDPsend(port,"");


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                SystemClock.sleep(300);
                                for(;;) {
                                    if(!Attribute.isInVideo) {
                                        myDateBase.Destory();
                                        return;
                                    }
                                    myDateBase.UDPsend(port, "1235677");
                                    SystemClock.sleep(500);
                                }
                            }
                        }).start();

                        for (; ; ) {
                            byte[] da = myDateBase.receiveData();
                            ActivityOpreation.updateUI(handler, 0x12, da.length + ":leng");
                        }
                    }
                }
                catch (Exception e){
                    ActivityOpreation.updateUI(handler, 0x12,"已关闭");
                    Attribute.isInVideo=false;
                }
            }
        });


        new Thread(new Runnable() {//发送服务器登录信息
            @Override
            public void run() {
                try {
                    receiveSocket = new DatagramSocket();
                    byte[] buf;
                    User user = new User();
                    user.setQQNum("1234567890");
                    Request request = new Request(0,"",user);
                    buf=MyDateBase.toByteArray(request);
                    receiveSocket.send(new DatagramPacket(buf, buf.length,InetAddress.getByName("47.107.138.4"),890));
                    DatagramPacket dpReceive;

                    while (true) {
                        buf = new byte[1024 * 100];
                        dpReceive = new DatagramPacket(buf, buf.length);
                        lock.acquire();
                        receiveSocket.receive(dpReceive);
                        request = (Request) MyDateBase.toObject(buf, dpReceive.getLength());
                        if (request.getRequestType() == 8) {
                            ActivityOpreation.updateUI(handler, 0x12, "接到视频，开启线程");
                            Attribute.friendVideoRequest=request;
                            videoThread.start();
                        }
                        lock.release();
                    }
                }
                catch (Exception e){
                    ActivityOpreation.updateUI(handler, 0x12, "开启端口失败");
                }
            }
        }).start();
    }
}
