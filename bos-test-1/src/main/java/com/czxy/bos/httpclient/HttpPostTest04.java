package com.czxy.bos.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class HttpPostTest04 {

    public static void main(String[] args) throws  Exception{
        // 创建浏览器HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建HttpPost请求方式，并且传递参数
        HttpPost post = new HttpPost("https://www.oschina.net/search");
        // 把自己伪装成浏览器。否则开源中国会拦截访问
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

        // 准备参数
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("scope","project"));
        list.add(new BasicNameValuePair("q","servlet"));
        list.add(new BasicNameValuePair("fromerr","iZ68ErWk"));

        // 将参数封装成表单对象
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(list);
        // 将参数给post请求
        post.setEntity(formEntity);

        // 发送请求 + 获取响应结果
        CloseableHttpResponse response = httpClient.execute(post);
        // 打印结果
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);

        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));

        // 关闭请求
        response.close();
        httpClient.close();






    }



}
