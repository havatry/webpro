package com.prop.extend;

import com.prop.util.Process;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(value = "/*", filterName = "AllExceptionProcess")
public class ExceptionHandler implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            Process.setResp((HttpServletResponse)servletResponse, ((HttpServletRequest)servletRequest).getHeader("origin"));
            PrintWriter out = servletResponse.getWriter();
            out.println("请求处理异常, 请按照帮助文档说明操作或者联系管理");
            out.close();
        }
    }

    @Override
    public void destroy() {

    }
}
