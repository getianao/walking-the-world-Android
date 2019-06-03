package com.whut.getianao.walking_the_world_android.utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author: Administrator
 * @date: 2019/5/30 0030
 */
public class TokenUtil {
    /**
     * @param email： 请求的邮箱
     * @return 返回验证码： 0--成功  1--失败
     * @description 给指定邮箱发送验证码
     * @author Liu Zhian
     * @time 2019/5/30 0030 上午 10:15
     */
    public static int getToken(String email) {
        int resultCode = -1;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(String.format("http://10.120.174.62:8080/reg.do?email=%s", email));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            in.close();
            // System.out.println(sb.toString());
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            JSONObject result = new JSONObject(sb.toString());
            resultCode = (int) result.get("status");
            System.out.println(resultCode);
        } catch (MalformedURLException me) {
            System.err.println("你输入的URL格式有问题！");
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultCode;
    }

    /**
     * @param
     * @return 返回结果 0--成功  1--失败
     * @description 验证注册时的邮箱验证码、用户名。密码等信息
     * @author Liu Zhian
     * @time 2019/5/30 0030 上午 10:19
     */
    public static int tokenVerificate(String email, String token, String password) {
        int resultCode = -1;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            // 用户名和邮箱一样
            URL url = new URL(String.format(
                    "http://10.120.174.62:8080/doReg.do?email=%s&code=%s&username=%s&password=%s", email, token, email, password));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            //  System.out.println(sb.toString());
            in.close();
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            JSONObject result = new JSONObject(sb.toString());
            resultCode = (int) result.get("status");
            System.out.println(resultCode);
        } catch (MalformedURLException me) {
            System.err.println("你输入的URL格式有问题！");
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultCode;
    }

    /**
     * @description 登录验证
     * @param
     * @return 返回结果 0--成功  1--失败
     * @author Liu Zhian
     * @time 2019/5/30 0030 下午 7:57
     */

    public static int loginVerificate(String email,String password)
    {
        int resultCode = -1;
        String temp;
        StringBuilder sb = new StringBuilder();
        try {
            // 用户名和邮箱一样
            URL url = new URL(String.format(
                    "http://10.120.174.62:8080/login?email=%s&password=%s", email, password));
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));// 获取输入流
            while ((temp = in.readLine()) != null) {
                sb.append(temp);
            }
            //  System.out.println(sb.toString());
            in.close();
            System.out.println(sb);
            // 解析服务器返回的数据，从string转为JSON
            JSONObject result = new JSONObject(sb.toString());
            resultCode = (int) result.get("status");
            System.out.println(resultCode);
        } catch (MalformedURLException me) {
            System.err.println("你输入的URL格式有问题！");
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultCode;
    }
}
