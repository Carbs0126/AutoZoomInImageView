package cn.carbs.autozoominimageview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import cn.carbs.android.library.AutoZoomInImageView;

public class MainActivity extends Activity {

    AutoZoomInImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        iv = (AutoZoomInImageView) findViewById(R.id.iv);
        iv.post(new Runnable() {

            @Override
            public void run() {
                //简单方式启动放大动画
//                iv.init()
//                  .startZoomInByScaleDeltaAndDuration(0.3f, 1000, 1000);//放大增量是0.3，放大时间是1000毫秒，放大开始时间是1000毫秒以后
                //使用较为具体的方式启动放大动画
                iv.init()
                        .setScaleDelta(0.3f)//放大的系数是原来的（1 + 0.3）倍
                        .setDurationMillis(1000)//动画的执行时间为1000毫秒
                        .setOnZoomListener(new AutoZoomInImageView.OnZoomListener(){
                            @Override
                            public void onStart(View view) {
                                //放大动画开始时的回调
                            }
                            @Override
                            public void onUpdate(View view, float progress) {
                                //放大动画进行过程中的回调 progress取值范围是[0,1]
                            }
                            @Override
                            public void onEnd(View view) {
                                //放大动画结束时的回调
                            }
                        })
                        .start(1000);//延迟1000毫秒启动
            }
        });
    }

}