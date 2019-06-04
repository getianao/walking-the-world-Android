package com.whut.getianao.walking_the_world_android.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.fragement.DymFragement;
import com.whut.getianao.walking_the_world_android.fragement.FriendsFragment;
import com.whut.getianao.walking_the_world_android.fragement.MapFragment;
import com.whut.getianao.walking_the_world_android.fragement.MineFragement;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;

public class HomePageActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private NavigationTabBar navigationTabBar;

    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_2);
        init();
    }

    private void init() {
        viewPager = findViewById(R.id.home_view_pager);
        navigationTabBar = findViewById(R.id.home_navi);

        initPageView();
        initNavigationBar();
    }


    private void initPageView() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new MapFragment());
        fragmentList.add(new DymFragement());
        fragmentList.add(new FriendsFragment());
      // fragmentList.add(new MineFragement());

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int arg0) {
                return fragmentList.get(arg0);//显示第几个页面
            }

            @Override
            public int getCount() {
                return fragmentList.size();//有几个页面
            }
        });
    }

    private void initNavigationBar() {
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.location_selected),
                        Color.parseColor("#87CEFA")//选中背景色
                ).title("地图")
//                        .badgeTitle("地图")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.recording),
                        Color.parseColor("#87CEFA")
                ).title("动态")
//                        .badgeTitle("动态")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.friends),
                        Color.parseColor("#87CEFA")
                ).title("好友")
//                        .badgeTitle("好友")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.mine),
                        Color.parseColor("#87CEFA")
                ).title("我")
//                        .badgeTitle("我")
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 2);

        navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
//        navigationTabBar.setBadgeGravity(NavigationTabBar.BadgeGravity.BOTTOM);
//        navigationTabBar.setBadgePosition(NavigationTabBar.BadgePosition.CENTER);
        navigationTabBar.setTypeface("fonts/custom_font.ttf");
        navigationTabBar.setIsBadged(false);
        navigationTabBar.setIsTitled(true);
        navigationTabBar.setIsTinted(true);
//        navigationTabBar.setIsBadgeUseTypeface(true);
//        navigationTabBar.setBadgeBgColor(Color.RED);
//        navigationTabBar.setBadgeTitleColor(Color.WHITE);
        navigationTabBar.setIsSwiped(true);
        navigationTabBar.setBgColor(getResources().getColor(R.color.lightgray));
//        navigationTabBar.setBadgeSize(10);
        navigationTabBar.setTitleSize(35);
        navigationTabBar.setIconSizeFraction((float) 0.5);
    }
}
