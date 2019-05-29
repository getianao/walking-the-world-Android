package com.whut.getianao.walking_the_world_android.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author: Administrator
 * @date: 2019/5/29 0029
 */
public class CallServer {
    /**
     * 加载网络图片，获取网络图片的bitmap
     */
    //加载图片
    public static Bitmap getBitmap(final String path) throws IOException {
        final Bitmap[] bitmap = {null};
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(5000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        InputStream inputStream = conn.getInputStream();
                        bitmap[0] = BitmapFactory.decodeStream(inputStream);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
        return bitmap[0];
    }

}
