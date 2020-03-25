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
        writeRequestToDB(req, resp);
        Process.setResp(resp, req.getHeader("origin"));
        resp.getWriter().close(); // 返回前端数据
        autoGenerate(req); // 生成拓扑

    }

    private class Task implements Runnable {
        private List<String> algorithms;
        private int min;
        private int maxExclude;
        private int step;
        private SimulationRCRGFAdapter simulationRCRGFAdapter;

        public Task(String[] algorithms, String[] part) {
            this.algorithms = Arrays.asList(algorithms);
            this.min = Integer.valueOf(part[0]);
            this.maxExclude = Integer.valueOf(part[1]);
            this.step = Integer.valueOf(part[2]);
            simulationRCRGFAdapter = new SimulationRCRGFAdapter();
        }

        @Override
        public void run() {
            for (int i = min; i < maxExclude; i+=step) {
                String filename; // 修改原jar文件
            }
            // 对于每种算法进行计算
            if (algorithms.contains("RCRGF")) {
//                simulationRCRGFAdapter.doRCRGF()
            }
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
