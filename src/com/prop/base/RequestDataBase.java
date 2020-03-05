package com.prop.base;

import com.prop.bean.Record;

import java.sql.*;

/**
 * 负责与数据库通信
 */
public class RequestDataBase {
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://175.24.101.52:3306/test?serverTimezone=GMT%2B8";
    private static final String user = "root";
    private static final String password = "~Ir3M}q7.,U4&VI1";

    private Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return DriverManager.getConnection(url, user, password);
    }

    // 插入前端请求
    public boolean insertRequest(String type, String algorithm, String uid, String date) throws SQLException, ClassNotFoundException {
        String sql = "insert into request(type, status, algorithm, create_time, operator) values (?, ?, ?, ?, ?)";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, type);
        preparedStatement.setString(2, "在执行中");
        preparedStatement.setString(3, algorithm);
        preparedStatement.setString(4, date);
        preparedStatement.setString(5, uid);
        boolean succ = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        connection.close();
        return succ;
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

    // 获取请求的唯一id
    public Record queryOne(String dateStr, String uid) throws SQLException, ClassNotFoundException {
        String sql = "select * from request where create_time = ? and operator = ?";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, dateStr);
        preparedStatement.setString(2, uid);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Record record = new Record();
        // 只有一条记录
        record.setId(resultSet.getInt("id"));
        record.setDate(dateStr);
        record.setAlgorithm(resultSet.getString("algorithm"));
        return record;
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
