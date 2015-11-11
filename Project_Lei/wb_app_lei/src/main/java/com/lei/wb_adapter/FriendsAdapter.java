package com.lei.wb_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lei.wb_app_lei.R;
import com.lei.wb_model.WB_Friend;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.openapi.models.User;

import java.util.ArrayList;

public class FriendsAdapter extends BaseAdapter {
    WB_Friend mWb_friend;
    LayoutInflater inflater;
    ArrayList<User> users_list;
    ImageLoader imageLoader;
    DisplayImageOptions circleOptions;

    public FriendsAdapter(LayoutInflater inflater, ArrayList<User> users_list, ImageLoader imageLoader, DisplayImageOptions circleOptions) {
        this.inflater = inflater;
        this.users_list = users_list;
        this.imageLoader = imageLoader;
        this.circleOptions = circleOptions;
    }

    public void setUsers_list(ArrayList<User> users_list) {
        this.users_list = users_list;
    }

    @Override
    public int getCount() {
        return users_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        User user = users_list.get(position);
        if (convertView==null){
            convertView = inflater.inflate(R.layout.activity_friend_item,parent,false);
            mWb_friend = new WB_Friend(convertView);
            convertView.setTag(mWb_friend);
        }else{
            mWb_friend = (WB_Friend) convertView.getTag();
        }
        imageLoader.displayImage(user.profile_image_url,mWb_friend.iv_friend_pic,circleOptions);
        mWb_friend.tv_friend_screen_name.setText(user.screen_name);
        mWb_friend.tv_friend_description.setText(user.description);
        return convertView;
    }
}
