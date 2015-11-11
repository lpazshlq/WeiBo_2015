package com.lei.wb_model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lei.wb_app_lei.R;

public class WB_Comment {
    public ImageView iv_user_image;
    public TextView tv_user_screen_name;
    public TextView tv_comment_create_time;
    public TextView tv_comment_text;

    public WB_Comment(View view){
        init(view);
    }

    private void init(View view){
        iv_user_image = (ImageView) view.findViewById(R.id.iv_user_image_activity_comment_text_item);
        tv_user_screen_name = (TextView) view.findViewById(R.id.tv_user_screen_name_activity_comment_text_item);
        tv_comment_create_time = (TextView) view.findViewById(R.id.tv_comment_create_time_activity_comment_text_item);
        tv_comment_text = (TextView) view.findViewById(R.id.tv_comment_text_activity_comment_text_item);
    }
}
