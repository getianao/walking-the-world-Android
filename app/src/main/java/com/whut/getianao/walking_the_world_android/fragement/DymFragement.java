package com.whut.getianao.walking_the_world_android.fragement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.whut.getianao.walking_the_world_android.R;
import com.whut.getianao.walking_the_world_android.data.Dym;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dym, container, false);
        lv = view.findViewById(R.id.list_view);

        getDymData();

        //2.创建自定义适配器
        mba = new MyBaseAdapt((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        //3.为listView设置适配器
        lv.setAdapter(mba);
        return view;

    }

    //初始化动态信息
    private void getDymData() {
        data=new ArrayList<>();
        for(int i=0;i<10;i++){
            Dym dym=new Dym("明天去月球","同志们，冲压冲压冲压冲压冲压冲压冲压冲压冲压冲压冲压");
            data.add(dym);
        }
    }

    public class MyBaseAdapt extends BaseAdapter {
        public class ViewHolder {
            ImageView iv_img;
            TextView tv_title;
            TextView tv_location;
            TextView tv_detail;
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
                vh.tv_title = v.findViewById(R.id.lv_title);
                vh.tv_location = v.findViewById(R.id.lv_location);
                vh.tv_detail = v.findViewById(R.id.lv_detail);
                v.setTag(vh);
            }

            ViewHolder vh = (ViewHolder) v.getTag();
            Dym dym = data.get(i);
            //vh.iv_img.setImageURI(new Uri(dym.getImgUrls()) );
            vh.tv_title.setText(dym.getTitle());
            //vh.tv_location.setText(dym.getLocation());
            vh.tv_detail.setText(dym.getText());
            return v;
        }
    }


}
