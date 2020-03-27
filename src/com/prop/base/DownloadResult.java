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
 * 下载执行结果
 */
@WebServlet("/download")
public class DownloadResult extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取id
        // 打包zip并传输 参考https://www.cnblogs.com/nuccch/p/7151228.html
        String id = req.getParameter("id");
        if (id == null) {
            PrintWriter out = resp.getWriter();
            out.println("下载出现错误，可能服务器上资源不存在或者访问路径不适合");
            out.close();
            return;
        }
        // 将目录打包
        String targetFilePath = "results/data/" + String.valueOf(id) + ".zip";
        if (!new File(targetFilePath).exists()) {
            File file = new File("results/data/" + String.valueOf(id));
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(targetFilePath));
            ZipWrite.compress(file, zos, file.getName(), true);
        }
        // 将压缩包发送给前端
        resp.setContentType("application/octet-stream");
        resp.setHeader("Content-Disposition", "attachment;filename=" + String.valueOf(id) + ".zip");
        ServletOutputStream out = resp.getOutputStream();
        FileInputStream inputStream = new FileInputStream(targetFilePath);
        int len;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            // 还存在数据
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
    }
}
