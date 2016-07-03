# AutoZoomInImageView
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AutoZoomInImageView-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3819)

#前言：
很多app的splash动画是这样的：一张静态图片居中显示并充满屏幕，同时不断放大图片中间部分，形成一种图片不断向中间放大的动画效果。AutoZoomInImageView继承自ImageView并实现了这种动画效果。

##截图：
<center>
![you can check the preview.gif](https://github.com/Carbs0126/Screenshot/blob/master/AutoZoomInImageView.gif)
</center>

##功能：
1. 图像居中显示（图像覆盖整个AutoZoomInImageView），并可以使其进行中心放大显示的动画。
2. 可以设置动画放大的时长；
3. 可以设置动画放大的比例，设置其动画效果达到的最终尺寸是其初始化尺寸的增大倍数；

##原理：
1. 调整图片，使图片位于屏幕的正中间。由于android手机屏幕尺寸多种多样，而图片的大小也不甚相同，为了灵活的使用此效果，需要将任意尺寸比例的图片显示在任意尺寸比例的手机屏幕的正中间，同时不使图片扭曲变形。
2. 更改float[]的值，然后更新Matrix并应用到ImageView中，从而达到图片zoomin的效果

##添加至工程
```
compile 'cn.carbs.android:AutoZoomInImageView:1.0.0'
```

##使用方法：
1.layout文件中添加此view
```
<cn.carbs.android.library.AutoZoomInImageView
        android:id="@+id/auto_zoomin_image_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:src="@drawable/your_pic" />
```

2.在onCreate(Bundle bundle)函数中，添加如下代码：
```
auto_zoomin_image_view.post(new Runnable() {

            @Override
            public void run() {
                //简单方式启动放大动画
                //放大增量是0.3，放大时间是1000毫秒，放大开始时间是1000毫秒以后
//                auto_zoomin_image_view.init()
//                  .startZoomInByScaleDeltaAndDuration(0.3f, 1000, 1000);

                //使用较为具体的方式启动放大动画
                auto_zoomin_image_view.init()
                        .setScaleDelta(0.2f)//放大的系数是原来的（1 + 0.2）倍
                        .setDurationMillis(1500)//动画的执行时间为1500毫秒
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

```


## License

    Copyright 2016 Carbs.Wang (AutoZoomInImageView)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
