package com.whut.getianao.walking_the_world_android.fragement;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.whut.getianao.walking_the_world_android.MyApplication;
import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.data.Dym;
import com.whut.getianao.walking_the_world_android.utility.ActivityUtil;
import com.whut.getianao.walking_the_world_android.utility.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * @author: Administrator
 * @date: 2019/5/28 0028
 */
public class DymFragement extends Fragment {

    private List<Dym> data;
    private MyBaseAdapt mba;//自定义适配器
    private ListView lv;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {      //判断标志位
                case 0:
                    try {
                        JSONObject dymJSON = new JSONObject(msg.getData().getString("dym"));
                        data = ActivityUtil.JSON2Activity(dymJSON);
                        handleList();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
    };

    private void handleList() {
        //2.创建自定义适配器
        mba = new MyBaseAdapt((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        //3.为listView设置适配器
        lv.setAdapter(mba);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dym, container, false);
        lv = view.findViewById(R.id.list_view);

        getDymData();


        return view;

    }

    //初始化动态信息
    private void getDymData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject dymJSON = ActivityUtil.getAllActivities(MyApplication.userId);

                Bundle bundle = new Bundle();
                bundle.putString("dym", dymJSON.toString());
                Message msg = handler.obtainMessage();//每发送一次都要重新获取
                msg.what = 0;
                msg.setData(bundle);
                handler.sendMessage(msg);//用handler向主线程发送信息
            }
        }).start();
    }


    public class MyBaseAdapt extends BaseAdapter {
        public class ViewHolder {
            ImageView iv_img;
            ImageView iv_zan;
            TextView tv_title;
            TextView tv_location;
            TextView tv_detail;
            TextView tv_dianzhanhsu;
        }

        private LayoutInflater inflater;

        public MyBaseAdapt(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            if (v == null) {
                v = inflater.inflate(R.layout.listview_item, null);
                ViewHolder vh = new ViewHolder();
                vh.iv_img = v.findViewById(R.id.lv_img);
                vh.iv_zan = v.findViewById(R.id.lv_dianzai);
                vh.tv_title = v.findViewById(R.id.lv_title);
                vh.tv_location = v.findViewById(R.id.lv_location);
                vh.tv_detail = v.findViewById(R.id.lv_detail);
                vh.tv_dianzhanhsu = v.findViewById(R.id.lv_dianzhanshu);
                v.setTag(vh);
            }

            final ViewHolder vh = (ViewHolder) v.getTag();
            final Dym dym = data.get(i);

            vh.iv_img.setImageResource(R.mipmap.python);
            vh.tv_title.setText(dym.getTitle());
            //vh.tv_location.setText(dym.getLocation());
            vh.tv_detail.setText(dym.getText());

            //点赞
            vh.iv_zan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),"已点赞",Toast.LENGTH_SHORT);
                    vh.tv_dianzhanhsu.setText(""+(Long.valueOf(dym.getLikeCount())+1));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ActivityUtil.thumbUp(dym.getId(), MyApplication.userId);
                        }
                    }).start();
                }
            });


            return v;
        }
    }


//    public void dymPic(final Dym dym) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Bitmap bmp = getURLimage(dym.getImgUrls());
//                Message msg = new Message();
//                msg.what = 0;
//                msg.obj = bmp;
//               // handle.sendMessage(msg);
//            }
//        }).start();
//    }
//
//    //加载图片
//    public Bitmap getURLimage(String url) {
//        Bitmap bmp = null;
//        try {
//            URL myurl = new URL(url);
//            // 获得连接
//            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
//            conn.setConnectTimeout(6000);//设置超时
//            conn.setDoInput(true);
//            conn.setUseCaches(false);//不缓存
//            conn.connect();
//            InputStream is = conn.getInputStream();//获得图片的数据流
//            bmp = BitmapFactory.decodeStream(is);
//            is.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bmp;
//    }

}
