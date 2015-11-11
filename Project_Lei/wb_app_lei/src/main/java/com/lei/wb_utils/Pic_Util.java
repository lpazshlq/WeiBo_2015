package com.lei.wb_utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * 自定义大中小图url转换工具类
 */
public class Pic_Util {
    private String bmiddle_pic = "bmiddle";
    private String original_pic = "large";

    public static String getMiddlePic(String thumbnail_pic) {
        String[] strs = thumbnail_pic.split("/");
        return strs[0]+"//"+strs[2]+"/"+"bmiddle"+"/"+strs[4];
    }

    public static String getOriginalPic(String thumbnail_pic) {
        String[] strs = thumbnail_pic.split("/");
        return strs[0]+"//"+strs[2]+"/"+"large"+"/"+strs[4];
    }

    /**
     * 动态增加九宫格高度
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(GridView listView) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 3;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
            Log.i("tag", listItem.getMeasuredHeight() + "  itemHeight");
        }
        //获取height由于image间距的差值
        int d_value_Height;
        if (listAdapter.getCount() == 0) {
            d_value_Height = 0;
        } else if (listAdapter.getCount() < 4) {
            d_value_Height = 16;
        } else if (listAdapter.getCount() < 7) {
            d_value_Height = 24;
        } else {
            d_value_Height = 32;
        }
        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        params.height = totalHeight + d_value_Height;
        // 设置margin
//        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        // 设置参数
        listView.setLayoutParams(params);
    }

}
