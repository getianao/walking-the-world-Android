package com.whut.getianao.walking_the_world_android.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuView;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.whut.getianao.walking_the_world_android.R;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

import static com.baidu.mapapi.BMapManager.getContext;

public class MineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        ImageView blurImageView = findViewById(R.id.h_back);
        Glide.with(this).load(R.mipmap.head)
                .bitmapTransform(new BlurTransformation(this, 25), new CenterCrop(this))
                .into(blurImageView);

        ImageView avatarImageView = findViewById(R.id.h_head);
        Glide.with(this).load(R.mipmap.head)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(avatarImageView);


    }
}
