package com.lei.wb_model;

import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lei.wb_app_lei.R;

/**
 * 微博正文布局信息
 */
public class WB_Text {
    /**
     * 微博发布者用户头像
     */
    public ImageView user_image;
    /**
     * 微博发布者用户昵称
     */
    public TextView tv_screen_name;
    /**
     * 微博发布所在城市
     */
    public TextView tv_city;
    /**
     * 微博发布时间
     */
    public TextView tv_wb_create_time;
    /**
     * 微博发布来源
     */
    public TextView tv_wb_create_from;
    /**
     * 微博正文
     */
    public TextView tv_text;
    /**
     * 微博图片
     */
    public AbsListView lv_pics;
    /**
     * 被转发原文线性布局
     */
    public LinearLayout ll_retweeted;
    /**
     * 被转发原文微博正文
     */
    public TextView tv_retweeted_text;
    /**
     * 被转发原文微博图片
     */
    public AbsListView lv_retweeted_pics;
    /**
     * radio控件线性布局
     */
    public LinearLayout ll_toolbar;
    /**
     * 微博转发控件
     */
    public RadioButton rbtn_retweet;
    /**
     * 微博评论控件
     */
    public RadioButton rbtn_comment;
    /**
     * 微博点赞控件
     */
    public RadioButton rbtn_like;

    /**
     * 父布局
     */
    public View view;
    public WB_Text(View view) {
        this.view = view;
        init();
    }

    private void init(){
        user_image = (ImageView) view.findViewById(R.id.iv_user_image_fragment_text);
        tv_screen_name = (TextView) view.findViewById(R.id.tv_screen_name_fragment_text);
        tv_city = (TextView) view.findViewById(R.id.tv_city_fragment_text);
        tv_wb_create_time = (TextView) view.findViewById(R.id.tv_created_at_fragment_text);
        tv_wb_create_from = (TextView) view.findViewById(R.id.tv_created_from_fragment_text);
        tv_text = (TextView) view.findViewById(R.id.tv_text_fragment_text);
        lv_pics = (GridView) view.findViewById(R.id.grid_9pic_fragment_text);
        rbtn_retweet = (RadioButton) view.findViewById(R.id.rbtn_retweet_fragment_text);
        rbtn_comment = (RadioButton) view.findViewById(R.id.rbtn_comment_fragment_text);
        rbtn_like = (RadioButton) view.findViewById(R.id.rbtn_like_fragment_text);
        ll_retweeted = (LinearLayout) view.findViewById(R.id.ll_retweeted_fragment_text);
        tv_retweeted_text = (TextView) view.findViewById(R.id.tv_retweeted_text_fragment_text);
        lv_retweeted_pics = (GridView) view.findViewById(R.id.grid_retweeted_9pic_fragment_text);
        ll_toolbar = (LinearLayout) view.findViewById(R.id.ll_toolbar_fragment_text);
    }
}
