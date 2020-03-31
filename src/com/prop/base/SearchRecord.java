package com.prop.base;

import com.alibaba.fastjson.JSON;
import com.prop.bean.Page;
import com.prop.bean.Record;
import com.prop.util.Constant;
import com.prop.util.Process;
import com.prop.util.RequestDataBase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/search/record")
public class SearchRecord extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取uid, 和查询参数
        String uid = Process.parseUid(req, resp);
        String query = req.getParameter("query");
        int pageNo = Integer.valueOf(req.getParameter("pageNo"));
        String start = null, end = null, type = "", status = "";
        String[] part = query.split(";");
        if (part.length >= 1) {
            // 有时间参数
            if (part[0].indexOf("~") >=0) {
                // 两个
                start = part[0].split("~")[0].trim();
                end = part[0].split("~")[1].trim();
            } else {
                // 只有start
                if (query.indexOf(";") > 0) {
                    start = part[0].substring(0, query.indexOf(";")).trim();
                }
            }
        }
        if (part.length >= 2) {
            // 有type参数
            type = part[1].trim();
        }
        if (part.length >= 3) {
            // status参数
            status = part[2].trim();
        }
        // 执行查询
        List<Record> records = null;
        try {
            records = new RequestDataBase().queryRecords(start, end, type, status, uid);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Page page = new Page();
        page.setAutoFlush(false);
        page.setData(records.subList(pageNo * Constant.SIZE, Math.min(pageNo * Constant.SIZE + Constant.SIZE, records.size())));
        page.setTotal(records.size());
        Process.setResp(resp, req.getHeader("origin"));
        PrintWriter out = resp.getWriter();
        out.println(JSON.toJSONString(page));
        out.close();
    }
}
