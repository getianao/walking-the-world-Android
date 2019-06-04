package com.whut.getianao.walking_the_world_android.fragement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.whut.getianao.walking_the_world_android.MyApplication;
import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.activity.AddFriendActivity;
import com.whut.getianao.walking_the_world_android.activity.FriendActivity;
import com.whut.getianao.walking_the_world_android.activity.FriendsSearchActivity;
import com.whut.getianao.walking_the_world_android.activity.OtherPerson_del;
import com.whut.getianao.walking_the_world_android.data.User;
import com.whut.getianao.walking_the_world_android.utility.UserUtil;

import java.util.List;

public class FriendsFragment extends Fragment {
    private QMUIGroupListView mGroupListView;
    private View view;
    private ImageView imageViewadd;
    private ImageView imageViewsearch;
    private String userid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_friends, container, false);

        mGroupListView=view.findViewById(R.id.groupListView_friends);
        initGroupListView();
        initView();
        return view;
    }
    private void initView(){
        imageViewadd=view.findViewById(R.id.friends_add);
        imageViewsearch=view.findViewById(R.id.friends_search);
        imageViewadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddFriendActivity.class);
                startActivity(intent);
            }
        });
        imageViewsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FriendsSearchActivity.class);
                startActivity(intent);
            }
        });

    }
    private void initGroupListView() {
        List<User> list=UserUtil.JSON2User(UserUtil.allFriends(MyApplication.userId));
        QMUICommonListItemView[] items=new QMUICommonListItemView[list.size()];
        for (int i=0;i<list.size();i++) {
            User user = list.get(i);
            items[i]=mGroupListView.createItemView(
                    ContextCompat.getDrawable(getContext(), R.mipmap.friends_selected),
                    "name",
                    null,
                    QMUICommonListItemView.HORIZONTAL,
                    QMUICommonListItemView.ACCESSORY_TYPE_NONE);
            items[i].setDetailText(user.getName());
            userid=String.valueOf(user.getId());
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v instanceof QMUICommonListItemView) {
                        Intent intent = new Intent(getContext(), OtherPerson_del.class);
                        intent.putExtra("id",userid);
                        startActivity(intent);
                    }
                }
            };
            int size = QMUIDisplayHelper.dp2px(getContext(), 20);
            QMUIGroupListView.newSection(getContext())
                    .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                    .addItemView(items[i], onClickListener)
                    .addTo(mGroupListView);
        }



//        QMUIGroupListView.newSection(getContext())
//                .setTitle("Section 2: 自定义右侧 View")
//                .addItemView(itemWithCustom, onClickListener)
//                .addTo(mGroupListView);
    }
}

