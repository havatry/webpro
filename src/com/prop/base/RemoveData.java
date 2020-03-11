package com.prop.base;

import com.alibaba.fastjson.JSON;
import com.prop.util.Process;

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
 * ɾ��һЩ����
 */
@WebServlet("/remove")
public class RemoveData extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        Map<String, String> ret = new HashMap<>(); // ���ص���Ϣ
        if (id == null) {
            ret.put("msg", "�쳣���, ���ܲ���ͨ�����ʵ�·������");
        } else {
            // ɾ����Ӧ���ļ���
            File target = new File("results/data/" + id);
            // ��ɾ�����е��ļ�
            for (File f : target.listFiles()) {
                f.delete();
            }
            target.delete();
            // ɾ��ѹ����
            new File("results/data/"+id+".zip").delete();
            RequestDataBase requestDataBase = new RequestDataBase();
            try {
                // ɾ�����ݿ��еļ�¼
                requestDataBase.deleteRequest(Integer.valueOf(id));
                ret.put("msg", "ɾ���ɹ�");
            } catch (Exception e) {
                e.printStackTrace();
                ret.put("msg", "ɾ��δ�ɹ�");
            }
        }
        Process.setResp(resp, req.getHeader("origin"));
        PrintWriter out = resp.getWriter();
        out.println(JSON.toJSONString(ret));
        out.close();
    }
}
