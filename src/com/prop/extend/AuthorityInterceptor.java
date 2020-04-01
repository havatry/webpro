package com.prop.extend;

import com.alibaba.fastjson.JSON;
import com.prop.util.Process;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(value = {"/search/*"}, filterName = "restrictExperiment")
public class AuthorityInterceptor implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 判断用户是否登陆
        String uid = Process.parseUid((HttpServletRequest)servletRequest, (HttpServletResponse)servletResponse);
        if (uid.indexOf("session_") >= 0) {
            // 登陆状态
            filterChain.doFilter(servletRequest, servletResponse); // 放行
        } else {
            // 非登陆
            Process.setResp((HttpServletResponse)servletResponse, ((HttpServletRequest)servletRequest).getHeader("Origin"));
            PrintWriter out = servletResponse.getWriter();
            out.println(JSON.toJSONString(JSON.parseObject("{msg:'若要执行查询操作, 请先登陆'}")));
            out.close();
        }
    }

    @Override
    public void destroy() {}
}
