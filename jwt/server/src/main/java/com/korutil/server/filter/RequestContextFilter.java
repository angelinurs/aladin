package com.korutil.server.filter;

import com.korutil.server.util.RequestContext;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

@WebFilter("/*")
public class RequestContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {
            // HttpServletRequest를 ThreadLocal에 저장
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            RequestContext.setRequest(httpRequest);

            // 필터 체인 진행
            chain.doFilter(request, response);
        } finally {
            RequestContext.clear();
        }
    }
}