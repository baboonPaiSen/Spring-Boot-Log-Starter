package cn.com.riven.starter.log.utils;

import com.alibaba.fastjson.JSON;

public class FastJsonUtil {

    public static String toJson(Object o){
        return JSON.toJSONString(o);
    }
}