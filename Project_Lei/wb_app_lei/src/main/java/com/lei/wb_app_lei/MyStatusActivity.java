package com.lei.wb_app_lei;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lei.wb_adapter.Pic9Adapter;
import com.lei.wb_model.WB_Text;
import com.lei.wb_utils.AccessTokenKeeper;
import com.lei.wb_utils.Constants;
import com.lei.wb_utils.Data_Util;
import com.lei.wb_utils.ImageLoader_Init_Util;
import com.lei.wb_utils.Pic_Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;

import java.util.ArrayList;

/**  */
public class MyStatusActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton ibtn_refresh, ibtn_return;
    private ListView lv_status;
    private Oauth2AccessToken mAccessToken;
    private StatusesAPI mStatusesAPI;
    private WB_Text wb_text;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private DisplayImageOptions circleOptions;
    private DisplayImageOptions options;
    private ArrayList<Status> list = new ArrayList<>();
    private MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_status);
        init();
        listener();
    }

    private void listener() {
        ibtn_refresh.setOnClickListener(this);
        ibtn_return.setOnClickListener(this);
    }

    private void init() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoader_Init_Util.initConfiguration(getApplicationContext()));
        options = ImageLoader_Init_Util.initDisplayImageOption(getApplicationContext());
        circleOptions = ImageLoader_Init_Util.initCircleDisplayImageOption(getApplicationContext());
        inflater = LayoutInflater.from(getApplicationContext());
        mAccessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
        mStatusesAPI = new StatusesAPI(getApplicationContext(), Constants.APP_KEY, mAccessToken);
        ibtn_refresh = (ImageButton) findViewById(R.id.ibtn_refresh_activity_my_status);
        ibtn_return = (ImageButton) findViewById(R.id.ibtn_return_activity_my_status);
        lv_status = (ListView) findViewById(R.id.lv_status_activity_my_status);
        adapter = new MyListAdapter();
        lv_status.setAdapter(adapter);
        mStatusesAPI.userTimeline(0L, 0L, 100, 1, false, 0, false, new MyRequestListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_refresh_activity_my_status:
                list.clear();
                mStatusesAPI.userTimeline(0L, 0L, 100, 1, false, 0, false, new MyRequestListener());
                break;
            case R.id.ibtn_return_activity_my_status:
                finish();
                break;
        }
    }

    public void refresh() {

    }

    class MyRequestListener implements RequestListener {

        @Override
        public void onComplete(String s) {
            StatusList statusList = StatusList.parse(s);
            if (statusList != null && statusList.statusList != null) {
                for (Status status : statusList.statusList) {
                    list.add(status);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.e("error", "" + e.toString());
            Toast.makeText(MyStatusActivity.this, "更新失败，请检查网络是否正常...", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 微博列表适配器
     */
    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView != null) {
                wb_text = (WB_Text) convertView.getTag();
            } else {
                convertView = inflater.inflate(R.layout.fragment_text, parent, false);
                wb_text = new WB_Text(convertView);
                convertView.setTag(wb_text);
            }//item界面重用优化
            Status status = list.get(position);
            imageLoader.displayImage(status.user.profile_image_url, wb_text.user_image, circleOptions);
            wb_text.tv_screen_name.setText(status.user.screen_name + "");
            wb_text.tv_wb_create_time.setText(Data_Util.getData(status.created_at) + "");
            wb_text.tv_text.setText(status.text + "");
            wb_text.tv_text.setOnClickListener(new MyClickListener(status));

            if (status.retweeted_status != null) {
                wb_text.ll_retweeted.setVisibility(View.VISIBLE);
                wb_text.tv_retweeted_text.setText("@" + status.retweeted_status.user.screen_name + "：" + status.retweeted_status.text);
                wb_text.tv_retweeted_text.setOnClickListener(new MyClickListener(status.retweeted_status));
                if (status.retweeted_status.pic_urls != null) {
                    wb_text.lv_retweeted_pics.setVisibility(View.VISIBLE);
                    Pic9Adapter retweeted_pic9Adapter = new MyPic9Adapter(status.retweeted_status.pic_urls, inflater, imageLoader, options);
                    wb_text.lv_retweeted_pics.setAdapter(retweeted_pic9Adapter);
                    Pic_Util.setListViewHeightBasedOnChildren((GridView) wb_text.lv_retweeted_pics);
                } else {
                    wb_text.lv_retweeted_pics.setVisibility(View.GONE);
                }
            } else {
                wb_text.ll_retweeted.setVisibility(View.GONE);
            }
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
            if (status.pic_urls != null) {
                wb_text.lv_pics.setVisibility(View.VISIBLE);
                Pic9Adapter pic9Adapter = new MyPic9Adapter(status.pic_urls, inflater, imageLoader, options);
                wb_text.lv_pics.setAdapter(pic9Adapter);
                Pic_Util.setListViewHeightBasedOnChildren((GridView) wb_text.lv_pics);
            } else {
                wb_text.lv_pics.setVisibility(View.GONE);
            }
            return convertView;
        }
    }
    /** 微博正文点击事件 */
    class MyClickListener implements View.OnClickListener {
        Status status;

        public MyClickListener(Status status) {
            this.status = status;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent("com.lei.action.textactivity");
            intent.putExtra("status", status);
            startActivity(intent);
        }
    }

    /**
     * 继承自定义Pic9Adapter重写监听事件
     */
    private class MyPic9Adapter extends Pic9Adapter {

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
