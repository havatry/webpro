package com.prop.base;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ����ִ�н��
 */
@WebServlet("/download")
public class DownloadResult extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ��ȡid
        // ���zip������ �ο�https://www.cnblogs.com/nuccch/p/7151228.html
        String id = req.getParameter("id");
        if (id == null) {
            return;
        }
        // ��Ŀ¼���
    }
}
