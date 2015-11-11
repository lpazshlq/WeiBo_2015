package com.lei.wb_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.lei.wb_adapter.Pic9Adapter;
import com.lei.wb_app_lei.R;
import com.lei.wb_utils.Data_Util;
import com.lei.wb_utils.ImageLoader_Init_Util;
import com.lei.wb_model.WB_Text;
import com.lei.wb_utils.Pic_Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sina.weibo.sdk.openapi.models.Status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
/** 微博正文fragment */
public class Text_Fragment extends Fragment {
    private Status status;
    private ImageLoader imageLoader;
    private WB_Text wb_text;
    private LayoutInflater inflater;
    private DisplayImageOptions options;
    private ImageLoaderConfiguration configuration;
    private boolean isToolbarVisiable = true;

    public void setStatus(Status status, boolean isToolbarVisiable) {
        this.status = status;
        this.isToolbarVisiable = isToolbarVisiable;
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_text, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wb_text = new WB_Text(view);
        inflater = LayoutInflater.from(getActivity().getApplicationContext());
        options = ImageLoader_Init_Util.initDisplayImageOption(getActivity().getApplicationContext());
        configuration = ImageLoader_Init_Util.initConfiguration(getActivity().getApplicationContext());
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(configuration);
        if (getArguments() != null) {
            status = (Status) getArguments().getSerializable("status");
            if (status != null) {
                init();
            }
        }
    }

    private void init() {

        imageLoader.displayImage(status.user.profile_image_url, wb_text.user_image);
        wb_text.tv_screen_name.setText(status.user.screen_name + "");

        wb_text.tv_wb_create_time.setText(Data_Util.getData(status.created_at) + "");
        wb_text.tv_text.setText(status.text + "");

        if (isToolbarVisiable) {
            if (status.reposts_count > 0) {
                wb_text.rbtn_retweet.setText("   " + status.reposts_count);
            } else {
                wb_text.rbtn_retweet.setText("   " + "转发数");
            }
            if (status.comments_count > 0) {
                wb_text.rbtn_comment.setText("   " + status.comments_count);
            } else {
                wb_text.rbtn_comment.setText("   " + "评论数");
            }

            if (status.attitudes_count > 0) {
                wb_text.rbtn_like.setText("   " + status.attitudes_count);
            } else {
                wb_text.rbtn_like.setText("   " + "赞");
            }
        } else {
            wb_text.ll_toolbar.setVisibility(View.GONE);
        }

        if (status.retweeted_status != null) {
            if (status.retweeted_status.pic_urls != null) {
                Pic9Adapter retweeted_pic9Adapter = new MyPic9Adapter(status.retweeted_status.pic_urls, inflater, imageLoader, options);
                wb_text.lv_retweeted_pics.setAdapter(retweeted_pic9Adapter);
                Pic_Util.setListViewHeightBasedOnChildren((GridView) wb_text.lv_retweeted_pics);
            }
            wb_text.tv_retweeted_text.setText("@" + status.retweeted_status.user.screen_name + "：" + status.retweeted_status.text);
        } else {
            wb_text.ll_retweeted.setVisibility(View.GONE);
        }

        if (status.pic_urls != null) {
            Pic9Adapter pic9Adapter = new MyPic9Adapter(status.pic_urls, inflater, imageLoader, options);
            wb_text.lv_pics.setAdapter(pic9Adapter);
            //动态增加lv_pics高度
            Pic_Util.setListViewHeightBasedOnChildren((GridView) wb_text.lv_pics);
        }
    }

    class MyPic9Adapter extends Pic9Adapter {

        public MyPic9Adapter(ArrayList<String> pics, LayoutInflater inflater, ImageLoader imageLoader, DisplayImageOptions options) {
            super(pics, inflater, imageLoader, options);
        }

        @Override
        public void pic_9_Listener(ArrayList<String> pics, int position) {
            Intent intent = new Intent("com.lei.action.pic_9_activity");
            intent.putExtra("pics", pics);
            intent.putExtra("position", position);
            startActivity(intent);
        }
    }
}
