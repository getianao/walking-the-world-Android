package com.whut.getianao.walking_the_world_android.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.utility.ActivityUtil;
import com.whut.getianao.walking_the_world_android.utility.TokenUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ShareActivity extends AppCompatActivity implements View.OnClickListener {

    private Button addActivityBtn, addCoomentBtn, getAllActivitiesBtn, getCommentBtn;

    private void initView() {
        addActivityBtn = findViewById(R.id.buttonNewShare);
        addCoomentBtn = findViewById(R.id.buttonAddComment);
        getAllActivitiesBtn = findViewById(R.id.buttonAllActivities);
        getCommentBtn = findViewById(R.id.buttonGetComment);

        addActivityBtn.setOnClickListener(this);
        addCoomentBtn.setOnClickListener(this);
        getAllActivitiesBtn.setOnClickListener(this);
        getCommentBtn.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        initView();
    }

    private final int ADD_ACTIVITY_SUCCESS = 1;
    private final int ADD_ACTIVITY_FAILED = 2;
    private final int GET_ACTIVITIES_SUCCESS = 3;
    private final int GET_ACTIVITIES_FAILED = 4;
    private final int THUMB_UP_SUCCESS = 5;
    private final int THUMB_UP_FAILED = 6;
    private final int ADD_COMMENT_SUCCESS = 7;
    private final int ADD_COMMENT_FAILED = 8;
    private final int GET_COMMENT_SUCCESS = 9;
    private final int GET_COMMENT_FAILED = 10;



    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case ADD_ACTIVITY_SUCCESS:
                    Toast.makeText(getApplicationContext(), "新建动态成功！", Toast.LENGTH_LONG).show();
                    break;
                case ADD_ACTIVITY_FAILED:
                    Toast.makeText(getApplicationContext(), "新建动态失败！", Toast.LENGTH_LONG).show();
                    break;
                case GET_ACTIVITIES_SUCCESS:
                    Toast.makeText(getApplicationContext(), "获取朋友圈成功！", Toast.LENGTH_LONG).show();
                    break;
                case GET_ACTIVITIES_FAILED:
                    Toast.makeText(getApplicationContext(), "新建朋友圈失败！", Toast.LENGTH_LONG).show();
                    break;
                case THUMB_UP_SUCCESS:
                    Toast.makeText(getApplicationContext(), "点赞成功！", Toast.LENGTH_LONG).show();
                    break;
                case THUMB_UP_FAILED:
                    Toast.makeText(getApplicationContext(), "点赞失败！", Toast.LENGTH_LONG).show();
                    break;
                case ADD_COMMENT_SUCCESS:
                    Toast.makeText(getApplicationContext(), "添加评论成功！", Toast.LENGTH_LONG).show();
                    break;
                case ADD_COMMENT_FAILED:
                    Toast.makeText(getApplicationContext(), "添加评论失败！", Toast.LENGTH_LONG).show();
                    break;
                case GET_COMMENT_SUCCESS:
                    Toast.makeText(getApplicationContext(), "获取某条动态的所有成功！", Toast.LENGTH_LONG).show();
                    break;
                case GET_COMMENT_FAILED:
                    Toast.makeText(getApplicationContext(), "获取某条动态的所有失败！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonNewShare: // 新建动态
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, Object> data = new HashMap<>();
                        data.put("userId", 123);
                        data.put("title", "title123");
                        data.put("text", "text123");
                        data.put("imagUrls", "");
                        int result = 0;
                        try {
                            result = ActivityUtil.addActivity(data);
                            //从全局池中返回一个message实例，避免多次创建message（如new Message）
                            Message msg = Message.obtain();
                            msg.what = (result == 0) ? ADD_COMMENT_SUCCESS : ADD_COMMENT_FAILED;   //标志消息的标志
                            handler.sendMessage(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.buttonAddComment: // 添加评论
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       String text="这是一条评论！";
                        int result = 0;
                        try {
                            result = ActivityUtil.addComment(text,1,1);
                            //从全局池中返回一个message实例，避免多次创建message（如new Message）
                            Message msg = Message.obtain();
                            msg.what = (result == 0) ? ADD_COMMENT_SUCCESS : ADD_COMMENT_FAILED;   //标志消息的标志
                            handler.sendMessage(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            case R.id.buttonAllActivities: // 朋友圈
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject result = null;
                        result = ActivityUtil.getAllActivities(1);
                        //从全局池中返回一个message实例，避免多次创建message（如new Message）
                        Message msg = Message.obtain();
//                            msg.what = (result.getIgnt("res") == 0) ? GET_ACTIVITIES_SUCCESS : GET_ACTIVITIES_FAILED;   //标志消息的标志
                        msg.what = GET_ACTIVITIES_SUCCESS;
                        handler.sendMessage(msg);
                    }
                }).start();
                break;
            case R.id.buttonGetComment: // 动态的所有评论
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject result = null;
                        result = ActivityUtil.getAllComments(1);
                        //从全局池中返回一个message实例，避免多次创建message（如new Message）
                        Message msg = Message.obtain();
//                            msg.what = (result.getIgnt("res") == 0) ? GET_ACTIVITIES_SUCCESS : GET_ACTIVITIES_FAILED;   //标志消息的标志
                        msg.what = GET_COMMENT_SUCCESS;
                        handler.sendMessage(msg);
                    }
                }).start();
                break;



//            // 点赞代码
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    int result = -1;
//                    result =  ActivityUtil.thumbUp(10,1);
//                    //从全局池中返回一个message实例，避免多次创建message（如new Message）
//                    Message msg = Message.obtain();
//                    msg.what = (result == 0) ? THUMB_UP_SUCCESS : THUMB_UP_FAILED;   //标志消息的标志
//                    handler.sendMessage(msg);
//                }
//            }).start();
        }
    }
}
