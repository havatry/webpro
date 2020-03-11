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
 * ���������ݿ�ͨ��
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

    // ����ǰ�����󲢷�������id
    public int insertRequest(String type, String algorithm, String uid, String date) throws SQLException, ClassNotFoundException {
        String sql = "insert into request(type, status, algorithm, create_time, operator) values (?, ?, ?, ?, ?)";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, type);
        preparedStatement.setString(2, "��ִ����");
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

    // �������״̬�ĳ�ָ����״̬�����ں�̨�����ӳ���ȥ�������ݿ�
    public boolean updateRequest(Integer id, String status) throws SQLException, ClassNotFoundException {
        String sql = "update request set status = ? where id = ?";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, status);
        preparedStatement.setInt(2, id);
        return preparedStatement.executeUpdate() > 0;
    }

    // ��ȡ�����ִ�в���
    public String getArguments(Integer id) throws SQLException, ClassNotFoundException {
        String sql = "select arguments from request where id = ?";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return resultSet.getString("arguments");
    }

    // ���ò���
    public boolean updateRequestArguments(Integer id, String args) throws SQLException, ClassNotFoundException {
        String sql = "update request set arguments = ? where id = ?";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, args);
        preparedStatement.setInt(2, id);
        return preparedStatement.executeUpdate() > 0;
    }

    // ��ȡ�û�uid���������� ���Ұ���ʱ�併�� ȡ��ǰSIZE����¼
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

    // ��ѯ���ݿ�����
    public int getToTal(String uid) throws SQLException, ClassNotFoundException {
        String sql = "select count(*) from request WHERE  operator = '" + uid + "'";
        Connection connection = connect();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        return resultSet.getInt(1);
    }

    // ɾ��ָ����request
    public boolean deleteRequest(Integer id) throws SQLException, ClassNotFoundException {
        String sql = "delete from request where id = ?";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        return preparedStatement.executeUpdate() > 0;
    }
}
