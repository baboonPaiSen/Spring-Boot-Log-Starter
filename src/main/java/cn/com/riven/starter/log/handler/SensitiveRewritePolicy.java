package cn.com.riven.starter.log.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginNode;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.impl.MutableLogEvent;
import org.apache.logging.log4j.message.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.com.riven.starter.log.utils.ErrorLogUtils.getErrorMsg;

@Plugin(name = "SensitiveRewritePolicy", category = Node.CATEGORY, elementType = "rewritePolicy", printObject = true)
public class SensitiveRewritePolicy implements RewritePolicy {


    private static boolean eagleInit = false;

    public SensitiveRewritePolicy() {
    }

    @Override
    public LogEvent rewrite(LogEvent source) {
        // 直接返回原始的。
        return source;
    }

    private Message getMessage(LogEvent source) {
        if (source instanceof Log4jLogEvent) {
            Log4jLogEvent log4jLogEvent = (Log4jLogEvent) source;
            return log4jLogEvent.getMessage();
        } else if (source instanceof MutableLogEvent) {
            MutableLogEvent log4jLogEvent = (MutableLogEvent) source;
            return log4jLogEvent.getMessage();
        }

        return source.getMessage();
    }

    // 指定对应的 factory

    /**
     * @param pluginConfig 配置
     * @param pluginNode   节点
     * @return 结果
     */
    @PluginFactory
    public static SensitiveRewritePolicy createPolicy(@PluginConfiguration Configuration pluginConfig,
                                                      @PluginNode Node pluginNode
    ) {
        return new SensitiveRewritePolicy();
    }


    public void handlerThrowable(LogEvent iLoggingEvent,String eagleLevel) {
        Level level = iLoggingEvent.getLevel();
        String loggerName = iLoggingEvent.getLoggerName();
        Throwable throwable = iLoggingEvent.getThrown();
        String[] levelList = {"error"};
        if (StringUtils.isNotEmpty(eagleLevel)) {
            levelList = eagleLevel.split(",");
        }
        List<String> levels = Arrays.stream(levelList).collect(Collectors.toList());
        boolean info = levels.contains("info");
        boolean error = levels.contains("error");
        boolean warn = levels.contains("warn");
        String message = iLoggingEvent.getMessage().getFormattedMessage();
        
        //有异常
        if (Objects.nonNull(throwable)) {
            // 处理异常
        } else {
            // 处理普通消息
        }
    }
}