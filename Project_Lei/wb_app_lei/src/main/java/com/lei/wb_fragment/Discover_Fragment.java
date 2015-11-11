package com.lei.wb_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.lei.wb_adapter.FriendsAdapter;
import com.lei.wb_app_lei.R;
import com.lei.wb_model.DiscoverUser;
import com.lei.wb_utils.AccessTokenKeeper;
import com.lei.wb_utils.Constants;
import com.lei.wb_utils.ImageLoader_Init_Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.SearchAPI;
import com.sina.weibo.sdk.openapi.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/** 搜索界面 */
public class Discover_Fragment extends Fragment  {
    ListView lv_hot_user;
    ImageButton ibtn_discover;
    EditText edt_discover;
    SearchAPI mSearchAPI;
    UsersAPI mUsersAPI;
    Oauth2AccessToken mAccessToken;
    LayoutInflater inflater;
    ImageLoader imageLoader;
    DisplayImageOptions circleOptions;
    FriendsAdapter adapter;
    ArrayList<DiscoverUser> discoverUsers = new ArrayList<>();
    ArrayList<User> users = new ArrayList<>();
    int position=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover,container,false);
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        init(view);
//        listener();
//    }
//
//    private void init(View view) {
//        inflater = LayoutInflater.from(getActivity().getApplicationContext());
//        imageLoader = ImageLoader.getInstance();
//        imageLoader.init(ImageLoader_Init_Util.initConfiguration(getActivity().getApplicationContext()));
//        circleOptions = ImageLoader_Init_Util.initCircleDisplayImageOption(getActivity().getApplicationContext());
//        mAccessToken = AccessTokenKeeper.readAccessToken(getActivity().getApplicationContext());
//        mUsersAPI = new UsersAPI(getActivity().getApplicationContext(),Constants.APP_KEY,mAccessToken);
//        mSearchAPI = new SearchAPI(getActivity().getApplicationContext(), Constants.APP_KEY,mAccessToken);
//        lv_hot_user = (ListView) view.findViewById(R.id.lv_hot_user_fragment_discover);
//        ibtn_discover = (ImageButton) view.findViewById(R.id.ibtn_discover_fragment_discover);
//        edt_discover = (EditText) view.findViewById(R.id.edt_discover_fragment_discover);
//        adapter = new FriendsAdapter(inflater,users,imageLoader,circleOptions);
//        lv_hot_user.setAdapter(adapter);
//    }
//
//    private void listener() {
//        ibtn_discover.setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.ibtn_discover_fragment_discover:
//                if (edt_discover.getText()!=null&&!"".equals(edt_discover.getText()+"")){
//                    mSearchAPI.users(edt_discover.getText()+"",20,new MyRequestListener("search"));
//                }else{
//                    Toast.makeText(getActivity().getApplicationContext(), "搜索内容不能为空", Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }
//
//    class MyRequestListener implements RequestListener {
//        String doWhat;
//
//        public MyRequestListener(String doWhat) {
//            this.doWhat = doWhat;
//        }
//
//        @Override
//        public void onComplete(String s) {
//            switch (doWhat){
//                case "search":
//                    getDiscoverUsers(s);
//                    break;
//                case "user":
//                    if (position<discoverUsers.size()){
//                        User user = User.parse(s);
//                        users.add(user);
//                        mUsersAPI.show(discoverUsers.get(position++).uid,new MyRequestListener("user"));
//                    }else{
//                        adapter.setUsers_list(users);
//                        adapter.notifyDataSetChanged();
//                    }
//                    break;
//            }
//
//        }
//
//        private void getDiscoverUsers(String s) {
//            try {
//                discoverUsers.clear();
//                users.clear();
//                position = 0;
//                JSONArray jsonArray = new JSONArray(s);
//                for (int i =0;i<jsonArray.length();i++){
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    String screen_name = jsonObject.getString("screen_name");
//                    int followers_count = jsonObject.getInt("followers_count");
//                    long uid = jsonObject.getLong("uid");
//                    DiscoverUser discoverUser = new DiscoverUser(screen_name,followers_count,uid);
//                    discoverUsers.add(discoverUser);
//                }
//                if (discoverUsers.size()>0){
//                    mUsersAPI.show(discoverUsers.get(position++).uid,new MyRequestListener("user"));
//                }else{
//                    adapter.setUsers_list(users);
//                    adapter.notifyDataSetChanged();
//                    Toast.makeText(getActivity().getApplicationContext(), "为查询到任何结果", Toast.LENGTH_SHORT).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onWeiboException(WeiboException e) {
//            Log.e("error",e.toString());
//            Toast.makeText(getActivity().getApplicationContext(), "更新失败，请检查网络是否正常", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    class MyRequestListener2 implements RequestListener {
//
//        @Override
//        public void onComplete(String s) {
//            if (position<discoverUsers.size()){
//                User user = User.parse(s);
//                users.add(user);
//                mUsersAPI.show(discoverUsers.get(position++).uid,new MyRequestListener("user"));
//            }else{
//                adapter.setUsers_list(users);
//                adapter.notifyDataSetChanged();
//            }
//        }
//
//        @Override
//        public void onWeiboException(WeiboException e) {
//
//        }
//    }

    public void refreshDate(){

    }
}
