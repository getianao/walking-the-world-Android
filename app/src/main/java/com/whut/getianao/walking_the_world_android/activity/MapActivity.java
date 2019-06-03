package com.whut.getianao.walking_the_world_android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;


import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.map.BaiduMap;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;

import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import com.baidu.mapapi.search.poi.PoiCitySearchOption;

import com.baidu.mapapi.search.poi.PoiSearch;
import com.whut.getianao.walking_the_world_android.R;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private Button mRequestLocation, mCompleteButton;
    private ListView mListView;

    private void initView() {
        mMapView = (MapView) findViewById(R.id.baiduMapView);
        mBaiduMap=mMapView.getMap();

        mCompleteButton = (Button) findViewById(R.id.chat_publish_complete_publish);
        mRequestLocation = (Button) findViewById(R.id.request);
        mCompleteButton.setOnClickListener(this);
        mRequestLocation.setOnClickListener(this);

        mListView = (ListView) findViewById(R.id.lv_location_nearby);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        initView();
        allowLocationPermission();// 询问权限
    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    private void allowLocationPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 101);
            } else {
                //有了权限，你要做什么呢？具体的动作
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.request:
//                PoiSearch mPoiSearch = PoiSearch.newInstance();
//                mPoiSearch.searchInCity(new PoiCitySearchOption()
//                        .city("北京") //必填
//                        .keyword("美食") //必填
//                        .pageNum(10));
//                mPoiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
//                    @Override
//                    public void onGetPoiResult(PoiResult poiResult) {
//
//                    }
//
//                    @Override
//                    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
//
//                    }
//
//                    @Override
//                    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
//
//                    }
//
//                    @Override
//                    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
//
//                    }
//                });
//
            case R.id.request:
                //定义Maker坐标点
                LatLng point = new LatLng(39.963175, 116.400244);
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_mark);
                //构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
                //在地图上添加Marker，并显示
                mBaiduMap.addOverlay(option);
                // 标记点击事件
                mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                    //marker被点击时回调的方法
                    //若响应点击事件，返回true，否则返回false
                    //默认返回false
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        System.out.println("标记被点击！");
                        PoiSearch mPoiSearch = PoiSearch.newInstance();
                        mPoiSearch.searchInCity(new PoiCitySearchOption()
                                .city("北京") //必填
                                .keyword("美食") //必填
                                .pageNum(0)
                                .pageCapacity(10));
                        // 新建一个btn
                        Button btn = new Button(getApplicationContext());
                        btn.setBackgroundColor(Color.RED);
                        btn.setText(marker.getTitle());

                        // btn 变成 View 图片
                        BitmapDescriptor descriptor = BitmapDescriptorFactory
                                .fromView(btn);

                        /**
                         * 弹窗的点击事件：
                         *  - InfoWindow 展示的bitmap position
                         *  - InfoWindow 显示的地理位置
                         *  - InfoWindow Y 轴偏移量 listener
                         *  - InfoWindow 点击监听者
                         *  InfoWindow 点击的时候 消失。
                         * */
                        InfoWindow infoWindow = new InfoWindow(descriptor, marker
                                .getPosition(), -60, new InfoWindow.OnInfoWindowClickListener() {

                            public void onInfoWindowClick() {
                                // TODO Auto-generated method stub
                                // 当用户点击 弹窗 触发：
                                // 开启 POI 检索、 开启 路径规矩, 跳转界面。

                                // 1 隐藏 弹窗。
                                mBaiduMap.hideInfoWindow();
                            }
                        });

                        // 2 show infoWindow
                        mBaiduMap.showInfoWindow(infoWindow);
                        return false;
                    }
                });
        }
    }


}
