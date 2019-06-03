package com.whut.getianao.walking_the_world_android.activity;

import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whut.getianao.walking_the_world_android.R;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;

public class HomePageActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private NavigationTabBar navigationTabBar;

    private View view1, view2, view3, view4;
    private List<View> viewList;//view数组

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
        LayoutInflater inflater = getLayoutInflater();
        view1 = inflater.inflate(R.layout.fragment01, null);
        view2 = inflater.inflate(R.layout.fragment02, null);
        view3 = inflater.inflate(R.layout.fragment03, null);
        view4 = inflater.inflate(R.layout.fragment04, null);
        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        });
    }

    private void initNavigationBar() {
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.location_selected),
                        Color.parseColor("#87CEFA")//选中背景色
                ).title("Heart")
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.recording),
                        Color.parseColor("#87CEFA")
                ).title("Cup")
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.friends),
                        Color.parseColor("#87CEFA")
                ).title("Diploma")
                        .badgeTitle("state")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.mipmap.mine),
                        Color.parseColor("#87CEFA")
                ).title("Flag")
                        .badgeTitle("icon")
                        .build()
        );
//        models.add(
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.ic_fifth),
//                        Color.parseColor("#000000")
//                ).title("Medal")
//                        .badgeTitle("777")
//                        .build()
//        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);

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
