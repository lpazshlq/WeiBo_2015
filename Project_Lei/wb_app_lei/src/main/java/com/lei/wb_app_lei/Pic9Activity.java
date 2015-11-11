package com.lei.wb_app_lei;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lei.wb_adapter.ViewPagerAdapter;
import com.lei.wb_utils.ImageLoader_Init_Util;
import com.lei.wb_utils.Pic_Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polites.android.GestureImageView;

import java.util.ArrayList;

public class Pic9Activity extends Activity {
    int position;//浏览图片列表图片序号
    ViewPager viewPager;
    ArrayList<String> pics;
    ArrayList<GestureImageView> list_ImageView;
    TextView tv_pic_num;
    ImageLoader imageLoader;
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        init();
        listener();
        viewPager.setCurrentItem(position);
    }

    private void init() {
        tv_pic_num = (TextView) findViewById(R.id.tv_pic_num_activity_pic);
        options = ImageLoader_Init_Util.initDisplayImageOption(getApplicationContext());
        pics = getIntent().getStringArrayListExtra("pics");
        position = getIntent().getIntExtra("position", -1);
        Pic_Util pic__util = new Pic_Util();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoader_Init_Util.initConfiguration(getApplicationContext()));
        list_ImageView = new ArrayList<>();
        for (String pic : pics) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            GestureImageView imageView = new GestureImageView(getApplicationContext());
            imageLoader.displayImage(pic__util.getOriginalPic(pic), imageView);
            imageView.setLayoutParams(params);
            list_ImageView.add(imageView);
        }
        viewPager = (ViewPager) findViewById(R.id.vpager_pic_activity_pic);
        viewPager.setAdapter(new ViewPagerAdapter(list_ImageView));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_pic_num.setText((position + 1) + "/" + pics.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (pics.size() < 2) {
            tv_pic_num.setVisibility(View.INVISIBLE);
        }
        tv_pic_num.setText((position + 1) + "/" + pics.size());
    }

    private void listener() {

    }
}
