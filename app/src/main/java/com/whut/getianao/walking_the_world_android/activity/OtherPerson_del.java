package com.whut.getianao.walking_the_world_android.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.whut.getianao.walking_the_world_android.MyApplication;
import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.data.User;
import com.whut.getianao.walking_the_world_android.utility.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.whut.getianao.walking_the_world_android.utility.UserUtil.getFriendInfo;

public class OtherPerson_del extends AppCompatActivity {
    private QMUIGroupListView mGroupListView;
    private User u;
    private Context _this = this;
    private ImageButton op_add;
    private int res = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case 0:
                    try {
                        res = new Integer(msg.getData().getString("result"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 1:
                    try {
                        JSONObject userJSON = new JSONObject(msg.getData().getString("user"));
                        u = UserUtil.trans(userJSON);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        mGroupListView = findViewById(R.id.acyivity_mine_listitem);
        inithead();
        initGroupListView();

    }


    public void Friend_del(final int friendsid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int res = UserUtil.deleteFriend(MyApplication.userId, friendsid);
                Bundle bundle = new Bundle();
                bundle.putString("result", String.valueOf(res));
                Message msg = handler.obtainMessage();//每发送一次都要重新获取
                msg.what = 0;
                msg.setData(bundle);
                handler.sendMessage(msg);//用handler向主线程发送信息
            }
        }).start();
    }

    public void getInfobyid() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject userJson = UserUtil.getFriendInfo(Integer.valueOf(getIntent().getStringExtra("id")));
                Bundle bundle = new Bundle();
                bundle.putString("user", userJson.toString());
                Message msg = handler.obtainMessage();//每发送一次都要重新获取
                msg.what = 1;
                msg.setData(bundle);
                handler.sendMessage(msg);//用handler向主线程发送信息
                handler.handleMessage(msg);
            }
        }).start();
    }

    private void inithead() {
        /**
         * 获取userId
         */
        getInfobyid();
        while (u == null) {
            ;
        }
        ImageView blurImageView = findViewById(R.id.h_back);
        op_add = findViewById(R.id.mine_bar_del);
        op_add.setVisibility(View.VISIBLE);
        op_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new QMUIDialog.MessageDialogBuilder(_this)
                        .setTitle("您确定删除此位用户吗？")
                        .setMessage("")
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                                Friend_del(u.getId());

                                if (res != -1) {
                                    Toast.makeText(_this, "删除成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(_this, "删除失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .create().show();
            }
        });
        Glide.with(this).load("http://"+MyApplication.SERVER_IP+":8080/"+u.getHeadUrl().replace("\r\n",""))
                .bitmapTransform(new BlurTransformation((this), 25), new CenterCrop(this))
                .into(blurImageView);
        ImageView avatarImageView = findViewById(R.id.h_head);
        Glide.with(this).load("http://"+MyApplication.SERVER_IP+":8080/"+u.getHeadUrl().replace("\r\n",""))
                .bitmapTransform(new CropCircleTransformation(this))
                .into(avatarImageView);

        TextView username = findViewById(R.id.user_name);
        username.setText(u.getName());
        TextView user_email =findViewById(R.id.user_email);
        user_email.setText(u.getEmail());
//        TextView user_age = findViewById(R.id.user_age);
//        user_email.setText(u.getAge());
//        TextView user_company = view.findViewById(R.id.user_company);
//        user_email.setText(u.getCompany());
//        TextView user_bornPlace = view.findViewById(R.id.user_bornPlace());
//        user_email.setText(u.getBornPlace());
    }


    private void initGroupListView() {


        QMUICommonListItemView itemWithDetail = mGroupListView.createItemView(
                ContextCompat.getDrawable(this, R.mipmap.friends_selected),
                "年龄",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithDetail.setDetailText(String.valueOf(u.getAge()));

        QMUICommonListItemView itemWithDetai2 = mGroupListView.createItemView(
                ContextCompat.getDrawable(this, R.mipmap.friends_selected),
                "公司",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithDetai2.setDetailText(String.valueOf(u.getCompany()));

        QMUICommonListItemView itemWithDetai3 = mGroupListView.createItemView(
                ContextCompat.getDrawable(this, R.mipmap.friends_selected),
                "出生地",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithDetai3.setDetailText(String.valueOf(u.getBornPlace()));


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof QMUICommonListItemView) {

//                    CharSequence text = ((QMUICommonListItemView) v).getText();
//                    Toast.makeText(getActivity(), text + " is Clicked", Toast.LENGTH_SHORT).show();
//
//                    showEditTextDialog((QMUICommonListItemView) v);
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(this, 20);
        QMUIGroupListView.newSection(this)
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemWithDetail, onClickListener)
                .addItemView(itemWithDetai2, onClickListener)
                .addItemView(itemWithDetai3, onClickListener)
                .addTo(mGroupListView);

//        QMUIGroupListView.newSection(getContext())
//                .setTitle("Section 2: 自定义右侧 View")
//                .addItemView(itemWithCustom, onClickListener)
//                .addTo(mGroupListView);
    }

    private void showEditTextDialog(final QMUICommonListItemView view) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(_this);
        CharSequence text = (view).getText();
        builder.setTitle(text + "")
                .setPlaceholder("在此输入您新的" + text)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            view.setDetailText(text);
                            //修改用户信息
                            Toast.makeText(_this, "您的昵称: " + text, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(_this, "请填入新的" + text, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create().show();
    }

}
