package cn.com.riven.starter.log.handler;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class LogbackMessConverter extends MessageConverter {

    private static boolean eagleInit = false;

    public LogbackMessConverter() {
    }

    @Override
    public String convert(ILoggingEvent event) {
        return super.convert(event);
    }


    /**
     * 处理异常
     *
     * @param info
     * @param error
     * @param warn
     */
    private void handleThrowable(ILoggingEvent iLoggingEvent, boolean info, boolean error, boolean warn) {
        String loggerName = iLoggingEvent.getLoggerName();
        Level level = iLoggingEvent.getLevel();
        String message = iLoggingEvent.getMessage();
        
        //有异常
        IThrowableProxy throwableProxy = iLoggingEvent.getThrowableProxy();

        if (Objects.nonNull(throwableProxy)) {
            StackTraceElementProxy[] stackTraceElementProxyArray = throwableProxy.getStackTraceElementProxyArray();
            // 处理异常堆栈信息
        } else {
            // 处理普通消息
        }
    }
}