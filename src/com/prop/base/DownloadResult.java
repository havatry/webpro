package com.prop.base;

import com.prop.util.Process;
import com.prop.util.ZipWrite;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.zip.ZipOutputStream;

/**
 * ����ִ�н��
 */
@WebServlet("/download")
public class DownloadResult extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ��ȡid
        // ���zip������ �ο�https://www.cnblogs.com/nuccch/p/7151228.html
        String id = req.getParameter("id");
        if (id == null) {
            PrintWriter out = resp.getWriter();
            out.println("���س��ִ��󣬿��ܷ���������Դ�����ڻ��߷���·�����ʺ�");
            out.close();
            return;
        }
        // ��Ŀ¼���
        File file = new File("results/data/" + String.valueOf(id));
        String targetFilePath = "results/data/" + String.valueOf(id) + ".zip";
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetFilePath));
        ZipWrite.compress(file, zos, file.getName(), true);
        // ��ѹ�������͸�ǰ��
//        Process.setResp(resp, req.getHeader("origin"));
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment;filename=" + String.valueOf(id) + ".zip");
        ServletOutputStream out = resp.getOutputStream();
        FileInputStream inputStream = new FileInputStream(targetFilePath);
        int len;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            // ����������
            out.write(buffer, 0, len);
        }
        out.close();
    }
}
