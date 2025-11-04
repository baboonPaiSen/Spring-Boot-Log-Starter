package cn.com.riven.starter.log.handler;


import cn.com.riven.starter.log.annotation.Log;
import cn.com.riven.starter.log.enums.BusinessStatus;
import cn.com.riven.starter.log.model.SysOperLogDto;
import cn.com.riven.starter.log.prop.UrlFilterProperties;
import cn.com.riven.starter.log.utils.*;

import com.alibaba.fastjson.JSON;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringEscapeUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 操作日志记录处理
 */
@Aspect
@Order(-2147483647)
public class ControllerLogAspect {


    @Autowired
    private Environment environment;


    private static final Logger log = LoggerFactory.getLogger(ControllerLogAspect.class);

    private final UrlFilterProperties urlFilterProperties;

    public ControllerLogAspect(UrlFilterProperties urlFilterProperties) {
        this.urlFilterProperties = urlFilterProperties;
    }


    // 配置织入点
    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.RequestMapping) ")
    public void logPointCut() {
    }


    /**
     * @description 使用环绕通知
     * <p>
     * <p>
     * 环绕通知可以将你所编写的逻辑将被通知的目标方法完全包装起来。
     * 我们可以使用一个环绕通知来代替之前多个不同的前置通知和后置通知。
     * 如下所示，前置通知和后置通知位于同一个方法中，不像之前那样分散在不同的通知方法里面。
     */
    @Around("logPointCut()")
    public Object doAroundGame(ProceedingJoinPoint joinPoint) throws Throwable {
        if (RequestContextHolder.getRequestAttributes() == null){
                return joinPoint.proceed();
        }
        long start = System.currentTimeMillis();
        SysOperLogDto operLog = new SysOperLogDto();
        try {

            if (Objects.nonNull(environment)) {
                operLog.setModel(environment.getProperty("spring.application.name"));
            }

            // 构建请求前的操作信息
            buildPreOperLog(joinPoint, operLog);
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (Objects.nonNull(controllerLog)) {
                // 设置操作人类别
                operLog.setOperatorType(controllerLog.operatorType().name());
            } else {
                String param = "";
                // 根据url选择过滤请求参数
                if (urlFilterProperties.getBigDataUrls().contains(operLog.getOperUrl()) || urlFilterProperties.getGlobalSwitch()) {
                    try {
                        param = buildRequestParamHideBase64(joinPoint);
                    } catch (Throwable e) {
                        // 便于检查暂用info级别
                        log.error("buildRequestParam获取参数异常", e);
                        param = buildRequestParam(joinPoint);
                    }
                } else {
                    param = buildRequestParam(joinPoint);

                }
                operLog.setOperParam(param);
            }
            // 执行
            Object resp = joinPoint.proceed();
            long end = System.currentTimeMillis();
            long spend = end - start;
            operLog.setContinueTime(String.valueOf(spend));
            if (urlFilterProperties.getBigDataUrls().contains(operLog.getOperUrl()) || urlFilterProperties.getGlobalSwitch()) {
                try {
                    Object o = HideBigDataUtils.hideObject(resp);
                    operLog.setJsonResult(StringEscapeUtils.unescapeJson(FastJsonUtil.toJson(o)));
                } catch (Throwable e) {
                    // 便于检查暂用info级别
                    log.error("设置响应参数异常", e);
                    operLog.setJsonResult(StringEscapeUtils.unescapeJson(FastJsonUtil.toJson(resp)));
                }
            } else {
                operLog.setJsonResult(StringEscapeUtils.unescapeJson(FastJsonUtil.toJson(resp)));

            }

            operLog.setStatus(BusinessStatus.SUCCESS.name());

            log.info("api:{}->{}|time:{}ms|method:{}|ip:{}|--->req:{}|<---resp:{}",
                    operLog.getOperUrl(),
                    operLog.getStatus(),
                    operLog.getContinueTime(),
                    operLog.getMethod(),
                    operLog.getOperIp(),
                    operLog.getOperParam(),
                    operLog.getJsonResult());

            return resp;

        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            long spend = end - start;
            operLog.setContinueTime(String.valueOf(spend));
            operLog.setStatus(BusinessStatus.FAIL.name());
            operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            // 记录本地异常日志

            log.error("api:{}->{}|time:{}ms|method:{}|ip:{}|--->req:{}|ex:{}|msg:{}",
                    operLog.getOperUrl(),
                    operLog.getStatus(),
                    operLog.getContinueTime(),
                    operLog.getMethod(),
                    operLog.getOperIp(),
                    operLog.getOperParam(),
                    e.getClass().getName(),
                    e.getMessage(),
                    e);

            // 抛出业务异常e
            throw e;
        }


    }

    private String buildRequestParamHideBase64(ProceedingJoinPoint joinPoint) {

        HttpServletRequest request = ServletUtil.getRequest();
        if (StringUtils.startsWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            StringBuilder params = new StringBuilder();
            Object[] paramsArray = joinPoint.getArgs();
            if (paramsArray != null && paramsArray.length > 0) {
                for (Object o : paramsArray) {
                    // 如果是需要过滤的对象(如文件)，则返回true；否则返回false。
                    if (!isFilterObject(o)) {
                        Object o1 = HideBigDataUtils.hideObject(o);
                        Object jsonObj = JSON.toJSON(o1);
                        params.append(StringEscapeUtils.unescapeJson(String.valueOf(jsonObj)));
                        params.append(" ");
                    }
                }
            }
            return params.toString().trim();
        }
        Map<String, String> map = new HashMap<>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String[] paramValues = request.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                    Object o1 = HideBigDataUtils.hideObject(paramValue);
                    map.put(paramName, StringEscapeUtils.unescapeJson(String.valueOf(o1)));
                }
            }
        }
        return String.valueOf(map);
    }


    /**
     * 构建操作之前的log信息
     *
     * @param joinPoint
     * @param operLog
     */
    private void buildPreOperLog(ProceedingJoinPoint joinPoint, SysOperLogDto operLog) {

        //请求的地址
        operLog.setOperIp(getIpAddress());
        String requestUri = ServletUtil.getRequest().getRequestURI();
        operLog.setOperUrl(requestUri);

        // 设置方法名称
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        // 设置请求方式
        operLog.setRequestMethod(ServletUtil.getRequest().getMethod());
        operLog.setMethod(className + "." + methodName + "()");
        operLog.setOperTime(new Date());
    }


    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }


    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    public static boolean isFilterObject(final Object o) {
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }


    /**
     * 构建参数信息
     *
     * @return
     */
    public static String buildRequestParam(ProceedingJoinPoint point) {
        try {
            HttpServletRequest request = ServletUtil.getRequest();
            if (StringUtils.startsWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
                StringBuilder params = new StringBuilder();
                Object[] paramsArray = point.getArgs();
                if (paramsArray != null && paramsArray.length > 0) {
                    for (Object o : paramsArray) {
                        // 如果是需要过滤的对象(如文件)，则返回true；否则返回false。
                        if (!isFilterObject(o)) {
                            Object jsonObj = JSON.toJSON(o);
                            params.append(StringEscapeUtils.unescapeJson(String.valueOf(jsonObj)));
                            params.append(" ");
                        }
                    }
                }
                return params.toString().trim();
            }
            Map<String, String> map = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String[] paramValues = request.getParameterValues(paramName);
                if (paramValues.length == 1) {
                    String paramValue = paramValues[0];
                    if (paramValue.length() != 0) {
                        map.put(paramName, StringEscapeUtils.unescapeJson(paramValue));
                    }
                }
            }
            return String.valueOf(map);
        } catch (Exception e) {
            log.error("buildRequestParam获取参数异常", e);
            return "";
        }
    }


    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     *
     * @return
     */
    public static String getIpAddress() {
        HttpServletRequest request = ServletUtil.getRequest();
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String defaultIp = "0:0:0:0:0:0:0:1";
        final String localhostIp = "127.0.0.1";
        return ip.equals(defaultIp) ? localhostIp : ip;
    }
}