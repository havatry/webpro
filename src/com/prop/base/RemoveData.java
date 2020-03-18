package com.prop.base;

import com.alibaba.fastjson.JSON;
import com.prop.util.Process;
import com.prop.util.RequestDataBase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 删除一些请求
 */
@WebServlet("/remove")
public class RemoveData extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Map<String, String> ret = new HashMap<>(); // 返回的消息
        if (id == null) {
            ret.put("msg", "异常情况, 可能不是通过合适的路径访问");
        } else {
            // 删除对应的文件夹
            File target = new File("results/data/" + id);
            // 先删除其中的文件
            for (File f : target.listFiles()) {
                f.delete();
            }
            target.delete();
            // 删除压缩包
            new File("results/data/"+id+".zip").delete();
            RequestDataBase requestDataBase = new RequestDataBase();
            try {
                // 删除数据库中的记录
                requestDataBase.deleteRequest(Integer.valueOf(id));
                ret.put("msg", "删除成功");
            } catch (Exception e) {
                e.printStackTrace();
                ret.put("msg", "删除未成功");
            }
        }
        Process.setResp(resp, req.getHeader("origin"));
        PrintWriter out = resp.getWriter();
        out.println(JSON.toJSONString(ret));
        out.close();
    }
}
