package cn.com.riven.starter.log.filter;

import cn.com.riven.starter.log.constant.MDCConstants;
import cn.com.riven.starter.log.utils.StringUtils;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.UUID;


public class HttpRequestMDCFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestMDCFilter.class);



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private String getLocalIp() {
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = netInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address.isSiteLocalAddress() && !address.isLoopbackAddress()) {
                        return address.getHostAddress();
                    }
                }
            }
        }catch (Exception e) {
            log.error("getLocalIp error",e);
            return null;
        }
        return null;
    }
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hsr = (HttpServletRequest) request;
        try {
            mdc(hsr);
            chain.doFilter(request, response);
        } finally {
            // 保证每个请求结束后清理，防止线程池复用造成串日志
            MDC.clear();
        }
    }
 
    private void mdc(HttpServletRequest hsr) {
        String traceId = hsr.getHeader(MDCConstants.traceId);
        if (StringUtils.isEmpty(traceId)){
            traceId = UUID.randomUUID().toString().replace("-","");
        }
        MDC.put(MDCConstants.traceId, traceId);
        MDC.put(MDCConstants.userId, hsr.getHeader(MDCConstants.userId));
        MDC.put(MDCConstants.requestChannel, hsr.getHeader(MDCConstants.requestChannel));

    }
 
    @Override
    public void destroy() {

    }
}