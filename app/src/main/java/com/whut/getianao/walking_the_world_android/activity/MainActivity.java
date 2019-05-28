package com.whut.getianao.walking_the_world_android.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.data.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    private Button login_btn, signup_btn;
    private EditText username_input, pwd_input;

    /**
     * @param
     * @return
     * @description 获取到相关组件
     * @author Liu Zhian
     * @time 2019/5/28 0028 上午 10:29
     */
    private void initial_wigets() {
        login_btn = findViewById(R.id.login_btn);
        signup_btn = findViewById(R.id.signup_btn);
        username_input = findViewById(R.id.username_input);
        pwd_input = findViewById(R.id.pwd_input);
    }

    /**
     * @param
     * @return
     * @description 按钮监听事件注册
     * @author Liu Zhian
     * @time 2019/5/28 0028 上午 10:55
     */
    private void btn_register_listener() {
        // 用户注册测试
        signup_btn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
              MyUser myUser = new MyUser();
              myUser.setUsername(username_input.getText().toString());
              myUser.setPassword(pwd_input.getText().toString());
              myUser.setGender("男");
              myUser.setBio("hello world!");

              myUser.signUp(new SaveListener<MyUser>() {
                  @Override
                  public void done(MyUser myUser, BmobException e) {
                      if (e == null) {
                          Toast.makeText(MainActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                      } else {
                          Log.e("注册失败", "原因: ", e);
                      }
                  }
              });
              }
            }
        );

        // 登录按钮点击事件
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUser myUser = new MyUser();
                myUser.setUsername(username_input.getText().toString());
                myUser.setPassword(pwd_input.getText().toString());
                myUser.login(new SaveListener<BmobUser>() {
                    @Override
                    public void done(BmobUser bmobUser, BmobException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, bmobUser.getUsername() + "登录成功", Toast.LENGTH_SHORT).show();

                            // 跳转到个人信息页面
                            Intent intent=new Intent();
                            intent.setClass(MainActivity.this,UserInfoActivity.class);
                            startActivity(intent);

                        } else {
                            Log.e("登录失败", "原因: ", e);
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 默认初始化Bmob
        Bmob.initialize(this, "109c0e6013461cbdee50c31f5f68bc21");
        // 初始化组件
        initial_wigets();

        // 按钮事件注册
        btn_register_listener();

    }
}
