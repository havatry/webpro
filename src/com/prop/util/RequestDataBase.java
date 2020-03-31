package com.prop.util;

import com.prop.bean.Page;
import com.prop.bean.Record;
import com.prop.bean.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 负责与数据库通信
 */
public class RequestDataBase {
    private static final String driver;
    private static final String url;
    private static final String user;
    private static final String password;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("config/jdbc.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = properties.getProperty("jdbc.driver");
        url = properties.getProperty("jdbc.url");
        user = properties.getProperty("jdbc.user", "root");
        password = properties.getProperty("jdbc.password");
    }

    private Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }

    // 插入前端请求并返回自增id
    public int insertRequest(String type, String uid, String date) throws SQLException, ClassNotFoundException {
        String sql = "insert into request(type, status, create_time, operator) values (?, ?, ?, ?)";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, type);
        preparedStatement.setString(2, "在执行中");
        preparedStatement.setString(3, date);
        preparedStatement.setString(4, uid);
        preparedStatement.executeUpdate();
        ResultSet resultSet = preparedStatement.getGeneratedKeys();
        resultSet.next();
        int id = resultSet.getInt(1);
        preparedStatement.close();
        connection.close();
        return id;
    }

    // 将请求的状态改成指定的状态，这在后台完成了映射后去更新数据库
    public boolean updateRequest(Integer id, String status) throws SQLException, ClassNotFoundException {
        String sql = "update request set status = ? where id = ?";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, status);
        preparedStatement.setInt(2, id);
        return preparedStatement.executeUpdate() > 0;
    }

    // 获取请求的执行参数
    public String getArguments(Integer id) throws SQLException, ClassNotFoundException {
        String sql = "select arguments from request where id = ?";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("arguments");
    }

    // 设置参数
    public boolean updateRequestArguments(Integer id, String args) throws SQLException, ClassNotFoundException {
        String sql = "update request set arguments = ? where id = ?";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, args);
        preparedStatement.setInt(2, id);
        return preparedStatement.executeUpdate() > 0;
    }

    // 获取用户uid的所有请求 并且按照时间降序 取出前SIZE条记录
    public Page queryAll(String uid, int offset) throws SQLException, ClassNotFoundException {
        String sql = "select * from request where operator = ? ORDER BY create_time DESC limit ?, ?";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, uid);
        preparedStatement.setInt(2, offset);
        preparedStatement.setInt(3, Constant.SIZE);
        ResultSet resultSet = preparedStatement.executeQuery();
        Page page = new Page();
        List<Record> records = new ArrayList<>();
        while (resultSet.next()) {
            Record record = new Record();
            record.setId(resultSet.getInt("id"));
            record.setDate(resultSet.getString("create_time"));
            record.setStatus(resultSet.getString("status"));
            record.setType(resultSet.getString("type"));
            records.add(record);
        }
        page.setData(records);
        return page;
    }

    // 查询数据库总数
    public int getToTal(String uid) throws SQLException, ClassNotFoundException {
        String sql = "select count(*) from request WHERE  operator = '" + uid + "'";
        Connection connection = connect();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        return resultSet.getInt(1);
    }

    // 删除指定的request
    public boolean deleteRequest(Integer id) throws SQLException, ClassNotFoundException {
        String sql = "delete from request where id = ?";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate() > 0;
    }

    public User queryUser(String username) throws SQLException, ClassNotFoundException {
        String sql = "select * from webpro_user where username = ?";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        User user = null;
        if (resultSet.next()) {
            // 存在
            user = new User();
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setSessionId(resultSet.getString("sessionId"));
            user.setOrigin(resultSet.getString("origin"));
        }
        return user;
    }

    public boolean insertUser(User user) throws SQLException, ClassNotFoundException {
        String sql = "insert into webpro_user (username, password, sessionId, origin) values (?, ?, ?, ?)";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        preparedStatement.setString(3, user.getSessionId());
        preparedStatement.setString(4, user.getOrigin());
        return preparedStatement.executeUpdate() > 0;
    }

    public String findUid(String sessionId) throws SQLException, ClassNotFoundException {
        String sql = "select origin from webpro_user where sessionId = ?";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, sessionId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("origin");
        } else {
            // 找不到
            return null;
        }
    }

    // 搜索框查询
    public List<Record> queryRecords(String start, String end, String str, String uid) throws SQLException, ClassNotFoundException {
        if (start == null) {
            start = "2020-01-01 00:00:00";
        }
        if (end == null) {
            end = "2030-01-01 00:00:00";
        }
        String sql = "select * from request where create_time >= ? and create_time <= ? and operation = ? and " +
                "(type like ? or status like ?) order by create_time desc";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, start);
        preparedStatement.setString(2, end);
        preparedStatement.setString(3, uid);
        preparedStatement.setString(4, str);
        preparedStatement.setString(5, str);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Record> list = new ArrayList<>();
        while (resultSet.next()) {
            Record record = new Record();
            record.setId(resultSet.getInt("id"));
            record.setStatus(resultSet.getString("status"));
            record.setDate(resultSet.getString("create_time"));
            record.setType(resultSet.getString("type"));
        }
        return list;
    }
}
