package com.prop.base;

import com.prop.bean.Page;
import com.prop.bean.Record;
import com.prop.util.Constant;

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
            properties.load(new FileInputStream("src/resources/config/jdbc.properties"));
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
    public int insertRequest(String type, String algorithm, String uid, String date) throws SQLException, ClassNotFoundException {
        String sql = "insert into request(type, status, algorithm, create_time, operator) values (?, ?, ?, ?, ?)";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, type);
        preparedStatement.setString(2, "在执行中");
        preparedStatement.setString(3, algorithm);
        preparedStatement.setString(4, date);
        preparedStatement.setString(5, uid);
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
            record.setAlgorithm(resultSet.getString("algorithm"));
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
}
