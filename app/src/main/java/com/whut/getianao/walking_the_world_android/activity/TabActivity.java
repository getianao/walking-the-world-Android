package com.whut.getianao.walking_the_world_android.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.fragement.TabFragement01;
import com.whut.getianao.walking_the_world_android.fragement.TabFragement02;
import com.whut.getianao.walking_the_world_android.fragement.TabFragement03;

import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity  {
    private LinearLayout android_layout,ios_layout,win_layout;
    private ImageButton imageButton1, imageButton2, imageButton3;
    private ViewPager viewPager;
    private List<Fragment> mFragments = new ArrayList<>();


    private void init_widgets() {
        viewPager = findViewById(R.id.viewPager);
        // 底部3个layout
        android_layout = findViewById(R.id.android_layout);
        ios_layout = findViewById(R.id.ios_layout);
        win_layout = findViewById(R.id.win_layout);

        // 底部3个按钮
        imageButton1 = findViewById(R.id.imageButton1);
        imageButton2 = findViewById(R.id.imageButton2);
        imageButton3 = findViewById(R.id.imageButton3);

        // 3个fragement
        TabFragement01 tabFragement01 = new TabFragement01();
        TabFragement02 tabFragement02 = new TabFragement02();
        TabFragement03 tabFragement03 = new TabFragement03();
        mFragments.add(tabFragement01);
        mFragments.add(tabFragement02);
        mFragments.add(tabFragement03);
    }

    // 重置所有状态
    private void resetImageBtn() {
        imageButton1.setImageResource(R.drawable.android);
        imageButton2.setImageResource(R.drawable.apple);
        imageButton3.setImageResource(R.drawable.windows);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        // 初始化，获取到组件
        init_widgets();

        FragmentPagerAdapter mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public Fragment getItem(int i) {
                return mFragments.get(i);
            }
        };
        viewPager.setAdapter(mAdapter);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int curIndex;

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                // 先重置其他按钮状态
                resetImageBtn();
                switch (i) {
                    case 0:
                        imageButton1.setImageResource(R.drawable.like_selected);
                        break;
                    case 1:
                        imageButton2.setImageResource(R.drawable.star_selected);
                        break;
                    case 2:
                        imageButton3.setImageResource(R.drawable.up_selected);
                        break;
                }
                curIndex = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        viewPager.setCurrentItem(0);  // 默认选中第一个
    }

}
