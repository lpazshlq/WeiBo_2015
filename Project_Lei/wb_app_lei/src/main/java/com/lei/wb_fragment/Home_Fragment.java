package com.lei.wb_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lei.wb_adapter.Pic9Adapter;
import com.lei.wb_app_lei.R;
import com.lei.wb_app_lei.UpdateActivity;
import com.lei.wb_utils.AccessTokenKeeper;
import com.lei.wb_utils.Constants;
import com.lei.wb_utils.Data_Util;
import com.lei.wb_utils.ImageLoader_Init_Util;
import com.lei.wb_model.WB_Text;
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
/** 关注微博列表主界面 */
public class Home_Fragment extends Fragment {
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private PullToRefreshListView mPullToRefreshListView;
    private ArrayList<Status> list = new ArrayList<>();
    private Oauth2AccessToken accessToken;
    private StatusesAPI statusesAPI;
    private MyListAdapter adapter;
    private int mScreenWidth;
    private LayoutInflater inflater;
    private WB_Text wb_text;
    private DisplayImageOptions options;
    private DisplayImageOptions circleOptions;
    private int update_page = 1;
    private boolean isDoing;
    private long since_id;
    private long max_id;
    private long since_id_up;
    private long max_id_up;
    private long since_id_down;
    private long max_id_down;
    private static final String TEXT_CLICK = "text";
    private final String RETWEET_CLICK = "retweet";
    private final String COMMENT_CLICK = "comment";
    public static final int UPDATE_FIRST = 0;
    public static final int UPDATE_LAST = 1;
    public static final String UPREFRESH = "uprefresh";
    public static final String DOWNREFRESH = "downrefresh";
    public static final String NORMAL = "normal";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inflater = LayoutInflater.from(getActivity().getApplicationContext());
        init(view);
    }

    private void init(View view) {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度

        options = ImageLoader_Init_Util.initDisplayImageOption(getActivity().getApplicationContext());
        circleOptions = ImageLoader_Init_Util.initCircleDisplayImageOption(getActivity().getApplicationContext());
        imageLoader.init(ImageLoader_Init_Util.initConfiguration(getActivity().getApplicationContext()));

        adapter = new MyListAdapter();
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_list);
        mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshListView.setAdapter(adapter);
        accessToken = AccessTokenKeeper.readAccessToken(getActivity().getApplicationContext());
        statusesAPI = new StatusesAPI(getActivity().getApplicationContext(), Constants.APP_KEY, accessToken);
        if (!isDoing){
            isDoing = true;
            statusesAPI.friendsTimeline(0L, 0L, 10, 1, false, 0, false, new MyRequestListener(NORMAL));
        }
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                statusesAPI.friendsTimeline(max_id_down, 0L, 100, 1, false, 0, false, new MyRequestListener(DOWNREFRESH));
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                statusesAPI.friendsTimeline(0L, since_id_up, 10, 1, false, 0, false, new MyRequestListener(UPREFRESH));
            }
        });
    }
    /** 最新微博更新 */
    public void refreshDate() {
        statusesAPI.friendsTimeline(since_id_up, 0, 200, 1, false, 0, false, new MyRequestListener(NORMAL));
    }

    class MyRequestListener implements RequestListener {
        String doWhat;

        public MyRequestListener(String doWhat) {
            this.doWhat = doWhat;
        }

        @Override
        public void onComplete(String s) {
            StatusList statuses = StatusList.parse(s);

            switch (doWhat) {
                case DOWNREFRESH://下拉更新
                    if (statuses != null && statuses.statusList != null) {
                        for (int i = 0; i < statuses.statusList.size(); i++) {
                            list.add(i, statuses.statusList.get(i));
                        }
                        max_id_down = Long.parseLong(list.get(0).id) + 1;
                        adapter.notifyDataSetChanged();
                    }
                    mPullToRefreshListView.onRefreshComplete();
                    break;
                case NORMAL://正常刷新
                    if (statuses != null && statuses.statusList != null) {
                        list.clear();
                        for (Status status : statuses.statusList) {
                            list.add(status);
                        }
                        max_id_down = Long.parseLong(list.get(0).id) + 1;
                        since_id_up = Long.parseLong(list.get(list.size() - 1).id) - 1;
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case UPREFRESH://上拉更新
                    if (statuses != null && statuses.statusList != null) {
                        for (Status status : statuses.statusList) {
                            list.add(status);
                        }
                        since_id_up = Long.parseLong(list.get(list.size() - 1).id) - 1;
                        adapter.notifyDataSetChanged();
                    }
                    mPullToRefreshListView.onRefreshComplete();
                    break;

            }

        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getActivity().getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
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
            wb_text.tv_text.setOnClickListener(new MyClickListener(TEXT_CLICK, status));
            if (status.retweeted_status != null) {
                wb_text.ll_retweeted.setVisibility(View.VISIBLE);
                wb_text.tv_retweeted_text.setText("@" + status.retweeted_status.user.screen_name + "：" + status.retweeted_status.text);
                wb_text.tv_retweeted_text.setOnClickListener(new MyClickListener(TEXT_CLICK, status.retweeted_status));
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
            wb_text.rbtn_retweet.setOnClickListener(new MyClickListener(RETWEET_CLICK, status));
            if (status.comments_count > 0) {
                wb_text.rbtn_comment.setText("   " + status.comments_count);
            } else {
                wb_text.rbtn_comment.setText("   " + "评论数");
            }
            wb_text.rbtn_comment.setOnClickListener(new MyClickListener(COMMENT_CLICK, status));
            if (status.attitudes_count > 0) {
                wb_text.rbtn_like.setText("   " + status.attitudes_count);
            } else {
                wb_text.rbtn_like.setText("   " + "赞");
            }
            if (status.pic_urls != null) {
                wb_text.lv_pics.setVisibility(View.VISIBLE);
                Pic9Adapter pic9Adapter = new MyPic9Adapter(status.pic_urls, inflater, imageLoader, options);
                wb_text.lv_pics.setAdapter(pic9Adapter);
                //动态增加lv_pics高度
                Pic_Util.setListViewHeightBasedOnChildren((GridView) wb_text.lv_pics);
            } else {
                wb_text.lv_pics.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshDate();
    }

    class MyClickListener implements View.OnClickListener {
        String doWhat;
        Status status;
        Intent intent;


        public MyClickListener(String doWhat, Status status) {
            this.doWhat = doWhat;
            this.status = status;
        }

        @Override
        public void onClick(View v) {
            switch (doWhat) {
                case TEXT_CLICK:
                    intent = new Intent("com.lei.action.textactivity");
                    intent.putExtra("status", status);
                    startActivityForResult(intent, 0x321);
                    break;
                case RETWEET_CLICK:
                    intent = new Intent(getActivity().getApplicationContext(), UpdateActivity.class);
                    intent.putExtra("status", status);
                    intent.putExtra("retweet", true);//通知UpdateActivity准备进行转发并初始化转发界面
                    startActivityForResult(intent, 0x321);
                    break;
                case COMMENT_CLICK:
                    intent = new Intent(getActivity().getApplicationContext(), UpdateActivity.class);
                    intent.putExtra("status", status);
                    intent.putExtra("comment", true);//通知UpdateActivity准备进行评论并初始化转发界面
                    startActivityForResult(intent, 0x321);
                    break;
            }
        }
    }

    /**
     * 继承自定义adapter并对监听传值优化
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
