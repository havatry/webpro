package com.prop.base;

import com.alibaba.fastjson.JSON;
import com.prop.util.Process;
import com.prop.util.RequestDataBase;

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
 * 返回请求的执行参数
 */
@WebServlet("/arguments")
public class ArgumentData extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取参数id
        // 返回该id对应的参数值
        String id = req.getParameter("id");
        Map<String, String> ret = new HashMap<>();
        if (id == null) {
            ret.put("msg", "异常情况, 可能不是通过合适的路径访问");
        } else {
            RequestDataBase requestDataBase = new RequestDataBase();
            try {
                String args = requestDataBase.getArguments(Integer.valueOf(id));
                // 是&分隔的
                String[] part = args.split("&");
                for (String p : part) {
                    String[] two = p.split("=");
                    ret.put(two[0], two[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ret.put("msg", "获取参数未成功");
            }
        }
        Process.setResp(resp, req.getHeader("origin"));
        PrintWriter out = resp.getWriter();
        out.println(JSON.toJSONString(ret));
        out.close();
    }
}
