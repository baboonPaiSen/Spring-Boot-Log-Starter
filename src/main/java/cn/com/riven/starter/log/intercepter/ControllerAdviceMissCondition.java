package cn.com.riven.starter.log.intercepter;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Map;

public class ControllerAdviceMissCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String, Object> beansWithAnnotation = beanFactory.getBeansWithAnnotation(ControllerAdvice.class);
        return beansWithAnnotation.isEmpty();
    }
}