package com.whut.getianao.walking_the_world_android.activity;

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
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.data.User;
import com.whut.getianao.walking_the_world_android.utility.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.baidu.mapapi.BMapManager.getContext;

public class AddFriendActivity extends AppCompatActivity{
    private QMUIGroupListView mGroupListView;
    private ImageView imageView_search;
    private EditText friends_add_search_text;
    private String username;
    private User u;
    private String userid;
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
                        Intent intent = new Intent(getContext(), OtherPerson_add.class);
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
        setContentView(R.layout.activity_friends_add);
        initView();
    }
    private void initView() {
        imageView_search=findViewById(R.id.friends_search);
        friends_add_search_text=findViewById(R.id.friends_add_search_text);
        mGroupListView=findViewById(R.id.groupListView_add_friends);
        // 加好友
        imageView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username=friends_add_search_text.getText().toString().trim();
                initGroupListView();
            }
        });
    }

    private void initGroupListView() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject userJson = UserUtil.queryUser(AddFriendActivity.this.username);
                Bundle bundle = new Bundle();
                bundle.putString("user", userJson.toString());
                Message msg = handler.obtainMessage();//每发送一次都要重新获取
                msg.what = 0;
                msg.setData(bundle);
                handler.sendMessage(msg);//用handler向主线程发送信息
            }
        }).start();


//        View.OnClickListener onClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (v instanceof QMUICommonListItemView) {
//                    CharSequence text = ((QMUICommonListItemView) v).getText();
//                    Toast.makeText(getActivity(), text + " is Clicked", Toast.LENGTH_SHORT).show();
//                }
//            }
//        };

//        int size = QMUIDisplayHelper.dp2px(getContext(), 20);
//        QMUIGroupListView.newSection(getContext())
//                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
//                .addItemView(itemWithDetail, onClickListener)
//                .addTo(mGroupListView);

//        QMUIGroupListView.newSection(getContext())
//                .setTitle("Section 2: 自定义右侧 View")
//                .addItemView(itemWithCustom, onClickListener)
//                .addTo(mGroupListView);
    }
}
