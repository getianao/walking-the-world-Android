package com.whut.getianao.walking_the_world_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.whut.getianao.walking_the_world_android.data.MyUser;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserInfoActivity extends AppCompatActivity {

    private Button btn_modify;
    private EditText editText_username, editText_email, editText_phone, editText_gender;

    /**
     * @param
     * @return
     * @description 获取到相关组件
     * @author Liu Zhian
     * @time 2019/5/28 0028 上午 10:29
     */
    private void initial_wigets() {
        // 获取到组件
        btn_modify = findViewById(R.id.btn_modify);
        editText_username = findViewById(R.id.editText_username);
        editText_email = findViewById(R.id.editText_email);
        editText_phone = findViewById(R.id.editText_phone);
        editText_gender = findViewById(R.id.editText_gender);

        // 这里需要设计activity之前通过intent传参
        // 暂时硬编码3537的id
        BmobQuery<MyUser> userQuery = new BmobQuery<>();
        userQuery.getObject("04c6ab98e4", new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e == null) {
                    // 根据查询结果赋值给相应控件
                    editText_username.setText(myUser.getUsername());
                    editText_username.setEnabled(false);  // username不可以改变
                    editText_email.setText(myUser.getEmail());
                    editText_phone.setText(myUser.getMobilePhoneNumber());
                    editText_gender.setText(myUser.getGender());
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });

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
        btn_modify.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              MyUser myUser = new MyUser();
                                              myUser.setUsername(editText_username.getText().toString());
                                              myUser.setGender(editText_gender.getText().toString());
                                              myUser.setEmail(editText_email.getText().toString());
                                              myUser.setMobilePhoneNumber(editText_phone.getText().toString());

                                              myUser.update("04c6ab98e4", new UpdateListener() {
                                                  @Override
                                                  public void done(BmobException e) {
                                                      if (e == null) {
                                                          Log.i("bmob", "更新成功");
                                                      } else {
                                                          Log.i("bmob", "更新失败：" + e.getMessage() + "," + e.getErrorCode());
                                                      }
                                                  }
                                              });
                                          }
                                      }
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // 初始化组件
        initial_wigets();
        // 注册事件监听
        btn_register_listener();
    }
}
