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
        import android.text.InputType;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.TextureView;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;
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
        TextView  t =(TextView) findViewById(R.id.editText2);

        t.setSingleLine(false);
        t.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        t.setGravity(Gravity.TOP);
        //水平滚动设置为False
        t.setHorizontallyScrolling(false);
        t.setText("sssssbbsbssbbssssbsssssssbssssssbsss");
    }
}
