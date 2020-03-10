package com.prop.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class OtherTest {
    @Test
    public void testJson() {
        Map<String, String> map = new HashMap<>();
        map.put("msg", "hello");
        System.out.println(JSON.toJSONString(map));
    }
}
