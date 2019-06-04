package com.whut.getianao.walking_the_world_android.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.whut.getianao.walking_the_world_android.MyApplication;
import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.utility.ActivityUtil;
import com.whut.getianao.walking_the_world_android.utility.UploadUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AddNewsActivity extends Activity {


    private int CAMERA_RESULT_CODE = 3537;//相机请求码
    private int REQUEST_CODE_PICK_IMAGE = 3538;//相册请求码
    private String imgName;
    private Uri uri;
    private String path;
    private Context _this = this;

    private EditText tv_title, tv_detail;
    private TextView tv_location;
    private ImageView im_addPic;
    private Button sendBtn;

    String loc; // location
    private final int ADD_ACTIVITY_SUCCESS = 1;
    private final int ADD_ACTIVITY_FAILED = 2;
    private final int UPLOAD_IMAGE_SUCCESS = 3;
    private final int UPLOAD_IMAGE_FAILED = 4;
    private Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_news);

        handler = new MyHandler();

        //获取Map Fragment传过来的值
        Intent intent = getIntent();
        loc = intent.getStringExtra("location");
        if (loc != null) {
            // 设置location
            tv_location = findViewById(R.id.sendNews_tv_location);
            tv_location.setText(loc);
        }
        tv_title = findViewById(R.id.sendNews_et_title);
        tv_detail = findViewById(R.id.sendNews_et_detail);
        im_addPic = findViewById(R.id.sendNews_iv_new_pic);

        sendBtn = findViewById(R.id.buttonSend);


        initAddpic();
    }

    private void initAddpic() {
        im_addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimpleBottomSheetList();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject upLoadFileResult = null;
                            // 假如上传了图片，就先把图片上传
                            if (path != null) {
                                upLoadFileResult = UploadUtil.uploadFile(new File(path));  // 上传文件
                                Message msg = Message.obtain();
                                msg.what = (upLoadFileResult.getInt("status") == 0) ? UPLOAD_IMAGE_SUCCESS : UPLOAD_IMAGE_FAILED;   //标志消息的标志
                                handler.sendMessage(msg);
                            }
                            Map<String, Object> newsData = new HashMap<>();
                            newsData.put("userId", MyApplication.userId);
                            newsData.put("title", tv_title.getText().toString());
                            newsData.put("text", tv_detail.getText().toString());
                            newsData.put("location", tv_location.getText().toString());
                            String imgUrls = (path == null) ? "" : upLoadFileResult.getString("res");
                            newsData.put("imgUrls",imgUrls );  // 只能上传一张图片

                            int result = -1;
                            result = ActivityUtil.addActivity(newsData);  // 上传一个动态到服务器
                            //从全局池中返回一个message实例，避免多次创建message（如new Message）
                            Message msg2 = Message.obtain();
                            msg2.what = (result == 0) ? ADD_ACTIVITY_SUCCESS : ADD_ACTIVITY_FAILED;   //标志消息的标志
                            handler.sendMessage(msg2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }


    private void showSimpleBottomSheetList() {
        new QMUIBottomSheet.BottomListSheetBuilder(this)
                .addItem("拍照")
                .addItem("相册")
                .setOnSheetItemClickListener(new QMUIBottomSheet.BottomListSheetBuilder.OnSheetItemClickListener() {
                    @Override
                    public void onClick(QMUIBottomSheet dialog, View itemView, int position, String tag) {
                        dialog.dismiss();
                        Toast.makeText(_this, "Item " + (position + 1), Toast.LENGTH_SHORT).show();
                        switch (position) {
                            case 0: {
                                openSysCamera();
                                break;
                            }
                            case 1: {
                                choosePhoto();
                                break;
                            }
                        }

                    }
                })
                .build()
                .show();
    }

    //打开相机
    private void openSysCamera() {
        allow_permission();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imgName = "酷行天下" + System.currentTimeMillis();

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
                new File(Environment.getExternalStorageDirectory(), imgName)));
        startActivityForResult(cameraIntent, CAMERA_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_RESULT_CODE) {
            File tempFile = new File(Environment.getExternalStorageDirectory(), imgName);
            uri = Uri.fromFile(tempFile);
            im_addPic.setImageURI(uri);
        } else if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                try {
                    Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    path = cursor.getString(columnIndex);  //获取照片路径
                    cursor.close();
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    im_addPic.setImageBitmap(bitmap);
                } catch (Exception e) {
                    // TODO Auto-generatedcatch block
                    e.printStackTrace();
                }
            }

        }
    }

    void choosePhoto() {
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
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

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case ADD_ACTIVITY_SUCCESS:
                    Toast.makeText(getApplicationContext(), "新建动态成功！", Toast.LENGTH_LONG).show();
                    finish();  // 关闭当前页面
                    break;
                case ADD_ACTIVITY_FAILED:
                    Toast.makeText(getApplicationContext(), "新建动态失败！", Toast.LENGTH_LONG).show();
                    break;
                case UPLOAD_IMAGE_SUCCESS:
                    Toast.makeText(getApplicationContext(), "上传照片到服务器成功！", Toast.LENGTH_LONG).show();
                    break;
                case UPLOAD_IMAGE_FAILED:
                    Toast.makeText(getApplicationContext(), "上传照片到服务器是啊比！", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }
}
