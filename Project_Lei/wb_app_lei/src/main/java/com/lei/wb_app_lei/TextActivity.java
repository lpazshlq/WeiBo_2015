package com.lei.wb_app_lei;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.lei.wb_adapter.CommentsAdapter;
import com.lei.wb_fragment.Text_Fragment;
import com.lei.wb_utils.AccessTokenKeeper;
import com.lei.wb_utils.Constants;
import com.lei.wb_utils.ImageLoader_Init_Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.legacy.FavoritesAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Comment;
import com.sina.weibo.sdk.openapi.models.CommentList;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 微博正文
 */
public class TextActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ll_loading;
    private RadioButton rbtn_return,rbtn_retweet_tab, rbtn_comment_tab, rbtn_like_tab, rbtn_retweet, rbtn_comment, rbtn_like;
    private ImageView iv_loading;
    private ImageButton ibtn_refresh;
    private ListView lv_retweet_comment_like;
    private Text_Fragment ft_text;
    private FragmentManager fm;
    private Status status;
    private Oauth2AccessToken accessToken;
    private StatusesAPI statusesAPI;
    private CommentsAPI commentsAPI;
    private FavoritesAPI favoritesAPI;
    private LayoutInflater inflater;
    private final static String RETWEET = "retweet";
    private final static String COMMENT = "comment";
    private final static String LIKE = "like";
    private ImageLoader mImageLoader;
    private DisplayImageOptions mOptions;
    private ArrayList<Comment> mComments = new ArrayList<>();
    private int commentsPage = 1;
    private Intent intent;
    private CommentsAdapter commentsAdapter;
    private final String RETWEET_CLICK = "retweet";
    private final String COMMENT_CLICK = "comment";
    private final String REFRESH = "refresh";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        getUtils();
        init();
        listener();
    }

    private void getUtils() {
        status = (Status) getIntent().getSerializableExtra("status");
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoader_Init_Util.initConfiguration(getApplicationContext()));
        mOptions = ImageLoader_Init_Util.initCircleDisplayImageOption(getApplicationContext());
        inflater = LayoutInflater.from(getApplicationContext());
        accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
        commentsAPI = new CommentsAPI(getApplicationContext(), Constants.APP_KEY, accessToken);
        commentsAPI.show(Long.parseLong(status.id), 0L, 0L, 20, commentsPage, CommentsAPI.AUTHOR_FILTER_ALL, new MyRequestListener_TextActivity(COMMENT));
        statusesAPI = new StatusesAPI(getApplicationContext(), Constants.APP_KEY, accessToken);
    }

    private void init() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        fm = getSupportFragmentManager();
        ft_text = (Text_Fragment) fm.findFragmentById(R.id.ft_text_activity_text);
        ft_text.setStatus(status, false);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading_activity_text);
        rbtn_return = (RadioButton) findViewById(R.id.rbtn_return_activity_text);
        ibtn_refresh = (ImageButton) findViewById(R.id.ibtn_refresh_activity_update);
        rbtn_retweet_tab = (RadioButton) findViewById(R.id.rbtn_retweet_tab_activity_text);
        rbtn_retweet_tab.setText("转发 " + status.reposts_count);
        rbtn_comment_tab = (RadioButton) findViewById(R.id.rbtn_comment_tab_activity_text);
        rbtn_comment_tab.setText("评论 " + status.comments_count);
        rbtn_like_tab = (RadioButton) findViewById(R.id.rbtn_like_tab_activity_text);
        rbtn_like_tab.setText("赞 " + status.attitudes_count);
        rbtn_retweet = (RadioButton) findViewById(R.id.rbtn_retweet_activity_text);
        rbtn_comment = (RadioButton) findViewById(R.id.rbtn_comment_activity_text);
        rbtn_like = (RadioButton) findViewById(R.id.rbtn_like_activity_text);
        iv_loading = (ImageView) findViewById(R.id.iv_loading_activity_text);
        lv_retweet_comment_like = (ListView) findViewById(R.id.lv_retweet_comment_like_activity_text);
        commentsAdapter = new CommentsAdapter(mComments, inflater, mImageLoader, mOptions);
        lv_retweet_comment_like.setAdapter(commentsAdapter);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void listener() {
        rbtn_retweet_tab.setOnClickListener(this);
        rbtn_comment_tab.setOnClickListener(this);
        rbtn_retweet.setOnClickListener(this);
        rbtn_comment.setOnClickListener(this);
        rbtn_return.setOnClickListener(this);
        ibtn_refresh.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbtn_retweet_tab_activity_text:
                break;
            case R.id.rbtn_comment_tab_activity_text:
                commentsAPI.show(Long.parseLong(status.id), 0L, 0L, 20, ++commentsPage, CommentsAPI.AUTHOR_FILTER_ALL, new MyRequestListener_TextActivity(COMMENT));
                break;
            case R.id.rbtn_retweet_activity_text:
                intent = new Intent(TextActivity.this, UpdateActivity.class);
                intent.putExtra("status", status);
                intent.putExtra("retweet", true);//通知UpdateActivity准备进行转发并初始化转发界面
                startActivity(intent);
                break;
            case R.id.rbtn_comment_activity_text:
                intent = new Intent(TextActivity.this, UpdateActivity.class);
                intent.putExtra("status", status);
                intent.putExtra("comment", true);//通知UpdateActivity准备进行转发并初始化转发界面
                startActivity(intent);
                break;
            case R.id.rbtn_return_activity_text:
                finish();
                break;
            case R.id.ibtn_refresh_activity_update:
                mComments.clear();
                commentsAdapter = new CommentsAdapter(mComments, inflater, mImageLoader, mOptions);
                lv_retweet_comment_like.setAdapter(commentsAdapter);
                commentsPage = 1;
                commentsAPI.show(Long.parseLong(status.id), 0L, 0L, 20, commentsPage, CommentsAPI.AUTHOR_FILTER_ALL, new MyRequestListener_TextActivity(COMMENT));
                break;
        }
    }

    class MyRequestListener_TextActivity implements RequestListener {
        String doWhat;

        public MyRequestListener_TextActivity(String doWhat) {
            this.doWhat = doWhat;
        }

        @Override
        public void onComplete(String s) {
            switch (doWhat) {
                case RETWEET:
                    Log.i("test", "retweetlist:::::::" + s);
                    break;
                case COMMENT:
                    CommentList commentList = CommentList.parse(s);
                    if (commentList!=null&&commentList.commentList != null){
                        for (Comment comment:commentList.commentList){
                            mComments.add(comment);
                        }
                        commentsAdapter.notifyDataSetChanged();
                        setListViewHeight(lv_retweet_comment_like);
                    }
                    break;
                case LIKE:
                    break;
                case REFRESH:
                    status = Status.parse(s);
                    ft_text.setStatus(status, false);
                    break;
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(TextActivity.this, ""+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 重新计算ListView的高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时起冲突的问题
     *
     * @param listView
     */
    public void setListViewHeight(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
