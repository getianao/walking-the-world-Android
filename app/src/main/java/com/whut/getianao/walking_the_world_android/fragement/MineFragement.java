package com.whut.getianao.walking_the_world_android.fragement;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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

public class MineFragement extends Fragment {
    private QMUIGroupListView mGroupListView;
    private View view;
    private User u;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case 0:
                    try {
                        JSONObject userJSON = new JSONObject(msg.getData().getString("user"));
                        u = UserUtil.trans(userJSON);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_mine, container, false);

       mGroupListView=view.findViewById(R.id.acyivity_mine_listitem);
        try {
            inithead();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initGroupListView();

        return view;
    }
    public void getInfobyid() throws JSONException {
        Bundle bundle =this.getArguments();//得到从Activity传来的数据
        JSONObject userJSON = new JSONObject(bundle.getString("user"));
        u = UserUtil.trans(userJSON);
    }

    private void inithead() throws JSONException {
        /**
         * 获取userId
         */
        getInfobyid();
        ImageView blurImageView = view.findViewById(R.id.h_back);


        Glide.with(this).load( R.mipmap.head)
                .bitmapTransform(new BlurTransformation(getContext(), 25), new CenterCrop(getContext()))
                .into(blurImageView);
        ImageView avatarImageView = view.findViewById(R.id.h_head);
        Glide.with(this).load( R.mipmap.head)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(avatarImageView);
//
        TextView username = view.findViewById(R.id.user_name);
        username.setText(u.getName());
        TextView user_email = view.findViewById(R.id.user_email);
        user_email.setText(u.getEmail());
//        TextView user_age = view.findViewById(R.id.user_age);
//        user_email.setText(u.getAge());
//        TextView user_company = view.findViewById(R.id.user_company);
//        user_email.setText(u.getCompany());
//        TextView user_bornPlace = view.findViewById(R.id.user_bornPlace());
//        user_email.setText(u.getBornPlace());
    }


    private void initGroupListView() {


        QMUICommonListItemView itemWithDetail = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.friends_selected),
                "年龄",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithDetail.setDetailText(String.valueOf(u.getAge()));

        QMUICommonListItemView itemWithDetai2 = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.friends_selected),
                "公司",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE);
        itemWithDetai2.setDetailText(String.valueOf(u.getCompany()));

        QMUICommonListItemView itemWithDetai3 = mGroupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.mipmap.friends_selected),
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
                    showEditTextDialog((QMUICommonListItemView) v);
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(getContext(), 20);
        QMUIGroupListView.newSection(getContext())
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
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(getActivity());
        CharSequence text = (view).getText();
        builder.setTitle(text+"")
                .setPlaceholder("在此输入您新的"+text)
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
                            Toast.makeText(getActivity(), "您的昵称: " + text, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "请填入新的"+text, Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create().show();
    }
}
