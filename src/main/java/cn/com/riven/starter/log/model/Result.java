package cn.com.riven.starter.log.model;

import cn.com.riven.starter.log.enums.CommonErrorCode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.io.Serializable;

/**
 * 返回统一数据结构
 */
public class Result<T> implements Serializable {


    /**
     * 服务器当前时间戳
     */
    private Long ts = System.currentTimeMillis();

    /**
     * 成功数据
     */
    private T data;

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误描述
     */
    private String msg;


    private static final String Success = "1";



    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Result() {

    }

    public Result( Long ts, T data, String code, String msg) {
        this.ts = ts;
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public static Result<Void> ofSuccess() {
        Result<Void> result = new Result<>();
        result.setCode(Success);
        return result;
    }

    public static <T> Result<T> ofSuccess(T data) {
        Result<T> result = new Result<>();
        result.setData(data);
        result.setCode(Success);
        return result;
    }

    public static <T> Result<T> ofFail(String code, String msg) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        return result;
    }

    public static <T> Result<T> ofFail(String code, String msg, T data) {
        Result<T> result = new Result<>();
        result.code = code;
        result.msg = msg;
        result.setData(data);
        return result;
    }

    public static Result<String> ofFail(CommonErrorCode resultEnum) {
        Result<String> result = new Result<>();
        result.code = resultEnum.getCode();
        result.msg = resultEnum.getMessage();
        return result;
    }

    /**
     * 获取 json
     * @return json
     */
    public String buildResultJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", this.code);
        jsonObject.put("ts", this.ts);
        jsonObject.put("msg", this.msg);
        jsonObject.put("data", this.data);
        return JSON.toJSONString(jsonObject, SerializerFeature.DisableCircularReferenceDetect);
    }

    @Override
    public String toString() {
        return "Result{" +
                ", ts=" + ts +
                ", data=" + data +
                ", code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}