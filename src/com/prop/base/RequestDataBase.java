package com.prop.base;

import com.prop.bean.Page;
import com.prop.bean.Record;
import com.prop.util.Constant;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * ���������ݿ�ͨ��
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

    // ����ǰ������
    public boolean insertRequest(String type, String algorithm, String uid, String date) throws SQLException, ClassNotFoundException {
        String sql = "insert into request(type, status, algorithm, create_time, operator) values (?, ?, ?, ?, ?)";
        Connection connection = connect();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, type);
        preparedStatement.setString(2, "��ִ����");
        preparedStatement.setString(3, algorithm);
        preparedStatement.setString(4, date);
        preparedStatement.setString(5, uid);
        boolean succ = preparedStatement.executeUpdate() > 0;
        preparedStatement.close();
        connection.close();
        return succ;
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
