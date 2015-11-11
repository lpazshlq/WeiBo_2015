package com.lei.wb_app_lei;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.lei.wb_adapter.FriendsAdapter;
import com.lei.wb_utils.AccessTokenKeeper;
import com.lei.wb_utils.Constants;
import com.lei.wb_utils.ImageLoader_Init_Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
import com.sina.weibo.sdk.openapi.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private ImageButton ibtn_refresh,ibtn_return;
    private ListView lv_friends;
    private Oauth2AccessToken mAccessToken;
    private FriendshipsAPI mFriendshipsAPI;
    private ArrayList<User> users_list = new ArrayList<>();
    private FriendsAdapter mFriendsAdapter;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private DisplayImageOptions circleOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        init();
        listener();
    }

    private void listener() {
        ibtn_refresh.setOnClickListener(this);
        ibtn_return.setOnClickListener(this);
        lv_friends.setOnItemClickListener(this);
    }

    private void init() {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoader_Init_Util.initConfiguration(getApplicationContext()));
        circleOptions = ImageLoader_Init_Util.initCircleDisplayImageOption(getApplicationContext());
        inflater = LayoutInflater.from(getApplicationContext());
        mFriendsAdapter = new FriendsAdapter(inflater,users_list,imageLoader,circleOptions);
        mAccessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
        mFriendshipsAPI = new FriendshipsAPI(getApplicationContext(), Constants.APP_KEY, mAccessToken);
        ibtn_refresh = (ImageButton) findViewById(R.id.ibtn_refresh_activity_friend);
        ibtn_return = (ImageButton) findViewById(R.id.ibtn_return_activity_friend);
        lv_friends = (ListView) findViewById(R.id.lv_friends_activity_friend);
        lv_friends.setAdapter(mFriendsAdapter);
        mFriendshipsAPI.friends(Long.parseLong(mAccessToken.getUid()), 200, 0, false, new MyRequestListener());
    }
    /** 刷新按钮点击事件 */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibtn_refresh_activity_friend:
                users_list.clear();
                mFriendshipsAPI.friends(Long.parseLong(mAccessToken.getUid()), 200, 0, false, new MyRequestListener());
                break;
            case R.id.ibtn_return_activity_friend:
                finish();
                break;
        }
    }
    /** friends列表点击事件 */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    class MyRequestListener implements RequestListener {

        @Override
        public void onComplete(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("users");
                for (int i = 0; i < jsonArray.length(); i++) {
                    User user = User.parse(jsonArray.getString(i));
                    users_list.add(user);
                }
                mFriendsAdapter.setUsers_list(users_list);
                mFriendsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.e("error",""+e.toString());
            Toast.makeText(getApplicationContext(), "更新失败，请检查网络是否正常...", Toast.LENGTH_SHORT).show();
        }
    }
}
