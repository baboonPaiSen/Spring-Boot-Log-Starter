package cn.com.riven.starter.log.configuration;

import cn.com.riven.starter.log.handler.CommonResponseDataAdvice;
import cn.com.riven.starter.log.handler.GlobalDefaultExceptionHandler;
import cn.com.riven.starter.log.intercepter.ControllerAdviceMissCondition;
import cn.com.riven.starter.log.prop.GlobalDefaultProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;

@AutoConfiguration
@EnableConfigurationProperties(GlobalDefaultProperties.class)
@AutoConfigureBefore(MethodLogAutoConfiguration.class)
@Conditional(ControllerAdviceMissCondition.class)
public class GlobalDefaultConfiguration {

    private static final Logger log = LoggerFactory.getLogger(GlobalDefaultConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = "log.exception.defaultExceptionHandler",
            name = {"enabled"},
            havingValue = "true",
            matchIfMissing = false
    )

    public GlobalDefaultExceptionHandler globalDefaultExceptionHandler(GlobalDefaultProperties properties) {
        log.info("log.exception.defaultExceptionHandler.enable= true, 注入全局异常处理器GlobalDefaultExceptionHandler");
        return new GlobalDefaultExceptionHandler(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = "log.exception.responseDataAdvice",
            name = {"enabled"},
            havingValue = "true",
            matchIfMissing = false
    )
    public CommonResponseDataAdvice commonResponseDataAdvice(GlobalDefaultProperties globalDefaultProperties) {
        log.info("log.exception.responseDataAdvice.enable= true, 注入响应处理器CommonResponseDataAdvice");
        return new CommonResponseDataAdvice(globalDefaultProperties);
    }

}