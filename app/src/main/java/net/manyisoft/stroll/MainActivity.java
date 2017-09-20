package net.manyisoft.stroll;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import net.manyisoft.library.stroll.Stroll;
import net.manyisoft.library.stroll.core.StrollConfig;
import net.manyisoft.library.stroll.img.ImageListener;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    TextView iv ,iv1, iv2,iv3,iv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stroll.Companion.install(new StrollConfig());
//        Stroll.Companion.get().setBaseUrl("https://www.qigeairen.com")
//                .setCallBack(new StringCallBack(){
//                    @Override
//                    public void asyncSuccess(@NonNull byte[] byteArray) {}
//                    @Override
//                    public void complate() {
//                        Log.w("TAG","加载完成");
//                    }
//                    @Override
//                    public void progress(int pro) {
//                        Log.w("TAG",""+pro);
//                    }
//                    @Override
//                    public void start() {
//                        Log.w("TAG","开始");
//                    }
//                    @Override
//                    public void error(String msg) {
//                        Log.w("TAG",msg);
//                    }
//                    @Override
//                    public void success(String text) {
//                        Log.w("TAG",text);
//                    }
//                }).build();
        String path = "sdcard/Stroll";
        File file = new File(path);
        if (!file.exists()) file.mkdirs();



        iv = (TextView) findViewById(R.id.iv);
        iv1 = (TextView) findViewById(R.id.iv1);
        iv2 = (TextView) findViewById(R.id.iv2);
        iv3 = (TextView) findViewById(R.id.iv3);
        iv4 = (TextView) findViewById(R.id.iv4);

        Test.INSTANCE.testHttp(iv,iv1,iv2,iv3,iv4,this,file.getAbsolutePath() ,".apk");

        String a = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1536086522,2785217828&fm=26&gp=0.jpg";
//
//            //加载图片带回调
            Stroll.Companion.loadImageWithUrl(iv, a, true, -1, new ImageListener() {
                @Override
                public void progress(int progress) {
                    iv.setText(progress+"");
                    Log.w("TAG",":::"+ progress);
                }

                @Override
                public void complate() {
                    iv.setText("加载完成:     ");
                }

                @Override
                public void error() {
                    iv.setText("加载失败");
                }
            });
//
//        //加载图片不带回调
        Stroll.Companion.loadImageWithUrl(iv, a, true, R.drawable.stroll_errorimg,null);




    }

}
