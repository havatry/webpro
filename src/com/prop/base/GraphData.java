package com.prop.base;

import com.alibaba.fastjson.JSON;
import com.prop.bean.Record;
import com.prop.util.Process;
import com.prop.util.RequestDataBase;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/look")
public class GraphData extends HttpServlet{
    private RequestDataBase requestDataBase = new RequestDataBase();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取id和cookie
        int id = Integer.valueOf(req.getParameter("id"));
        Path p = Paths.get("results/data/" + String.valueOf(id));
        List<String> content = Files.readAllLines(p.resolve("simulation.txt"));
        List<Integer> GE = new ArrayList<>(), RE = new ArrayList<>(), SE = new ArrayList<>();
        List<Double> GA = new ArrayList<>(), RA = new ArrayList<>(), SA = new ArrayList<>();
        List<Double> GR = new ArrayList<>(), RR = new ArrayList<>(), SR = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        // 获取记录
        try {
            String arguments = requestDataBase.getArguments(id);
            if (arguments.indexOf("Greedy") > 0) {
                map.put("Greedy", 1);
            } else {
                map.put("Greedy", 0);
            }
            if (arguments.indexOf("subgraphIsomorphism") > 0) {
                map.put("subgraphIsomorphism", 1);
            } else {
                map.put("subgraphIsomorphism", 0);
            }
            if (arguments.indexOf("RCRGF") > 0) {
                map.put("RCRGF", 1);
            } else {
                map.put("RCRGF", 0);
            }
            String snodes = Process.asUrlMap(arguments).get("snodes");
            map.put("snodes", snodes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("Greedy", 0); map.put("RCRGF", 0); map.put("SubgraphIsomorphism", 0);
        Files.list(p).forEach((Path f) -> {
//            System.out.println(f.getFileName());
            if (f.getFileName().toString().startsWith("Greedy")) {
                map.put("Greedy", 1);
            } else if (f.getFileName().toString().startsWith("RCRGF")) {
                map.put("RCRGF", 1);
            } else if (f.getFileName().toString().startsWith("SubgraphIsomorphism")) {
                map.put("SubgraphIsomorphism", 1);
            }
        });

        for (String s : content) {
            if (map.get("Greedy").equals(0)) {
                // 说明都是,分隔的
                if (map.get("SubgraphIsomorphism").equals(0)) {
                    // 只有 RCRGF
                    String[] part = s.split(",");
                    for (int i = 0; i < part.length - 1; i+=3) {
                        RE.add(Integer.valueOf(part[i]));
                        RA.add(Double.valueOf(part[i + 1]));
                        RR.add(Double.valueOf(part[i + 2]));
                    }
                } else if (map.get("RCRGF").equals(0)) {
                    // 只有SubgraphIsomorphism
                    String[] part = s.split(",");
                    for (int i = 0; i < part.length - 1; i+=3) {
                        SE.add(Integer.valueOf(part[i]));
                        SA.add(Double.valueOf(part[i + 1]));
                        SR.add(Double.valueOf(part[i + 2]));
                    }
                } else {
                    // 两个都有
                    String[] part = s.split(",");
                    for (int i = 0; i < part.length - 1; i+=6) {
                        RE.add(Integer.valueOf(part[i]));
                        RA.add(Double.valueOf(part[i + 1]));
                        RR.add(Double.valueOf(part[i + 2]));
                        SE.add(Integer.valueOf(part[i + 3]));
                        SA.add(Double.valueOf(part[i + 4]));
                        SR.add(Double.valueOf(part[i + 5]));
                    }
                }
            } else {
                // 以换行分隔
                String[] part = s.split(",");
                if (map.get("SubgraphIsomorphism").equals(0) && map.get("RCRGF").equals(1)) {
                    // 只有 RCRGF和Greedy
                    RE.add(Integer.valueOf(part[0]));
                    RA.add(Double.valueOf(part[1]));
                    RR.add(Double.valueOf(part[2]));
                    GE.add(Integer.valueOf(part[3]));
                    GA.add(Double.valueOf(part[4]));
                    GR.add(Double.valueOf(part[5]));
                } else if (map.get("RCRGF").equals(0) && map.get("SubgraphIsomorphism").equals(1)) {
                    // 只有SubgraphIsomorphism和Greedy
                    SE.add(Integer.valueOf(part[0]));
                    SA.add(Double.valueOf(part[1]));
                    SR.add(Double.valueOf(part[2]));
                    GE.add(Integer.valueOf(part[3]));
                    GA.add(Double.valueOf(part[4]));
                    GR.add(Double.valueOf(part[5]));
                } else if (map.get("RCRGF").equals(0) && map.get("SubgraphIsomorphism").equals(0)){
                    // 只有Greedy
                    GE.add(Integer.valueOf(part[0]));
                    GA.add(Double.valueOf(part[1]));
                    GR.add(Double.valueOf(part[2]));
                } else {
                    // 三个都有
                    RE.add(Integer.valueOf(part[0]));
                    RA.add(Double.valueOf(part[1]));
                    RR.add(Double.valueOf(part[2]));

                    SE.add(Integer.valueOf(part[3]));
                    SA.add(Double.valueOf(part[4]));
                    SR.add(Double.valueOf(part[5]));

                    GE.add(Integer.valueOf(part[6]));
                    GA.add(Double.valueOf(part[7]));
                    GR.add(Double.valueOf(part[8]));
                }
            }
        }
        map.put("GA", GA); map.put("GR", GR); map.put("GE", GE);
        map.put("RA", RA); map.put("RR", RR); map.put("RE", RE);
        map.put("SA", SA); map.put("SR", SR); map.put("SE", SE);
        Process.setResp(resp, req.getHeader("Origin"));
        PrintWriter out = resp.getWriter();
        out.println(JSON.toJSONString(map));
        out.close();
    }
}
