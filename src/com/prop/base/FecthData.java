package com.prop.base;

import com.alibaba.fastjson.JSON;
import com.prop.bean.Page;
import com.prop.util.Process;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 用于分页插件获取数据库数据
 */
@WebServlet("/fecth")
public class FecthData extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uid = Process.parseUid(req, resp);
        int pageNo = Integer.valueOf(req.getParameter("pageNo"));
        Page page = Process.parsePage(uid, pageNo);
        Process.setResp(resp, req.getHeader("origin"));
        PrintWriter out = resp.getWriter();
        out.println(JSON.toJSONString(page));
        out.close();
    }
}
