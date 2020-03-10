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
 * ���������ִ�в���
 */
@WebServlet("/arguments")
public class ArgumentData extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ��ȡ����id
        // ���ظ�id��Ӧ�Ĳ���ֵ
        String id = req.getParameter("id");
        Map<String, String> ret = new HashMap<>();
        if (id == null) {
            ret.put("msg", "�쳣���, ���ܲ���ͨ�����ʵ�·������");
        } else {
            RequestDataBase requestDataBase = new RequestDataBase();
            try {
                String args = requestDataBase.getArguments(Integer.valueOf(id));
                // ��&�ָ���
                String[] part = args.split("&");
                for (String p : part) {
                    String[] two = p.split("=");
                    ret.put(two[0], two[1]);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ret.put("msg", "��ȡ����δ�ɹ�");
            }
        }
        Process.setResp(resp, req.getHeader("origin"));
        PrintWriter out = resp.getWriter();
        out.println(JSON.toJSONString(ret));
        out.close();
    }
}
