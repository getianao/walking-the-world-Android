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
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIBottomSheet;
import com.whut.getianao.walking_the_world_android.R;

import java.io.File;

public class AddNewsActivity extends Activity {

    private int CAMERA_RESULT_CODE=3537;//相机请求码
    private int REQUEST_CODE_PICK_IMAGE=3538;//相册请求码
    private String imgName;
    private Uri uri;
    private String path;
    private Context _this=this;

    private TextView tv_title;
    private TextView tv_detail;
    private ImageView im_addPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_news);
        tv_title=findViewById(R.id.sendNews_et_title);
        tv_detail=findViewById(R.id.sendNews_et_detail);
        im_addPic=findViewById(R.id.sendNews_iv_new_pic);
        initAddpic();
    }

    private void initAddpic() {
        im_addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimpleBottomSheetList();
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
                        switch(position){
                            case 0:{
                                openSysCamera();
                                break;
                            }
                            case 1:{
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
        imgName="酷刑天下"+System.currentTimeMillis();

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(
                new File(Environment.getExternalStorageDirectory(), imgName)));
        startActivityForResult(cameraIntent, CAMERA_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_RESULT_CODE){
            File tempFile = new File(Environment.getExternalStorageDirectory(), imgName);
            uri = Uri.fromFile(tempFile);
            im_addPic.setImageURI(uri);
        }else if(requestCode==REQUEST_CODE_PICK_IMAGE){
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

    void choosePhoto(){
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
}
