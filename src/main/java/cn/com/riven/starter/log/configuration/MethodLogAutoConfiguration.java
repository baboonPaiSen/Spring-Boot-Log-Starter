package cn.com.riven.starter.log.configuration;

import cn.com.riven.starter.log.filter.HttpRequestMDCFilter;
import cn.com.riven.starter.log.handler.ControllerLogAspect;
import cn.com.riven.starter.log.prop.UrlFilterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnMissingClass("org.springframework.cloud.gateway.filter.GlobalFilter")
@EnableConfigurationProperties({UrlFilterProperties.class})
public class MethodLogAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MethodLogAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = "log.methodLogAspect",
            name = {"enabled"},
            havingValue = "true",
            matchIfMissing = false
    )
    public ControllerLogAspect methodLogAspect(UrlFilterProperties urlFilterProperties){
        log.info("log.methodLogAspect.enable= true, 注入日志切面ControllerLogAspect");
        return new ControllerLogAspect(urlFilterProperties);
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "log.httpRequestMDCFilter",
            name = {"enabled"},
            havingValue = "true",
            matchIfMissing = false
    )
    @ConditionalOnMissingBean
    public HttpRequestMDCFilter httpRequestMDCFilter(){
        log.info("log.httpRequestMDCFilter.enable= true, 注入过滤器HttpRequestMDCFilter");
        return new HttpRequestMDCFilter();
    }

}