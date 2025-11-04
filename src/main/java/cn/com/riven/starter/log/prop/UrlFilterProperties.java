package cn.com.riven.starter.log.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 日志切面中需在请求和响应中过滤日志的url
 * @date: 2022/3/7 15:55
 * @author: Riven Ge
 */

@ConfigurationProperties(prefix = "log.filter")
public class UrlFilterProperties {


    /**
     * 全局拦截开关
     */
    private Boolean globalSwitch = false;
    /**
     * 需过滤base64请求或相应的url
     */
    private List<String> bigDataUrls = new ArrayList<>();

    public Boolean getGlobalSwitch() {
        return globalSwitch;
    }

    public void setGlobalSwitch(Boolean globalSwitch) {
        this.globalSwitch = globalSwitch;
    }

    public List<String> getBigDataUrls() {
        return bigDataUrls;
    }

    public void setBigDataUrls(List<String> bigDataUrls) {
        this.bigDataUrls = bigDataUrls;
    }

}