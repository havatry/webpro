package com.prop.base;

import com.alibaba.fastjson.JSON;
import com.prop.bean.User;
import com.prop.util.Process;
import com.prop.util.RequestDataBase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@WebServlet("/login")
public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String type = req.getParameter("type"); // 是登录还是注册
        String uid = Process.parseUid(req, resp);
        RequestDataBase requestDataBase = new RequestDataBase();
        Process.setResp(resp, req.getHeader("origin"));
        PrintWriter out = resp.getWriter();
        Map<String, String> map = new HashMap<>();
        // 处理登录情况
        if (Objects.equals(type, "login")) {
            // 向数据库匹配密码是否正确
            User user = null;
            try {
                user = requestDataBase.queryUser(username);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (user != null && Objects.equals(password, user.getPassword())) {
                // 登录成功
                // 写入sessionId的cookie
                Cookie cnew = new Cookie("uid_", user.getSessionId());
                cnew.setMaxAge(60 * 60 * 24 * 365);
                cnew.setPath("/"); // 必须设置
                resp.addCookie(cnew);
                map.put("msg", "用户登录成功");
            } else if (user == null){
                // 用户不存在 先注册
                map.put("msg", "用户不存在,请先注册");
            } else {
                // 密码不匹配
                map.put("msg", "用户名或者密码不正确");
            }
        } else if (Objects.equals(type, "register")) {
            // 注册
            // 将用户名 密码 生成的sessionh额原id写入数据库
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setSessionId("session_" + Objects.hash(user.getUsername(), user.getPassword()));
            user.setOrigin(uid);
            boolean succ = false;
            try {
                succ = requestDataBase.insertUser(user);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            map.put("msg", succ ? "注册成功, 请点击登录" : "注册未成功，用户名已存在，请更换用户名重新注册");
        } else if (Objects.equals(type, "logout")){
            // 注销
            // 通过sessionId找到uid
            String origin = null;
            try {
                origin = requestDataBase.findUid(uid); // 此时的uid为sessionId, origin为uid
                // 写入cookie
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (origin == null) {
                // 注销失败 出现异常
                map.put("msg", "注销失败，请联系管理员");
            } else {
                Cookie cnew = new Cookie("uid_", origin);
                cnew.setMaxAge(60 * 60 * 24 * 365);
                cnew.setPath("/"); // 必须设置
                resp.addCookie(cnew);
                map.put("msg", "用户注销成功");
            }
        }
        out.println(JSON.toJSONString(map));
        out.close();
    }
}
