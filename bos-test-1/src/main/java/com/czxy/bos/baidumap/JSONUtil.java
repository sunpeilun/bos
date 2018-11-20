package com.czxy.bos.baidumap;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONUtil {
    /**
     * 返回json字符串中对应的值
     * json json格式字符串
     * s 要取的json键
     * e 要取的json键的下一个键
     */
    public static ArrayList<String> getParamByRex(String json, String start, String end) {

        ArrayList<String> list = new ArrayList<>();
        String regex=start + "\":(.*?),\"" + end;//别忘了使用非贪婪模式！
        Matcher matcher=Pattern.compile(regex).matcher(json);
        while(matcher.find())
        {
            String ret=matcher.group(1);
            list.add(ret);
        }
        return list;
    }

    public static void main(String[] args) {
        String json="{\"status\":0,\"message\":\"ok\",\"result\":[{\"source\":\"baidu\",\"location\":{\"lat\":41.132634994489368,\"lng\":114.15252831523267},\"bound\":\"40.740820,113.832183;41.543819,114.456838\",\"formatted_address\":\"河北省张家口市尚义县\",\"address_components\":{\"province\":\"河北省\",\"city\":\"张家口市\",\"district\":\"尚义县\",\"street\":\"\",\"level\":\"区县\"},\"precise\":0.14}]}";
        ArrayList<String> list = JSONUtil.getParamByRex(json, "district", "street");
        System.out.println(list.get(0));

    }

}
