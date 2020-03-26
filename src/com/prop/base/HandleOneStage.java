package com.prop.base;

import com.prop.util.Process;
import com.prop.util.RequestDataBase;
import vnreal.algorithms.myAdapter.SimulationRCRGFAdapter;
import vnreal.algorithms.myAdapter.ToGenRCRGFAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class HandleOneStage extends HttpServlet{
    private RequestDataBase requestDataBase = new RequestDataBase();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int id = writeRequestToDB(req, resp);
        Process.setResp(resp, req.getHeader("origin"));
        resp.getWriter().close(); // 返回前端数据
        autoGenerate(req); // 生成拓扑
        String[] part = req.getParameter("nodes").split("-");
        Thread t = new Thread(new Task(req.getParameterValues("algorithms"), part,
                Double.valueOf(req.getParameter("resourceRatio")),
                        Double.valueOf(req.getParameter("nodeRatio")), id));
        t.start(); // 执行计算
    }

    private class Task implements Runnable {
        private List<String> algorithms;
        private int min;
        private int maxExclude;
        private int step;
        private SimulationRCRGFAdapter simulationRCRGFAdapter;
        private double resourceRatio;
        private double nodeRatio;
        private int id;
        private String type;

        public Task(String[] algorithms, String[] part, double resourceRatio, double nodeRatio, int id) {
            this.algorithms = Arrays.asList(algorithms);
            this.min = Integer.valueOf(part[0]);
            this.maxExclude = Integer.valueOf(part[1]);
            this.step = Integer.valueOf(part[2]);
            this.resourceRatio = resourceRatio;
            this.nodeRatio = nodeRatio;
            this.id = id;
            this.type = "一阶段实验";
            simulationRCRGFAdapter = new SimulationRCRGFAdapter();
        }

        @Override
        public void run() {
            for (int i = min; i < maxExclude; i+=step) {
                String filename = "topology_" + i + "_" +  resourceRatio + "_" + nodeRatio + ".xml";
                if (algorithms.contains("RCRGF")) {
                    simulationRCRGFAdapter.doRCRGF(filename, id, type);
                }
                if (algorithms.contains("Greedy")) {
                    simulationRCRGFAdapter.doGreedy(filename, id, type);
                }
                if (algorithms.contains("subgrapIsomorphism")) {
                    simulationRCRGFAdapter.doSubgraph(filename, id, type);
                }
            }
            // 计算后更新数据库状态
            try {
                requestDataBase.updateRequest(id, "执行完成");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("一阶段实验task 执行完成");
        }
    }

    private int writeRequestToDB(HttpServletRequest req, HttpServletResponse resp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uid = Process.parseUid(req, resp);
        String[] algorithms = req.getParameterValues("algorithms");
        Optional<String> optional = Arrays.stream(algorithms).reduce((a, b) -> a + "," + b);
        int current_id = 0;
        try {
            current_id = requestDataBase.insertRequest("一阶段实验", optional.get(), uid, sdf.format(new Date()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return current_id;
    }

    private void autoGenerate(HttpServletRequest req) throws IOException {
        // 获取参数 生成测试用例
        String snodes = req.getParameter("nodes");
        String[] part = snodes.split("-");
        int min = Integer.valueOf(part[0]);
        int maxExclude = Integer.valueOf(part[1]);
        int step = Integer.valueOf(part[2]);
        double resourceRatio = Double.valueOf(req.getParameter("resourceRatio"));
        double nodeRatio = Double.valueOf(req.getParameter("nodeRatio"));
        ToGenRCRGFAdapter toGenRCRGFAdapter = new ToGenRCRGFAdapter();
        for (int i = min; i < maxExclude; i+=step) {
            toGenRCRGFAdapter.generateFile(i, resourceRatio, nodeRatio);
        }
    }
}
