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
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aclass.android.qq.common.ActivityOpreation;
import com.aclass.android.qq.common.AssetsOperation;
import com.aclass.android.qq.common.MyBitMapOperation;
import com.aclass.android.qq.common.MyButtonOperation;
import com.aclass.android.qq.common.Screen;
import com.aclass.android.qq.custom.GeneralActivity;
import com.aclass.android.qq.custom.control.RoundImageView;
import com.aclass.android.qq.entity.Request;
import com.aclass.android.qq.entity.*;
import com.aclass.android.qq.internet.Attribute;
import com.aclass.android.qq.tools.MyDateBase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.List;

public class VideoWindowActivity extends GeneralActivity implements TextureView.SurfaceTextureListener{

    AssetsOperation assetsOperation;
    Screen screen;
    ImageButton btn_refuse;
    ImageButton btn_mic;
    ImageButton btn_loudspeaker;
    ImageButton btn_mini;

    RoundImageView headImg;

    private Camera mCamera;
    private TextureView textureView;
    private ImageView videoView;

    private Thread threadStartVideo;
    private DatagramSocket receiveSocket = null;
    private WifiManager.MulticastLock lock=null;
    private MyDateBase sendVideoDataBase;
    private String QQfriend;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (msg.what == 0x11) {
                byte[] b=Attribute.video_bitmap;
//                Toast.makeText(VideoWindowActivity.this,""+b.length,Toast.LENGTH_LONG).show();
                Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
                bmp= MyBitMapOperation.rotateBitmap(bmp,270);
                bmp=MyBitMapOperation.flipBitmap(bmp);
                videoView.setImageBitmap(bmp);
            }
            else if(msg.what==0x12){
                Toast.makeText(VideoWindowActivity.this,bundle.getString("msg"),Toast.LENGTH_LONG).show();
            }
            else if(msg.what == 0x13){
                finish();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_video);

        WifiManager manager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
        lock= manager.createMulticastLock("test wifi");

        init();
        initData();
       set_btn_mini_click();
       set_btn_refuse_click();
        startThreadStartVideo();
    }

    @Override
    protected void consumeInsets(Rect insets) {
      LinearLayout l=(LinearLayout) findViewById(R.id.LinearLayout_video_window);
        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0,insets.top,0,0);
        l.setLayoutParams(layoutParams);
    }


    private void startThreadStartVideo(){

        final Thread videoThread= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (; ; ) {
                        Request request= Attribute.friendVideoRequest;
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
                                Attribute.isInVideo=true;
                                for(;;) {
                                    if(!Attribute.isInVideo) {
                                        myDateBase.Destory();
                                        return;
                                    }
                                    myDateBase.UDPsend(port, new byte[2]);
                                    SystemClock.sleep(500);
                                }
                            }
                        }).start();

                        for (; ; ) {
                            Attribute.video_bitmap = myDateBase.receiveData();
                            ActivityOpreation.updateUI(handler, 0x11, "");
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
                    user.setQQNum("1505249457");
                    Request request = new Request(0,"",user);
                    buf=MyDateBase.toByteArray(request);
                    receiveSocket.send(new DatagramPacket(buf, buf.length, InetAddress.getByName("47.107.138.4"),890));
                    DatagramPacket dpReceive;

                    while (true) {
                        buf = new byte[1024*10];
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


    //最小化按钮执行方法
    protected void set_btn_mini_click(){
        btn_mini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        sendVideoRequest("1234567890");
                    }
                }).start();
            }
        });
    }

    private void sendVideoRequest(String QQFriend){
        try {
            com.aclass.android.qq.entity.Message msg = new com.aclass.android.qq.entity.Message();
            msg.setSendQQ(Attribute.QQ);
            msg.setReceiveNum(QQFriend);
            msg.setContext("request");
            Request request = new Request(8, "", msg);

            sendVideoDataBase = new MyDateBase();
            sendVideoDataBase.setTimeout(10000);
            sendVideoDataBase.UDPsend(request);

            request=(Request) sendVideoDataBase.receiveObject();
            if(request.getRequestType()==9){
                ActivityOpreation.updateUI(handler, 0x12, "好友已拒绝");
                sendVideoDataBase.Destory();
                return;
            }
            else if(request.getRequestType()==10){
                ActivityOpreation.updateUI(handler, 0x12, "好友不在线");
                sendVideoDataBase.Destory();
                return;
            }

            byte[] b= sendVideoDataBase.receiveData();
            final  int port=Integer.parseInt(new String(b));//服务器视频端口
            ActivityOpreation.updateUI(handler, 0x12, "好友接受，服务器视频端口"+port);

            sendVideoDataBase.UDPsend(port,"");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Attribute.isInVideo=true;
                    SystemClock.sleep(1000);
                    addCallBack(port);
                    for(;;) {
                        if(!Attribute.isInVideo) {
                            sendVideoDataBase.Destory();
                            return;
                        }
                        sendVideoDataBase.UDPsend(port, Attribute.video_bitmap_send);
                        SystemClock.sleep(100);
                    }

                }
            }).start();

            for (; ; ) {
                byte[] da = sendVideoDataBase.receiveData();
                ActivityOpreation.updateUI(handler, 0x12, da.length + ":leng");
            }
        }
        catch (Exception e){
            ActivityOpreation.updateUI(handler, 0x12, "已关闭");
            Attribute.isInVideo=false;
        }
    }

    //d挂断按钮执行方法
    protected void set_btn_refuse_click(){
        btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送断开请求
                Attribute.isInVideo=false;
                close();
            }
        });
    }

    private void close(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    threadStartVideo.stop();
                }
                catch (Exception e){
                }
                finally {
                    ActivityOpreation.updateUI(handler,0x13,"finish");
                }
            }
        }).start();
    }

    protected void init(){
        //设置状态栏背景
      //  ActivityOpreation.setStatusBar(this,R.color.colorVideoViewBG,View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        //初始化对象
        screen=new Screen(this);
        assetsOperation=AssetsOperation.getInstance(this);
        btn_refuse=(ImageButton)findViewById(R.id.image_btn_refuse);
        btn_mic=(ImageButton)findViewById(R.id.image_btn_mic);
        btn_loudspeaker=(ImageButton)findViewById(R.id.image_btn_loudspeaker);
        btn_mini=(ImageButton)findViewById(R.id.image_btn_mini);
        headImg=(RoundImageView)findViewById(R.id.RoundImageView_video_head);
        textureView=(TextureView) findViewById(R.id.texture_video_ImageView);
        videoView=(ImageView) findViewById(R.id.video_ImageView);
        //videoView.setRotation(270);


        //头像框设置大小,图片资源
        int width=(int)(screen.getposWidth()*0.38);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
        headImg.setLayoutParams(params);
        headImg.setImageBitmap(assetsOperation.getImage("image/qq.png"));
        //设置拒绝按钮按下和弹起效果
        MyButtonOperation.changeImageButton(this,btn_refuse,R.drawable.btn_refuse_2,R.drawable.btn_refuse_1);
        //设置小化按钮按下和弹起效果
        MyButtonOperation.changeImageButton(this,btn_mini,R.drawable.videoview_mini_btn2,R.drawable.videoview_mini_btn);
    }

    private void initData() {
        int numberOfCameras = Camera.getNumberOfCameras();// 获取摄像头个数
        if(numberOfCameras<1){
            Toast.makeText(this, "没有相机", Toast.LENGTH_SHORT).show();
//            finish();
            return;
        }
        textureView.setSurfaceTextureListener(this);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        // 打开相机 0后置 1前置
        mCamera = Camera.open(1);
        if (mCamera != null) {
            // 设置相机预览宽高，此处设置为TextureView宽高
            Camera.Parameters params = mCamera.getParameters();
            params.setPreviewSize(width, height);
            chooseFixedPreviewFps(params,20);
            // 设置自动对焦模式
            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                mCamera.setParameters(params);
            }
            try {
                mCamera.setDisplayOrientation(90);// 设置预览角度，并不改变获取到的原始数据方向
                // 绑定相机和预览的View
                mCamera.setPreviewTexture(surface);
                // 开始预览
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addCallBack(final int port) {
        if(mCamera!=null){
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    Camera.Size size = camera.getParameters().getPreviewSize();
                    try{
                        YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                        if(image!=null){
                            if(!Attribute.isInVideo) {
                                sendVideoDataBase.Destory();
                                return;
                            }
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            image.compressToJpeg(new Rect(0, 0, size.width,size.height), 20, stream);
                            byte[] b=stream.toByteArray();
                            Attribute.video_bitmap_send=b;
                            stream.close();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 选择合适的FPS
     * @param parameters
     * @param expectedThoudandFps 期望的FPS
     * @return
     */
    public static int chooseFixedPreviewFps(Camera.Parameters parameters, int expectedThoudandFps) {
        List<int[]> supportedFps = parameters.getSupportedPreviewFpsRange();
        for (int[] entry : supportedFps) {
            if (entry[0] == entry[1] && entry[0] == expectedThoudandFps) {
                parameters.setPreviewFpsRange(entry[0], entry[1]);
                return entry[0];
            }
        }
        int[] temp = new int[2];
        int guess;
        parameters.getPreviewFpsRange(temp);
        if (temp[0] == temp[1]) {
            guess = temp[0];
        } else {
            guess = temp[1] / 2;
        }
        return guess;
    }
    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {}
    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mCamera.stopPreview();
        mCamera.release();
        return false;
    }
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {}

}
