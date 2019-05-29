package com.whut.getianao.walking_the_world_android.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.utility.UploadUtil;

import org.w3c.dom.Text;

import java.io.File;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView resultTextView;
    private ImageView imageView;
    private Button selectBtn, uploadBtn;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;

    // 这里的这个URL是我服务器的jURL
    private static String requestURL = "http://10.120.174.62:8080/image/upload";
    // 图片目录
    private String picPath = null;
    // startResultActivity请求码
    static final int PICK_IMAGE_REQUEST = 1;

    private void init_widgets() {
        resultTextView = findViewById(R.id.resultTextView);
        imageView = findViewById(R.id.imageView);
        selectBtn = findViewById(R.id.selectBtn);
        selectBtn.setOnClickListener(this);
        uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        // 初始化组件
        init_widgets();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                    final File file = new File(picPath);
                    if (file != null) {
                        // 上传文件到服务器
                        int result = UploadUtil.uploadFile(file, requestURL);
                        resultTextView.setText(result);
                    }
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
                    Log.e("Path：",  path);
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
