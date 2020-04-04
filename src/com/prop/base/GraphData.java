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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

@WebServlet("/look")
public class GraphData extends HttpServlet{
    private RequestDataBase requestDataBase = new RequestDataBase();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String type = req.getParameter("type");
        int id = Integer.valueOf(req.getParameter("id"));
        String arguments = null;
        try {
            arguments = requestDataBase.getArguments(id);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Map<String, String> t = Process.asUrlMap(arguments);
        if (Objects.equals(type, "一阶段实验")) {
            oneStageData(req, resp, t, id);
        } else if (Objects.equals(type, "二阶段协调实验")) {
            twoStageData(req, resp, t, id);
        }
    }

    private void oneStageData(HttpServletRequest req, HttpServletResponse resp, Map<String, String> t, int id) throws ServletException, IOException {
        Path p = Paths.get("results/data/" + String.valueOf(id));
        List<String> content = Files.readAllLines(p.resolve("simulation.txt"));
        List<Integer> GE = new ArrayList<>(), RE = new ArrayList<>(), SE = new ArrayList<>();
        List<Double> GA = new ArrayList<>(), RA = new ArrayList<>(), SA = new ArrayList<>();
        List<Double> GR = new ArrayList<>(), RR = new ArrayList<>(), SR = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        String algorithms = t.get("algorithms");
        map.put("legend", algorithms);
        map.put("snodes", t.get("snodes"));
        for (String s : content) {
            if (algorithms.indexOf("Greedy") < 0) {
                // 说明都是,分隔的
                if (algorithms.indexOf("SubgraphIsomorphism") < 0) {
                    // 只有 RCRGF
                    String[] part = s.split(",");
                    for (int i = 0; i < part.length - 1; i+=3) {
                        RE.add(Integer.valueOf(part[i]));
                        RA.add(Double.valueOf(part[i + 1]));
                        RR.add(Double.valueOf(part[i + 2]));
                    }
                } else if (algorithms.indexOf("RCRGF") < 0) {
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
                if (algorithms.indexOf("SubgraphIsomorphism") < 0 && algorithms.indexOf("RCRGF") >= 0) {
                    // 只有 RCRGF和Greedy
                    RE.add(Integer.valueOf(part[0]));
                    RA.add(Double.valueOf(part[1]));
                    RR.add(Double.valueOf(part[2]));
                    GE.add(Integer.valueOf(part[3]));
                    GA.add(Double.valueOf(part[4]));
                    GR.add(Double.valueOf(part[5]));
                } else if (algorithms.indexOf("RCRGF") < 0 && algorithms.indexOf("SubgraphIsomorphism") >= 0) {
                    // 只有SubgraphIsomorphism和Greedy
                    SE.add(Integer.valueOf(part[0]));
                    SA.add(Double.valueOf(part[1]));
                    SR.add(Double.valueOf(part[2]));
                    GE.add(Integer.valueOf(part[3]));
                    GA.add(Double.valueOf(part[4]));
                    GR.add(Double.valueOf(part[5]));
                } else if (algorithms.indexOf("RCRGF") < 0 && algorithms.indexOf("SubgraphIsomorphism") < 0){
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

    private void twoStageData(HttpServletRequest req, HttpServletResponse resp, Map<String, String> t, int id) throws ServletException, IOException {
        // 返回legend
        Map<String, Object> map = new HashMap<>();
        map.put("legend", t.get("algorithms"));
        map.put("total", t.get("time"));
        // 获取文件数据
        Path p = Paths.get("results/data/" + String.valueOf(id));
        RequestDataBase requestDataBase = new RequestDataBase();
        String algorithms = null;
        try {
            algorithms = Process.asUrlMap(requestDataBase.getArguments(id)).get("algorithms");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<String> AA, AB, S, D, N;
        if (algorithms.contains("AEF_Advance")) {
            AA = Files.readAllLines(p.resolve("simulation_aefAdvance.txt"));
            List<Object> tAA = parseContent(AA);
            map.put("AAE", tAA.get(0)); map.put("AAA", tAA.get(1)); map.put("AACR", tAA.get(2)); map.put("AASD", tAA.get(3));
        }
        if (algorithms.contains("AEF_Baseline")) {
            AB = Files.readAllLines(p.resolve("simulation_aefBaseline.txt"));
            List<Object> tAB = parseContent(AB);
            map.put("ABE", tAB.get(0)); map.put("ABA", tAB.get(1)); map.put("ABCR", tAB.get(2)); map.put("ABSD", tAB.get(3));
        }
        if (algorithms.contains("SubgraphIsomorphism")) {
            S = Files.readAllLines(p.resolve("simulation_subgraph.txt"));
            List<Object> tS = parseContent(S);
            map.put("SE", tS.get(0)); map.put("SA", tS.get(1)); map.put("SCR", tS.get(2)); map.put("SSD", tS.get(3));
        }
        if (algorithms.contains("D-ViNE_SP")) {
            D = Files.readAllLines(p.resolve("simulation_ViNE.txt"));
            List<Object> tD = parseContent(D);
            map.put("DE", tD.get(0)); map.put("DA", tD.get(1)); map.put("DCR", tD.get(2)); map.put("DSD", tD.get(3));
        }
        if (algorithms.contains("NRM")) {
            N = Files.readAllLines(p.resolve("simulation_NRM.txt"));
            List<Object> tN = parseContent(N);
            map.put("NE", tN.get(0)); map.put("NA", tN.get(1)); map.put("NCR", tN.get(2)); map.put("NSD", tN.get(3));
        }
        Process.setResp(resp, req.getHeader("origin"));
        PrintWriter out = resp.getWriter();
        out.println(JSON.toJSONString(map));
        out.close();
    }

    private List<Object> parseContent(List<String> content) {
        List<Integer> executionTime = new ArrayList<>();
        for (String p : content.get(0).split(" ")) {
            executionTime.add(Integer.valueOf(p));
        }
        List<Double> acceptanceRatio = new ArrayList<>();
        for (String p : content.get(1).split(" ")) {
            acceptanceRatio.add(Double.valueOf(p));
        }
        List<Double> costToRevenue = new ArrayList<>();
        for (String p : content.get(2).split(" ")) {
            costToRevenue.add(Double.valueOf(p));
        }
        List<Double> std = new ArrayList<>();
        for (String p : content.get(4).split(" ")) {
            std.add(Double.valueOf(p));
        }
        return Arrays.asList(executionTime, acceptanceRatio, costToRevenue, std);
    }
}
