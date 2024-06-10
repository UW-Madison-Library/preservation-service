package edu.wisc.library.sdg.preservation.common.spring;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class RequestIdFilter implements Filter {

    public static final String REQUEST_ID = "edu.wisc.library.preservation.RequestId";

    private static final String MDC_REQUEST_ID = "xRequestId";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var requestId = UUID.randomUUID().toString().replace("-", "");

        MDC.put(MDC_REQUEST_ID, requestId);
        servletRequest.setAttribute(REQUEST_ID, requestId);

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove(MDC_REQUEST_ID);
        }
    }

    @Override
    public void destroy() {

    }

}
