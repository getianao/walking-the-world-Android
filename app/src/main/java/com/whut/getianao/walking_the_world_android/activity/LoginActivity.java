package com.whut.getianao.walking_the_world_android.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.service.CallServer;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button getTokenBtn, RegisterBtn, LoginBtn;
    private EditText emailInput, pwdInput, tokenInput;
    private ImageView avatarView;

    // 子线程更新UI组件
    private Handler mHandler = new Handler();


    // 初始化组件
    private void init_wiegts() {
        getTokenBtn = findViewById(R.id.btnGetToken);
        RegisterBtn = findViewById(R.id.sign_up);
        LoginBtn = findViewById(R.id.sign_in);
        getTokenBtn.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
        LoginBtn.setOnClickListener(this);

        emailInput = findViewById(R.id.email_input);
        pwdInput = findViewById(R.id.pwd_input);
        tokenInput = findViewById(R.id.tokenInput);

        avatarView = findViewById(R.id.avatar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化组件
        init_wiegts();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 登录按钮点击
            case R.id.sign_in: {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String path = "http://10.120.174.62:8080/image/get?name=1.jpg";
                            URL url = new URL(path);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setConnectTimeout(5000);
                            conn.setRequestMethod("GET");
                            if (conn.getResponseCode() == 200) {
                                InputStream inputStream = conn.getInputStream();
                                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        // 通知UI线程更新组件
                                        avatarView.setImageBitmap(bitmap);
                                    }
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
            break;
            // 注册按钮点击
            case R.id.signup_btn: {
                // 先判断字段是否填写好
//                final String email = emailInput.getText().toString();
//                final String pwd = pwdInput.getText().toString();
                final String token = tokenInput.getText().toString();

                final String email = "929910266@qq.com";
                final String pwd = "123456";

                if (email.equals("") || pwd.equals("") || token.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "请填写字段！", Toast.LENGTH_LONG);
                    toast.show();
                }
                // 注册逻辑, 发送验证数据到服务器
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String temp;
                        StringBuilder sb = new StringBuilder();
                        try {
                            URL url = new URL(String.format(
                                    "http://10.120.174.62:8080/doReg.do?email=%s&code=%s&username=%s&password=%s", email, token, email, pwd));
                            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
                            while ((temp = in.readLine()) != null) {
                                sb.append(temp);
                            }
                            //  System.out.println(sb.toString());
                            in.close();
                            System.out.println(sb);
                            // 解析服务器返回的数据，从string转为JSON
                            JSONObject result = new JSONObject(sb.toString());
                            System.out.println(result.get("code"));
                        } catch (MalformedURLException me) {
                            System.err.println("你输入的URL格式有问题！");
                            me.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            }
            // 获取验证码按钮点击
            case R.id.btnGetToken: {
                final String email = "929910266@qq.com";
                String temp;
                StringBuilder sb = new StringBuilder();
                try {
                    URL url = new URL(String.format("http://10.120.174.62:8080/reg.do?email=%s", email));
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
                    while ((temp = in.readLine()) != null) {
                        sb.append(temp);
                    }
                    in.close();
                    // System.out.println(sb.toString());
                    System.out.println(sb);
                    // 解析服务器返回的数据，从string转为JSON
                    JSONObject result = new JSONObject(sb.toString());
                    System.out.println(result.get("code"));
                } catch (MalformedURLException me) {
                    System.err.println("你输入的URL格式有问题！");
                    me.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
        }
    }
}
