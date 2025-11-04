package cn.com.riven.starter.log.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(GlobalDefaultProperties.PREFIX)
public class GlobalDefaultProperties {

    public static final String PREFIX = "log.dispose";

    private Integer errorMsgShowMaxCount = 5;

    /**
     * 统一返回过滤包
     */
    private List<String> adviceFilterPackage = new ArrayList<>();

    /**
     * 统一返回过滤类
     */
    private List<String> adviceFilterClass = new ArrayList<>();

    public List<String> getAdviceFilterPackage() {
        return adviceFilterPackage;
    }

    public void setAdviceFilterPackage(List<String> adviceFilterPackage) {
        this.adviceFilterPackage = adviceFilterPackage;
    }

    public List<String> getAdviceFilterClass() {
        return adviceFilterClass;
    }

    public void setAdviceFilterClass(List<String> adviceFilterClass) {
        this.adviceFilterClass = adviceFilterClass;
    }

    public static String getPREFIX() {
        return PREFIX;
    }

    public Integer getErrorMsgShowMaxCount() {
        return errorMsgShowMaxCount;
    }

    public void setErrorMsgShowMaxCount(Integer errorMsgShowMaxCount) {
        this.errorMsgShowMaxCount = errorMsgShowMaxCount;
    }
}