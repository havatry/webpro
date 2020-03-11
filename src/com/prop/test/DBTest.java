package com.prop.test;

import com.alibaba.fastjson.JSON;
import com.prop.util.RequestDataBase;
import com.prop.bean.Record;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;

public class DBTest {
    @Test
    public void testInsertRequest() throws SQLException, ClassNotFoundException {
        String type = "手动分析";
        String uid = Objects.hash("10.1.2.3") + "" + new Date().getTime();
        RequestDataBase requestDataBase = new RequestDataBase();
        requestDataBase.insertRequest(type, "RCGRF", uid, "2020-03-03 12:02:03");
    }

    @Test
    public void testUpdateRequestStatus() throws SQLException, ClassNotFoundException {
        String status = "执行完成";
        Integer id = 3;
        RequestDataBase requestDataBase = new RequestDataBase();
        System.out.println(requestDataBase.updateRequest(id, status));
    }

    @Test
    public void test() {
//        System.out.println(System.getProperty("java.io.tmpdir"));
//        Map<String, String> map = new HashMap<>();
//        map.put("name", "hello");
//        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(map));
//        System.out.println(JSON.toJSONString(map));
//        System.out.println(map);
        Record record = new Record();
        record.setId(8);
        record.setType("手动分析");
        record.setStatus("在执行中");
        record.setDate("2020-03-03 11:20:03");
        System.out.println(JSON.toJSONString(record));
    }

    @Test
    public void testQueryId() throws SQLException, ClassNotFoundException {

    }

    @Test
    public void testDeleteRequest() throws SQLException, ClassNotFoundException {
        Integer id = 5;
        RequestDataBase requestDataBase = new RequestDataBase();
        System.out.println(requestDataBase.deleteRequest(id));
    }
}
