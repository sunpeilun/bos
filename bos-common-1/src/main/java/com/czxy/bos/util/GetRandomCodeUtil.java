package com.czxy.bos.util;

import java.util.Random;

public class GetRandomCodeUtil {

    public static String getCode(){
        String code = "";
        Random random = new Random();
        for(int i=0;i<6;i++){
            int j = random.nextInt(10);
            code += j;
        }
        return code;
    }



}
