package com.whut.getianao.walking_the_world_android.activity;

import android.os.Bundle;
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

import static com.baidu.mapapi.BMapManager.getContext;

public class AddFriendActivity extends AppCompatActivity{
    private QMUIGroupListView mGroupListView;
    private ImageView imageView_search;
    private EditText friends_add_search_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_add);
        initView();
    }
    private void initView() {
        imageView_search=findViewById(R.id.friends_add_search_img);
        mGroupListView=findViewById(R.id.groupListView_add_friends);
        imageView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=friends_add_search_text.getText().toString().trim();
            }
        });
    }
    private void PersonList(String username){
        //getPersonList
        initGroupListView();

    }
    private void initGroupListView() {


        QMUICommonListItemView itemWithDetail = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.friends_selected),
                "Item 2",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithDetail.setDetailText("在右方的详细信息");



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
