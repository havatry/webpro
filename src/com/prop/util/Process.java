package com.prop.util;

import com.prop.bean.Page;
import com.prop.bean.Record;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 对cookie统一处理
 */
public class Process {
    public static String parseUid(HttpServletRequest req, HttpServletResponse resp) {
        // 从cookie中读取uid
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
            // 新加
            uid = Objects.hash(req.getRemoteAddr()) + String.valueOf(System.currentTimeMillis());
            Cookie cnew = new Cookie("uid_", uid);
            cnew.setMaxAge(60 * 60 * 24 * 365);
            cnew.setPath("/"); // 必须设置
            resp.addCookie(cnew);
        }
        return uid;
    }

    public static Page parsePage(String uid, int pageNo) {
        // 从数据库中获取数据
        RequestDataBase requestDataBase = new RequestDataBase();
        Page page = null;
        try {
            page = requestDataBase.queryAll(uid, pageNo * Constant.SIZE);
            int total = requestDataBase.getToTal(uid);
            page.setTotal(total);
            updatePage(page);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return page;
    }

    private static void updatePage(Page page) throws ParseException, SQLException, ClassNotFoundException {
        boolean autoFlush = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RequestDataBase requestDataBase = new RequestDataBase();
        for (Record r : page.getData()) {
            if (Objects.equals(r.getStatus(), "在执行中")) {
                // 判断是否异常
                if (new Date().getTime() - sdf.parse(r.getDate()).getTime() > Constant.TIME_OUT * 1000) {
                    requestDataBase.updateRequest(r.getId(), "请求异常");
                    r.setStatus("请求异常");
                } else {
                    autoFlush = true;
                }
            }
        }
        page.setAutoFlush(autoFlush);
    }

    public static void setResp(HttpServletResponse resp, String origin) {
        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", origin);
        resp.addHeader("Access-Control-Allow-Credentials", "true");
    }

    public static Map<String, String> asUrlMap(String arguments) {
        Map<String, String> map = new HashMap<>();
        String[] arg = arguments.split("&");
        for (String item : arg) {
            String[] pair = item.split("=");
            map.put(pair[0].trim(), pair[1].trim());
        }
        return map;
    }

    /**
     * 将用户的请求写入数据库，此时状态是在执行中，上下文参数并没有设置
     * @param req
     * @param resp
     * @param type
     * @return
     */
    public static int writeRequestToDB(HttpServletRequest req, HttpServletResponse resp, String type) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uid = parseUid(req, resp);
        int current_id = 0;
        RequestDataBase requestDataBase = new RequestDataBase();
        try {
            current_id = requestDataBase.insertRequest(type, uid, sdf.format(new Date()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return current_id;
    }

    public static double parseFraction(String fra) {
        // 解析出分数
        double a = Double.valueOf(fra.split("/")[0].trim());
        double b = Double.valueOf(fra.split("/")[1].trim());
        return a / b;
    }

    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }
}
