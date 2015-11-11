package com.lei.wb_model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lei.wb_app_lei.R;

public class WB_Friend {
    public ImageView iv_friend_pic;
    public TextView tv_friend_screen_name,tv_friend_description;

    public WB_Friend(View view) {
        init(view);
    }

    private void init(View view) {
        iv_friend_pic = (ImageView) view.findViewById(R.id.iv_friend_pic_activity_friend_item);
        tv_friend_screen_name = (TextView) view.findViewById(R.id.tv_friend_screen_name_activity_friend_item);
        tv_friend_description = (TextView) view.findViewById(R.id.tv_friend_description_activity_friend_item);
    }
}
