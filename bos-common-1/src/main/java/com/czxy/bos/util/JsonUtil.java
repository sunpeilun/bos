package com.czxy.bos.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonUtil {

    /**
     * 查找json串
     *
     * {"province":"河北省","city":"张家口市","district":"尚义县","street":"","level":"区县"},
     *  start  +      ()    +   end
     *  district":"            ","street
     */
    public static List<String> findParamByRegix(String json, String start, String end){
        // 表达式         .* 单个字符匹配任意次     .*?   相同的情况只需要匹配一次，即最小匹配
        String regex = start + "\":\"(.*?)\",\"" + end;
        // 将json串和regex关联，进行匹配
        Matcher matcher = Pattern.compile(regex).matcher(json);
        // 最简单的
        List<String> list = new ArrayList<>();

        // 只要找到，就循环  从json中匹配regex
        while (matcher.find()){
            // matcher.group(0):district":"尚义县","street
            // matcher.group(1):尚义县
            String value = matcher.group(2);
            list.add(value);
        }

        return list;
    }

    public static void main(String[] args) {
        String json = "{\"status\":0,\"message\":\"ok\",\"result\":[{\"source\":\"baidu\",\"location\":{\"lat\":41.132634994489368,\"lng\":114.15252831523267},\"bound\":\"40.740820,113.832183;41.543819,114.456838\",\"formatted_address\":\"河北省张家口市尚义县\",\"address_components\":{\"province\":\"河北省\",\"city\":\"张家口市\",\"district\":\"尚义县\",\"street\":\"\",\"level\":\"区县\"},\"·\":0.14}]}";

        List<String> list = JsonUtil.findParamByRegix(json, "district", "street");

        System.out.println(list.get(0).toString());

    }



}
