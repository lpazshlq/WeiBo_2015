package com.lei.wb_adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lei.wb_app_lei.R;
import com.lei.wb_model.WB_Comment;
import com.lei.wb_utils.Data_Util;
import com.lei.wb_utils.ImageLoader_Init_Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;

import java.util.ArrayList;
/** 评论列表适配器 */
public class CommentsAdapter extends BaseAdapter {
    WB_Comment mWb_comment;
    ArrayList<Comment> mComments;
    LayoutInflater mInflater;
    ImageLoader mImageLoader;
    DisplayImageOptions mOptions;

    public CommentsAdapter(ArrayList<Comment> mComments, LayoutInflater inflater, ImageLoader imageLoader, DisplayImageOptions options) {
        this.mComments = mComments;
        this.mInflater = inflater;
        this.mImageLoader = imageLoader;
        this.mOptions = options;
    }

    @Override
    public int getCount() {
        return mComments.size();
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
        Comment mComment = mComments.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_comment_text_item, parent, false);
            mWb_comment = new WB_Comment(convertView);
            convertView.setTag(mWb_comment);
        } else {
            mWb_comment = (WB_Comment) convertView.getTag();
        }
        mImageLoader.displayImage(mComment.user.profile_image_url, mWb_comment.iv_user_image, mOptions);
        mWb_comment.tv_user_screen_name.setText(mComment.user.screen_name+"");
        mWb_comment.tv_comment_create_time.setText(Data_Util.getData(mComment.created_at)+"");
        mWb_comment.tv_comment_text.setText(mComment.text);
        return convertView;
    }
}
