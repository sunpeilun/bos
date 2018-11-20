package com.czxy.bos.baidumap;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class LBSYunTest01 {
    /**
     * 调用百度LBS云地理编码解析
     *
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {

        //1 创建浏览器HttpClient对象
        CloseableHttpClient client = HttpClients.createDefault();
        //2 创建请求方式
        //geotable_id :服务器上的   ：存储自定义数据时生成的数据表ID
        //address：必须
        //city：城市名
        //ak：必须
        String url = "http://api.map.baidu.com/cloudgc/v1?address=河北省张家口市尚义县&ak=nOXZem5adYQklK59hCYtECe3DbkkRWh5";
        HttpGet get = new HttpGet(url);
        //3 请求+接受响应结果
        CloseableHttpResponse response = client.execute(get);
        //4 解析结果
        int code = response.getStatusLine().getStatusCode();
        System.out.println(code);

        HttpEntity entity = response.getEntity();
        //如果直接打印entity，打印的是内存地址；所以需要通过EntityUtils.toString方法，将对象中的属性的值转成json类型
        String s = EntityUtils.toString(entity);
        System.out.println(EntityUtils.toString(entity));

        //5 关闭资源
        response.close();
        client.close();

    }

}
