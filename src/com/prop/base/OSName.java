package com.prop.base;

import com.alibaba.fastjson.JSON;
import com.prop.util.Process;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created at 2020/4/4
 */
@WebServlet("/os")
public class OSName extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> map = new HashMap<>();
        map.put("os", Process.isLinux() ? "linux" : "windows");
        Process.setResp(resp, req.getHeader("origin"));
        PrintWriter out = resp.getWriter();
        out.println(JSON.toJSONString(map));
        out.close();
    }
}
