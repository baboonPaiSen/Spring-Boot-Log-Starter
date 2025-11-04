package cn.com.riven.starter.log.annotation;

import java.lang.annotation.*;

import cn.com.riven.starter.log.enums.OperatorType;

/**
 * @author riven
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log{

    /**
     * 操作人类别
     */
    OperatorType operatorType() default OperatorType.MANAGE;

}