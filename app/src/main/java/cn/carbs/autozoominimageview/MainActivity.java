package cn.carbs.autozoominimageview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

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
                iv.init();
                iv.startZoomInBySpanDelayed(1.0f, 1000, 1000);
            }

        });
    }

}