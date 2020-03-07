package com.prop.util;

import com.prop.base.RequestDataBase;
import com.prop.bean.Page;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

/**
 * ��cookieͳһ����
 */
public class Process {
    public static String parseUid(HttpServletRequest req, HttpServletResponse resp) {
        // ��cookie�ж�ȡuid
        Cookie[] cookies = req.getCookies();
        String uid = null;
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (Objects.equals(c.getName(), "uid_")) {
                    uid = c.getValue();
                    break;
                }
            }
        }
        if (uid == null) {
            // �¼�
            uid = Objects.hash(req.getRemoteAddr()) + String.valueOf(new Date().getTime());
            Cookie cnew = new Cookie("uid_", uid);
            cnew.setMaxAge(60 * 60 * 24);
            cnew.setPath("/"); // ��������
            resp.addCookie(cnew);
        }
        return uid;
    }

    public static Page parsePage(String uid, int pageNo) {
        // �����ݿ��л�ȡ����
        RequestDataBase requestDataBase = new RequestDataBase();
        Page page = null;
        try {
            page = requestDataBase.queryAll(uid, pageNo * Constant.SIZE);
            int total = requestDataBase.getToTal(uid);
            page.setTotal(total);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return page;
    }

    public static void setResp(HttpServletResponse resp, String origin) {
        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", origin);
        resp.addHeader("Access-Control-Allow-Credentials", "true");
    }
}
