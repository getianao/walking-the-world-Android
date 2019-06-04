package com.whut.getianao.walking_the_world_android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.whut.getianao.walking_the_world_android.MyApplication;
import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.data.User;
import com.whut.getianao.walking_the_world_android.utility.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;

public class FriendsSearchActivity extends AppCompatActivity {
    private QMUIGroupListView mGroupListView;
    private ImageView friends_search_search_img;
    private EditText friends_search_search_text;
    private User u;
    private String userid;
    private String username;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case 0:
                    try {
                        JSONObject userJSON = new JSONObject(msg.getData().getString("user"));
                        u = UserUtil.trans(userJSON);
                        handleGetList();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
        }
    };
    private void handleGetList() {
        if (u!=null) {
            QMUICommonListItemView item = mGroupListView.createItemView(
                    ContextCompat.getDrawable(getContext(), R.mipmap.friends_selected),
                    "name",
                    null,
                    QMUICommonListItemView.HORIZONTAL,
                    QMUICommonListItemView.ACCESSORY_TYPE_NONE);
            item.setDetailText(u.getName());

                View.OnClickListener onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v instanceof QMUICommonListItemView) {
                            Intent intent = new Intent(getContext(), OtherPerson_del.class);
                            intent.putExtra("id", userid);
                            startActivity(intent);
                        }
                    }
                };
                int size = QMUIDisplayHelper.dp2px(getContext(), 20);
                QMUIGroupListView.newSection(getContext())
                        .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                        .addItemView(item, onClickListener)
                        .addTo(mGroupListView);
            }
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_search);
        initView();
    }
    private void initView() {
        friends_search_search_img=findViewById(R.id.friends_search_search_img);
        mGroupListView=findViewById(R.id.groupListView_add_friends);
        friends_search_search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username=friends_search_search_text.getText().toString().trim();
                initGroupListView();
            }
        });
    }

    private void initGroupListView() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject userJson = UserUtil.queryUser(FriendsSearchActivity.this.username);
                Bundle bundle = new Bundle();
                bundle.putString("user", userJson.toString());
                Message msg = handler.obtainMessage();//每发送一次都要重新获取
                msg.what = 0;
                msg.setData(bundle);
                handler.sendMessage(msg);//用handler向主线程发送信息
            }
        }).start();

//        QMUIGroupListView.newSection(getContext())
//                .setTitle("Section 2: 自定义右侧 View")
//                .addItemView(itemWithCustom, onClickListener)
//                .addTo(mGroupListView);
    }
}
