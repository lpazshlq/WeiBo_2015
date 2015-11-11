package com.lei.wb_app_lei;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.lei.wb_adapter.MyFragmentPagerAdapter;
import com.lei.wb_adapter.ViewPagerAdapter;
import com.lei.wb_fragment.Discover_Fragment;
import com.lei.wb_fragment.Home_Fragment;
import com.lei.wb_fragment.Message_Fragment;
import com.lei.wb_fragment.Profile_Fragment;

import java.util.ArrayList;
/** 用户Viewpager主界面 */
public class UserActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private ImageButton ibtn_share;
    private RadioGroup radioGroup;
    private RadioButton rbtn_home,rbtn_discover,rbtn_message,rbtn_profile;
    private ViewPager mViewPager;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private int isSelected_rbtn = R.id.rbtn_home_activity_user;
    private int[] rbtns = {R.id.rbtn_home_activity_user,R.id.rbtn_message_activity_user,R.id.rbtn_discover_activity_user,R.id.rbtn_profile_activity_user};
    private MyFragmentPagerAdapter pagerAdapter;
    private Home_Fragment mHome_fragment = new Home_Fragment();
    private Message_Fragment mMessage_fragment = new Message_Fragment();
    private Discover_Fragment mDiscover_fragment = new Discover_Fragment();
    private Profile_Fragment mProfile_fragment = new Profile_Fragment();
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        init();
        listener();
    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        pagerAdapter = new MyFragmentPagerAdapter(fragmentManager);
        mViewPager = (ViewPager) findViewById(R.id.vpager_fragment_activity_user);
        radioGroup = (RadioGroup) findViewById(R.id.rgroup_activity_user);
        rbtn_home = (RadioButton) findViewById(R.id.rbtn_home_activity_user);
        rbtn_discover = (RadioButton) findViewById(R.id.rbtn_discover_activity_user);
        rbtn_message = (RadioButton) findViewById(R.id.rbtn_message_activity_user);
        rbtn_profile = (RadioButton) findViewById(R.id.rbtn_profile_activity_user);
        ibtn_share = (ImageButton) findViewById(R.id.ibtn_share_activity_user);

        mFragments.add(mHome_fragment);
        mFragments.add(mMessage_fragment);
        mFragments.add(mDiscover_fragment);
        mFragments.add(mProfile_fragment);
        pagerAdapter.setmFragments(mFragments);
        mViewPager.setAdapter(pagerAdapter);
    }

    private void listener() {
        //监听groupradiobutton点击事件
        radioGroup.setOnCheckedChangeListener(this);
        //监听发布微博控件
        ibtn_share.setOnClickListener(this);
        //监听微博刷新事件
        rbtn_home.setOnClickListener(this);
        //监听消息界面刷新事件
        rbtn_message.setOnClickListener(this);
        //监听搜索界面刷新事件
        rbtn_discover.setOnClickListener(this);
        //监听个人信息界面刷新事件
        rbtn_profile.setOnClickListener(this);
        //监听viewpager界面切换事件
        mViewPager.addOnPageChangeListener(new MyPagerChangeListener());
//        mViewPager.setOnPageChangeListener(new MyPagerChangeListener());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            //微博正文界面
            case R.id.rbtn_home_activity_user:
                isSelected_rbtn= checkedId;
                mViewPager.setCurrentItem(0);
                break;
            //微博搜索界面
            case R.id.rbtn_message_activity_user:
                isSelected_rbtn= checkedId;
                mViewPager.setCurrentItem(1);
                break;
            //微博消息界面
            case R.id.rbtn_discover_activity_user:
                isSelected_rbtn= checkedId;
                mViewPager.setCurrentItem(2);
                break;
            //微博个人信息界面
            case R.id.rbtn_profile_activity_user:
                isSelected_rbtn= checkedId;
                mViewPager.setCurrentItem(3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHome_fragment.refreshDate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_share_activity_user:
                startActivityForResult(new Intent("com.lei.action.share_activity"), 0x321);
                break;
            case R.id.rbtn_home_activity_user:
                if (isSelected_rbtn == R.id.rbtn_home_activity_user){
                    mHome_fragment.refreshDate();
                }
                break;
            case R.id.rbtn_message_activity_user:
                if (isSelected_rbtn == R.id.rbtn_message_activity_user){
                    mMessage_fragment.refreshDate();
                }
                break;
            case R.id.rbtn_discover_activity_user:
                if (isSelected_rbtn == R.id.rbtn_discover_activity_user){
                    mDiscover_fragment.refreshDate();
                }
                break;
            case R.id.rbtn_profile_activity_user:
                if (isSelected_rbtn == R.id.rbtn_profile_activity_user){
                    mProfile_fragment.refreshDate();
                }
                break;
        }
    }
    /** viewpager切换监听 */
    class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            radioGroup.check(rbtns[position]);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
