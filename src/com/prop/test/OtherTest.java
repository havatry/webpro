package com.prop.test;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OtherTest {
    @Test
    public void testJson() {
        Map<String, String> map = new HashMap<>();
        map.put("msg", "hello");
        System.out.println(JSON.toJSONString(map));
    }

    @Test
    public void testSplit() {
        String line = "35 44 56 66 79 93 99 107 110 115 124 142 155 172 182 188 194 205 213 219 233 246 256 273 287 299 303 308 324 333 344 360 376 384 403 408 410 412 419 432 ";
//        System.out.println(line.split(" "));
        List<String> content = Arrays.asList(line);
        for (String p : content.get(0).split(" ")) {
            System.out.println(p);
        }
    }
}
