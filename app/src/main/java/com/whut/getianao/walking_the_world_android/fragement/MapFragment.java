package com.whut.getianao.walking_the_world_android.fragement;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.activity.AddNewsActivity;
import com.whut.getianao.walking_the_world_android.data.SearchLocation;

import java.util.ArrayList;
import java.util.List;



public class MapFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private View mapView;
    private TextView tvTitle;   // map_item.xml里的标题TextView
    private TextView tvAddress; // map_item.xml里的地址TextView
    private ListView mListView; // activity_map中的listView
    private BaseAdapter adapter;//要实现的类
    //    // private List<SearchLocation> locationsList = SearchLocation.getFakeDatas();  // 实体类，假数据
//    private List<SearchLocation> locationsList = null;
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private Button mLocateBtn, mSearchBtn;
    private EditText mSearchText;
    private boolean isFirst = true; // 控制定位的btn，是否第一次定位
    // 定位客户
    private LocationClient mLocationClient = null;


    private void initView() {
        mMapView =  mapView.findViewById(R.id.baiduMapView);
        mSearchText =  mapView.findViewById(R.id.editTextSearch);
        mSearchBtn =  mapView.findViewById(R.id.buttonSearch);
        mLocateBtn =  mapView.findViewById(R.id.buttonLocate);
        mSearchBtn.setOnClickListener(this);
        mLocateBtn.setOnClickListener(this);

        mListView =  mapView.findViewById(R.id.lv_location_nearby);
        mListView.setOnItemClickListener(this);


    }

    private void initMap() {
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true); // 开启定位
        MyLocationListener myLocationListener = new MyLocationListener();
        //定位初始化
        mLocationClient = new LocationClient(getContext());//声明LocationClient类
        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        //设置locationClientOption
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mapView = inflater.inflate(R.layout.activity_map, container, false);

        mMapView = mapView.findViewById(R.id.baiduMapView);
        initView();
        // allowLocationPermission();// 询问权限
        initMap();  // 初始化信息
        return mapView;
    }


    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

//    private void allowLocationPermission() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!Settings.System.canWrite(this)) {
//                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
//                        Uri.parse("package:" + getPackageName()));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivityForResult(intent, 101);
//            } else {
//                //有了权限，你要做什么呢？具体的动作
//            }
//
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLocate:
                isFirst = true;// 一次性定位置
                break;
            case R.id.buttonSearch:
                // mSearchText.setFocusable(false);  // 失去焦点
                String searchPlace = mSearchText.getText().toString();
                PoiSearch mPoiSearch = PoiSearch.newInstance();
                OnGetPoiSearchResultListener poiListener = new MyGetPoiSearchResultListener();
                mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
                // 检索
                mPoiSearch.searchInCity((new PoiCitySearchOption())
                        .city("武汉") //必填
                        .keyword(searchPlace) //必填
                        .cityLimit(false)
                        .pageNum(0));  // 选第一页（默认是10条）
                mPoiSearch.destroy();
                break;
        }

    }

    // Listiew点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView titleStr = view.findViewById(R.id.textViewAddress);  // 根据pos得到地址信息
        // 跳转到发布动态页面
        Intent intent = new Intent(getContext(),AddNewsActivity.class);
        intent.putExtra("location",titleStr.toString());
        startActivity(intent);

        // String str = locationsList.get(position).getTitle(); // 根据pos得到数据
        // Toast.makeText(getActivity(), "" + titleStr.getText().toString(), Toast.LENGTH_SHORT).show();//显示数据
    }


    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
          //  System.out.println(location.getLocType());
            if (isFirst) {
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(0)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(location.getDirection()).latitude(location.getLatitude())
                        .longitude(location.getLongitude()).build();
                //缩放一下
                MapStatus.Builder builder = new MapStatus.Builder();

                builder.target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(18.0f);
                //mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                System.out.println(location.getAddrStr());
                mBaiduMap.setMyLocationData(locData);
                isFirst = !isFirst;
            }


        }
    }

    private class MyGetPoiSearchResultListener implements OnGetPoiSearchResultListener {
        @Override
        public void onGetPoiResult(PoiResult result) {
            //获取POI检索结果
            if (result == null
                    || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                Toast.makeText(getActivity(), "未找到结果",
                        Toast.LENGTH_LONG).show();
                return;
            } else if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                {
                    List<PoiInfo> allPoi = result.getAllPoi();
                    // 包装数据成SearchLocation数据
                    List<SearchLocation> locList = new ArrayList<>(10);
                    int showNum = allPoi.size() >= 10 ? 10 : allPoi.size();
                    for (int i = 0; i <showNum; i++) {
                        SearchLocation temp = new SearchLocation(allPoi.get(i).name, allPoi.get(i).address);
                        locList.add(temp);
                    }
                    // 放到listView上
                    adapter = new MyAdapter(locList);
                    mListView.setAdapter(adapter);

                    return;
                }
            }
        }

        @Override
        public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

        }

        @Override
        public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {

        }

        @Override
        public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

        }
    }

    private class MyAdapter extends BaseAdapter {
        private List<SearchLocation> locationList;

        public MyAdapter(List<SearchLocation> locationsList)
        {
            this.locationList=locationsList;
        }
        @Override
        public int getCount() {
            if (locationList == null)
                return 0;
            return locationList.size();  // 数目
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View view;
            if (convertView == null) {
                // 因为getView()返回的对象，adapter会自动赋给ListView
                view = inflater.inflate(R.layout.map_item, null);
            } else {
                view = convertView;
                //  Log.i("info", "有缓存，不需要重新生成" + position);
            }
            tvTitle = view.findViewById(R.id.textViewTitle);
            tvTitle.setText(this.locationList.get(position).getTitle());//设置参数

            tvAddress =  view.findViewById(R.id.textViewAddress);
            tvAddress.setText(this.locationList.get(position).getAddress());//设置参数
            return view;

        }
    }
}
