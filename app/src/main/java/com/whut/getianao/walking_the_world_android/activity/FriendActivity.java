package com.whut.getianao.walking_the_world_android.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.utility.UserUtil;

import org.json.JSONObject;

public class FriendActivity extends AppCompatActivity implements View.OnClickListener {
    private Button addFriendBtn, deleteFriendBtn, getAllFriendsBtn, getAllUsersBtn;

    private void initView() {
        addFriendBtn = findViewById(R.id.buttonAddFriend);
        deleteFriendBtn = findViewById(R.id.buttonDeleteFriend);
        getAllFriendsBtn = findViewById(R.id.buttonQueryAllFriends);
        getAllUsersBtn = findViewById(R.id.buttonQueryUser);

        addFriendBtn.setOnClickListener(this);
        deleteFriendBtn.setOnClickListener(this);
        getAllFriendsBtn.setOnClickListener(this);
        getAllUsersBtn.setOnClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonAddFriend: // 加朋友
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int res = UserUtil.addFriend(1,3);
                        System.out.println(res);
                    }
                }).start();
                break;
            case R.id.buttonDeleteFriend: // 删朋友
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int res = UserUtil.deleteFriend(1,2);
                        System.out.println(res);
                    }
                }).start();
            case R.id.buttonQueryAllFriends: // 所有朋友
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject res = UserUtil.allFriends(1);
                        System.out.println(res);
                    }
                }).start();
                break;
            case R.id.buttonQueryUser: // 查找用户
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject user = UserUtil.queryUser("10@qq.com");
                        System.out.println(user);
                    }
                }).start();
                break;

        }
    }
}
