package com.whut.getianao.walking_the_world_android.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.utility.ActivityUtil;
import com.whut.getianao.walking_the_world_android.utility.UploadUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView resultTextView;
    private ImageView imageView;
    private Button selectBtn, uploadBtn, newShareBtn;

    private EditText titleInput, contentInput;

    // 这里的这个URL是我服务器的jURL
    private static String requestURL = "http://10.120.174.62:8080/image/upload";
    // 图片目录
    private String picPath = null;
    // startResultActivity请求码
    static final int PICK_IMAGE_REQUEST = 1;

    private String picLocationInServer;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            int status = data.getInt("status");
            String location = data.getString("location");
            picLocationInServer = location;
            Log.i("status", "请求结果为-->" + status);
            Log.i("location", "图片保存路径为-->" + location);
            // TODO
            // UI界面的更新等相关操作
            // resultTextView.setText(String.valueOf(result));  // 不知道为什么不好使
        }
    };


    private void init_widgets() {
        resultTextView = findViewById(R.id.resultTextView);
        imageView = findViewById(R.id.imageView);

        selectBtn = findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(this);
        uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(this);
        newShareBtn = findViewById(R.id.newShareBtn);
        newShareBtn.setOnClickListener(this);

        titleInput = findViewById(R.id.editTextTitle);
        contentInput = findViewById(R.id.editTextContent);

    }

    private void allow_permission() {
        /**
         * 动态获取权限，Android 6.0 新特性，一些保护权限，除了要在AndroidManifest中声明权限，还要使用如下代码动态获取
         */
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        // 初始化组件
        init_widgets();
        // 申请权限
        allow_permission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newShareBtn:  // 发布动态按钮
                List<String> URLs = new ArrayList<>();
//                URLs.add(picLocationInServer);
                URLs.add("http://10.120.174.62:8080/image/get?name=84cac6fb40de4257b2ea22943d6b672c.jpg");
                final Map<String, Object> data = new HashMap<>();
//                data.put("title",titleInput.getText().toString());
//                data.put("content",contentInput.getText().toString());
                data.put("title", "123");
                data.put("content", "test");
                data.put("URLs", URLs);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ActivityUtil.addActivity(data);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.selectBtn:
                /* 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的 */
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);  // 选择图片
                this.startActivityForResult(intent, PICK_IMAGE_REQUEST);
                break;
            case R.id.uploadBtn:
                if (picPath == null) {
                    Toast.makeText(UploadActivity.this, "请选择图片！", Toast.LENGTH_LONG).show();
                } else {
                    // 上传文件到服务器
                    /**
                     * 网络操作相关的子线程
                     */
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO
                            // 在这里进行 http request.网络请求相关操作
                            File file = new File(picPath);
                            // 上传文件到服务器,返回的结果是一个json数据
                            JSONObject result = UploadUtil.uploadFile(file, requestURL);
                            Message msg = new Message();
                            Bundle data = new Bundle();
                            try {
                                data.putString("location", result.getString("res"));
                                data.putInt("status", result.getInt("status"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            msg.setData(data);
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            /* 当选择的图片不为空的话，在获取到图片的途径 */
            Uri uri = data.getData();
            Log.e("选择的图片路径：", "uri = " + uri);
            try {
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, projection, null, null, null);
                if (cursor != null) {
                    ContentResolver cr = this.getContentResolver();
                    int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(colunm_index);
                    Log.e("Path：", path);
                    /***
                     * * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
                     * * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
                     */
                    if (path.endsWith("jpg") || path.endsWith("png")) {
                        picPath = path;
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        imageView.setImageBitmap(bitmap);
                    } else {
                        alert();
                    }
                } else {
                    alert();
                }
            } catch (Exception e) {
            }
        }
    }

    private void alert() {
        Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("您选择的不是有效的图片")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        picPath = null;
                    }
                }).create();
        dialog.show();
    }
}
