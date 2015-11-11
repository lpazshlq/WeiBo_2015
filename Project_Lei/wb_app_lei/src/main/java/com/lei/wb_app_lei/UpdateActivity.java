package com.lei.wb_app_lei;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lei.wb_utils.AccessTokenKeeper;
import com.lei.wb_utils.Constants;
import com.lei.wb_utils.ImageLoader_Init_Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.CommentsAPI;
import com.sina.weibo.sdk.openapi.UsersAPI;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 发微博，转发，评论一体Activity
 */
public class UpdateActivity extends Activity implements View.OnClickListener {
    private ImageButton ibtn_update, ibtn_delete_pic;
    private ImageView iv_pic_add, iv_retweeted_first_pic;
    private TextView tv_title, tv_screen_name, tv_retweet_screen_name, tv_retweet_text;
    private LinearLayout ll_retweet_text;
    private FrameLayout fl_pic_add;
    private RadioButton rbtn_cancel, rbtn_picture;
    private EditText edt_text;
    private Status status;
    private Oauth2AccessToken accessToken;
    private MyRequestListener myRequestListener;
    private StatusesAPI statusesAPI;
    private CommentsAPI commentsAPI;
    private ImageLoader imageLoader;
    /**
     * 微博正文
     */
    private String text;
    /**
     * 上传图片的Bitmap对象
     */
    private Bitmap bitmap;
    private ArrayList<String> pics_add = new ArrayList<>();

    private String doWhat;

    private static final String UPDATE = "update";
    private static final String RETWEET = "retweet";
    private static final String COMMENT = "comment";
    private static final String USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        init();
        listener();
        isRetweetChecked();
        isCommentChecked();
    }

    /**
     * 检查是否为评论窗口，默认为发微博窗口
     */
    private void isCommentChecked() {
        if (getIntent().getBooleanExtra("comment", false)) {
            doWhat = COMMENT;
            tv_title.setText("发评论");
            edt_text.setHint("写评论....");
            status = (Status) getIntent().getSerializableExtra("status");
            rbtn_picture.setEnabled(false);
        }
    }

    /**
     * 检查是否为转发窗口，默认为发微博窗口
     */
    private void isRetweetChecked() {
        if (getIntent().getBooleanExtra("retweet", false)) {
            doWhat = RETWEET;
            tv_title.setText("转发微博");
            edt_text.setHint("说说分享心得....");
            status = (Status) getIntent().getSerializableExtra("status");
            ll_retweet_text.setVisibility(View.VISIBLE);
            rbtn_picture.setEnabled(false);
            if (status.retweeted_status != null) {
                edt_text.setText("//@" + status.user.screen_name + ":" + status.text);
                status = status.retweeted_status;//将当前引用中的原文微博对象赋值给当前引用，以便后期使用
                tv_retweet_screen_name.setText("@" + status.user.screen_name);
                tv_retweet_text.setText(status.text);
                if (status.bmiddle_pic != null && !"".equals(status.bmiddle_pic)) {
                    imageLoader.displayImage(status.bmiddle_pic, iv_retweeted_first_pic);
                }
            } else {
                tv_retweet_screen_name.setText("@" + status.user.screen_name);
                tv_retweet_text.setText(status.text);
                if (status.bmiddle_pic != null && !"".equals(status.bmiddle_pic)) {
                    imageLoader.displayImage(status.bmiddle_pic, iv_retweeted_first_pic);
                }
            }
        }
    }

    private void init() {
        doWhat = UPDATE;
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoader_Init_Util.initConfiguration(getApplicationContext()));
        accessToken = AccessTokenKeeper.readAccessToken(getApplicationContext());
        ibtn_update = (ImageButton) findViewById(R.id.ibtn_update_message_activity_update);
        ibtn_delete_pic = (ImageButton) findViewById(R.id.ibtn_delete_pic_activity_update);
        rbtn_cancel = (RadioButton) findViewById(R.id.rbtn_cancel_message_activity_update);
        rbtn_picture = (RadioButton) findViewById(R.id.rbtn_picture_activity_update);
        iv_pic_add = (ImageView) findViewById(R.id.iv_pic_add_activity_update);
        iv_retweeted_first_pic = (ImageView) findViewById(R.id.iv_retweeted_first_pic_activity_update);
        edt_text = (EditText) findViewById(R.id.edt_text_activity_update);
        tv_title = (TextView) findViewById(R.id.tv_title_activity_update);
        tv_screen_name = (TextView) findViewById(R.id.tv_screen_name_activity_update);
        tv_retweet_screen_name = (TextView) findViewById(R.id.tv_retweet_screen_name_activity_update);
        tv_retweet_text = (TextView) findViewById(R.id.tv_retweet_text_activity_update);
        ll_retweet_text = (LinearLayout) findViewById(R.id.ll_retweet_text_activity_update);
        fl_pic_add = (FrameLayout) findViewById(R.id.fl_pic_add_activity_update);
        ibtn_update.setEnabled(false);
        UsersAPI usersAPI = new UsersAPI(getApplicationContext(), Constants.APP_KEY, accessToken);
        usersAPI.show(Long.parseLong(accessToken.getUid()), new MyRequestListener(USER));
    }

    private void listener() {
        edt_text.addTextChangedListener(textWatcher);
        ibtn_update.setOnClickListener(this);
        rbtn_cancel.setOnClickListener(this);
        rbtn_picture.setOnClickListener(this);
        ibtn_delete_pic.setOnClickListener(this);
    }

    /**
     * 监听文本框字数变化
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            text = s.toString();
            if (!ibtn_update.isEnabled() && !"".equals(text) && text.length() < 141) {
                ibtn_update.setEnabled(true);
            }
            if (ibtn_update.isEnabled()) {
                if ("".equals(text) || text.length() > 140) {
                    ibtn_update.setEnabled(false);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_update_message_activity_update:
                updateMessage();
                break;
            case R.id.rbtn_cancel_message_activity_update:
                finish();
                break;
            case R.id.rbtn_picture_activity_update:
                Intent intent = new Intent(UpdateActivity.this, PhotoAlbumActivity.class);
                startActivityForResult(intent, 0x001);
                break;
            case R.id.ibtn_delete_pic_activity_update:
                fl_pic_add.setVisibility(View.GONE);
                iv_pic_add.setImageResource(R.mipmap.empty_picture);
                bitmap = null;
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            pics_add = data.getStringArrayListExtra("pics");
            if (pics_add != null && pics_add.size() > 0) {
                fl_pic_add.setVisibility(View.VISIBLE);
                new MyAsyncTask().execute(pics_add.get(0));
            }
        }
    }

    /**
     * 自定义异步获取本地图片Bitmap对象
     */
    class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);
            bitmap = b;
            iv_pic_add.setImageBitmap(b);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                FileInputStream fis = new FileInputStream(params[0]);
                Bitmap bitmap = BitmapFactory.decodeStream(fis);
                fis.close();
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void updateMessage() {
        if (accessToken != null && accessToken.isSessionValid()) {
            if (!"".equals(edt_text.getText() + "")) {
                switch (doWhat) {
                    case UPDATE:
                        statusesAPI = new StatusesAPI(getApplicationContext(), Constants.APP_KEY, accessToken);
                        myRequestListener = new MyRequestListener(UPDATE);
                        if (bitmap == null) {
                            statusesAPI.update(edt_text.getText() + "", null, null, myRequestListener);
                        } else {
                            statusesAPI.upload(edt_text.getText() + "", bitmap, null, null, myRequestListener);
                        }
                        break;
                    case RETWEET:
                        statusesAPI = new StatusesAPI(getApplicationContext(), Constants.APP_KEY, accessToken);
                        myRequestListener = new MyRequestListener(RETWEET);
                        statusesAPI.repost(Long.parseLong(status.id), edt_text.getText() + "", StatusesAPI.COMMENTS_NONE, myRequestListener);
                        break;
                    case COMMENT:
                        commentsAPI = new CommentsAPI(getApplicationContext(), Constants.APP_KEY, accessToken);
                        myRequestListener = new MyRequestListener(COMMENT);
                        commentsAPI.create(edt_text.getText() + "", Long.parseLong(status.id), false, myRequestListener);
                        break;
                }

            }
        }
    }

    class MyRequestListener implements RequestListener {
        String doWhat;

        public MyRequestListener(String doWhat) {
            this.doWhat = doWhat;
        }

        @Override
        public void onComplete(String s) {
            switch (doWhat) {
                case UPDATE:
                    Toast.makeText(getApplicationContext(), "微博发送成功", Toast.LENGTH_SHORT).show();
                    setResult(0x123);
                    finish();
                    break;
                case RETWEET:
                    Toast.makeText(getApplicationContext(), "微博转发成功", Toast.LENGTH_SHORT).show();
                    setResult(0x123);
                    finish();
                    break;
                case COMMENT:
                    Toast.makeText(getApplicationContext(), "微博评论成功", Toast.LENGTH_SHORT).show();
                    setResult(0x123);
                    finish();
                    break;
                case USER:
                    User user = User.parse(s);
                    tv_screen_name.setText(user.screen_name + "");
                    break;
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Log.e("error", e.toString());
            Toast.makeText(UpdateActivity.this, "操作失败，请检查网络是否正常...", Toast.LENGTH_SHORT).show();
        }
    }
}
