package com.lei.wb_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lei.wb_app_lei.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
/** 九宫格图片适配器 */
public abstract class Pic9Adapter extends BaseAdapter {
    ArrayList<String> pics;
    LayoutInflater inflater;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    int pic_position;

    /**
     * 九宫图适配器构造方法
     * @param pics 显示图片地址集合
     * @param inflater
     * @param imageLoader 图片显示工具
     * @param options 图片显示配置
     */
    public Pic9Adapter(ArrayList<String> pics,LayoutInflater inflater,ImageLoader imageLoader,DisplayImageOptions options) {
        this.pics = pics;
        this.inflater = inflater;
        this.imageLoader = imageLoader;
        this.options = options;
    }

    @Override
    public int getCount() {
        return pics.size();
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
        convertView = inflater.inflate(R.layout.grid_9pic_item,parent,false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iv_9pic_item);
        imageLoader.displayImage(pics.get(position), imageView,options);
        pic_position = position;
        imageView.setOnClickListener(new Pic_9_Listener(pics,pic_position));
        return convertView;
    }
    /** 图片点击事件监听内部类 */
    private class Pic_9_Listener implements View.OnClickListener {
        ArrayList<String> pics;
        int position;

        public Pic_9_Listener(ArrayList<String> pics,int position) {
            this.pics = pics;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            pic_9_Listener(pics,position);
        }
    }
    /** 图片点击事件抽象方法 */
    public abstract void pic_9_Listener(ArrayList<String> pics, int position);
}
