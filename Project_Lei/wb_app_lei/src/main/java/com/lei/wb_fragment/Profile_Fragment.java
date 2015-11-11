package com.lei.wb_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lei.wb_app_lei.FavoritesActivity;
import com.lei.wb_app_lei.FollowerActivity;
import com.lei.wb_app_lei.FriendsActivity;
import com.lei.wb_app_lei.MyStatusActivity;
import com.lei.wb_app_lei.R;
import com.lei.wb_utils.AccessTokenKeeper;
import com.lei.wb_utils.Constants;
import com.lei.wb_utils.ImageLoader_Init_Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.models.User;

/** 个人信息界面 */
public class Profile_Fragment extends Fragment implements View.OnClickListener {
    private Oauth2AccessToken accessToken;
    private UsersAPI usersAPI;
    private ImageLoader imageLoader;
    private DisplayImageOptions roundOptions;

    private ImageButton ibtn_refresh;
    private ImageView iv_user_pic, iv_backgroun01, iv_backgroun02;
    private TextView tv_user_screen_name, tv_user_description;
    private RadioButton rbtn_friend, rbtn_status, rbtn_follower, rbtn_favourite;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        setValues();
        listener();
    }

    private void setValues() {
        if (accessToken != null && accessToken.isSessionValid()) {
            usersAPI = new UsersAPI(getActivity().getApplicationContext(), Constants.APP_KEY, accessToken);
            long uid = Long.parseLong(accessToken.getUid());
            usersAPI.show(uid, new MyRequestListener());
        }
    }

    public void refreshDate() {

    }

    private void init(View view) {
        accessToken = AccessTokenKeeper.readAccessToken(getActivity().getApplicationContext());
        imageLoader = ImageLoader.getInstance();
        roundOptions = ImageLoader_Init_Util.initRoundDisplayImageOption(getActivity().getApplicationContext());
        imageLoader.init(ImageLoader_Init_Util.initConfiguration(getActivity().getApplicationContext()));
        ibtn_refresh = (ImageButton) view.findViewById(R.id.ibtn_refresh_fragment_profile);
        iv_user_pic = (ImageView) view.findViewById(R.id.iv_user_pic_fragment_profile);
        iv_backgroun01 = (ImageView) view.findViewById(R.id.iv_background1_fragment_profile);
        iv_backgroun02 = (ImageView) view.findViewById(R.id.iv_background2_fragment_profile);
        tv_user_screen_name = (TextView) view.findViewById(R.id.tv_user_screen_name_fragment_profile);
        tv_user_description = (TextView) view.findViewById(R.id.tv_user_description_fragment_profile);
        rbtn_friend = (RadioButton) view.findViewById(R.id.rbtn_friend_fragment_profile);
        rbtn_status = (RadioButton) view.findViewById(R.id.rbtn_status_fragment_profile);
        rbtn_follower = (RadioButton) view.findViewById(R.id.rbtn_follower_fragment_profile);
        rbtn_favourite = (RadioButton) view.findViewById(R.id.rbtn_favourite_fragment_profile);
    }

    private void listener() {
        ibtn_refresh.setOnClickListener(this);
        rbtn_friend.setOnClickListener(this);
        rbtn_status.setOnClickListener(this);
        rbtn_follower.setOnClickListener(this);
        rbtn_favourite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ibtn_refresh_fragment_profile:
                break;
            case R.id.rbtn_friend_fragment_profile:
                intent = new Intent(getActivity().getApplicationContext(), FriendsActivity.class);
                startActivity(intent);
                break;
            case R.id.rbtn_status_fragment_profile:
                intent = new Intent(getActivity().getApplicationContext(), MyStatusActivity.class);
                startActivity(intent);
                break;
            case R.id.rbtn_follower_fragment_profile:
                intent = new Intent(getActivity().getApplicationContext(), FollowerActivity.class);
                startActivity(intent);
                break;
            case R.id.rbtn_favourite_fragment_profile:
                intent = new Intent(getActivity().getApplicationContext(), FavoritesActivity.class);
                startActivity(intent);
                break;
        }
    }

    class MyRequestListener implements RequestListener {

        @Override
        public void onComplete(String s) {
            Log.i("test",s.toString());
            User user = User.parse(s);
            if (user != null) {
                imageLoader.displayImage(user.avatar_large, iv_user_pic, roundOptions);
                tv_user_screen_name.setText(user.screen_name + "");
                tv_user_description.setText(user.description + "");
                rbtn_friend.setText(user.friends_count + "\n关注");
                rbtn_status.setText(user.statuses_count + "\n微博");
                rbtn_follower.setText(user.followers_count + "\n粉丝");
                rbtn_favourite.setText(user.favourites_count + "\n收藏");
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getActivity().getApplicationContext(), "" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
