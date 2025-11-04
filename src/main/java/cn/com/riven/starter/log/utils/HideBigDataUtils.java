package cn.com.riven.starter.log.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @description: 隐藏对象中的json
 * @date: 2022/3/8 11:35
 * @author: Riven Ge
 */

public class HideBigDataUtils {


    public static void hideJSONBigData(JSON o) {

        if (o instanceof JSONObject) {
            JSONObject o1 = (JSONObject) o;
            Set<String> keySet = o1.keySet();
            for (String key : keySet) {
                Object o2 = o1.get(key);
                if (o2 instanceof  String){
                    String string = o1.getString(key);
                    if (JSON.isValidObject(string)) {
                        JSONObject jsonObject = JSON.parseObject(string);
                        hideJSONBigData(jsonObject);
                        o1.put(key, FastJsonUtil.toJson(jsonObject));
                    } else if (JSON.isValidArray(string)) {
                        JSONArray jsonArray = JSON.parseArray(string);
                        hideJSONBigData(jsonArray);
                        o1.put(key, FastJsonUtil.toJson(jsonArray));
                    } else if (!isJSONString(string) && string.length() > 2000) {
                        o1.put(key, "******bigData******");
                    }
                }else if (o2 instanceof JSON){
                    hideJSONBigData((JSON) o2);
                    o1.put(key,o2);
                }

            }
        } else if (o instanceof JSONArray) {
            JSONArray array = (JSONArray) o;
            for (int i = 0; i < array.size(); i++) {
                Object o1 = array.get(i);
                String s = String.valueOf(o1);
                if (isJSONString(s)) {
                    hideJSONBigData((JSON) array.get(i));
                }else if (o1 instanceof String){
                    String s1 = hideNormalString(s);
                    if (!Objects.equals(s1,s)){
                        array.set(i, s1);
                    }
                }
            }
        }

    }

    public static boolean isJSONString(String str) {
        boolean result;
        try {
            Object obj=JSON.parse(str);
            JSON test = (JSON)obj;
            result = true;
        } catch (Exception e) {
            result=false;
        }
        return result;
    }

    public static Object hideObject(Object o){

            String s = String.valueOf(o);
            // 如果是对象
            if (!(o instanceof String) && !StringUtils.isNumeric(s)){
                s = JSONObject.toJSONString(o);
            }
            if (isJSONString(s)){
                Object parse = JSON.parse(s);
                hideJSONBigData((JSON) parse);
                return parse;
            }
            else if (!StringUtils.isNumeric(s)){
                return hideNormalString(s);
            }else {
                return o;
            }
    }


    public static String hideNormalString(String s) {

        if (s.length() > 2000) {
            return "******bigData******";
        }
        return s;
    }


}