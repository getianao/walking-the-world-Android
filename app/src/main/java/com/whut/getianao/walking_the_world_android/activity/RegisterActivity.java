package com.whut.getianao.walking_the_world_android.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.utility.TokenUtil;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailInput, et_register_pwd_input;
    private TextView getTokenText,et_register_auth_code;
    private Button bt_register_submit;

    private final int GET_TOKEN_SUCCESS = 0;  // 获取验证码成功
    private final int GET_TOKEN_FAILED = 1; // 获取验证码失败
    private final int SIGN_UP_SUCCESS = 2;  //注册成功
    private final int SIGN_UP_FAILED = 3; // 注册失败

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case GET_TOKEN_SUCCESS:
                    Toast.makeText(getApplicationContext(), "验证码发送成功！", Toast.LENGTH_LONG).show();
                    break;
                case GET_TOKEN_FAILED:
                    Toast.makeText(getApplicationContext(), "验证码发送失败！", Toast.LENGTH_LONG).show();
                    break;
                case SIGN_UP_SUCCESS:
                    Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_LONG).show();
                    break;
                case SIGN_UP_FAILED:
                    Toast.makeText(getApplicationContext(), "注册失败！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void initView() {
        emailInput = findViewById(R.id.et_register_username);
        et_register_pwd_input = findViewById(R.id.et_register_pwd_input);
        et_register_auth_code=findViewById(R.id.et_register_auth_code);
        getTokenText = findViewById(R.id.tv_register_sms_call);
        getTokenText.setOnClickListener(this);
        bt_register_submit = findViewById(R.id.bt_register_submit);
        bt_register_submit.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 注册第一步
        setContentView(R.layout.activity_main_register_step_one);
        // 初始化控件和注册事件
        initView();
        findViewById(R.id.ib_navigation_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.bt_register_submit:
//                final String token = "6fcbd2";
//                final String email = "1024121845@qq.com";
//                final String pwd = "123";
                final String token = et_register_auth_code.getText().toString();
                final String email =  emailInput.getText().toString();
                final String pwd =  et_register_pwd_input.getText().toString();
                // 先判断字段是否填写好
                if (email.equals("") || pwd.equals("") || token.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "请填写字段！", Toast.LENGTH_LONG);
                    toast.show();
                }
                // 注册逻辑, 发送验证、邮箱、密码到服务器
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int result = TokenUtil.tokenVerificate(email, token, pwd);
                        //从全局池中返回一个message实例，避免多次创建message（如new Message）
                        Message msg = Message.obtain();
                        msg.what = (result == 0) ? SIGN_UP_SUCCESS : SIGN_UP_FAILED;   //标志消息的标志
                        handler.sendMessage(msg);
                    }
                }).start();
                break;
            case R.id.tv_register_sms_call: // 获取验证码
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int resultCode = TokenUtil.getToken(emailInput.getText().toString());
                        //从全局池中返回一个message实例，避免多次创建message（如new Message）
                        Message msg = Message.obtain();
                        msg.what = (resultCode == 0) ? GET_TOKEN_SUCCESS : GET_TOKEN_FAILED;   //标志消息的标志
                        handler.sendMessage(msg);
                    }
                }).start();
                break;
        }
    }
}