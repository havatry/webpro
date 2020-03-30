package com.prop.base;

import com.prop.util.Process;
import com.prop.util.RequestDataBase;
import vnreal.algorithms.AlgorithmParameter;
import vnreal.algorithms.CoordinatedMapping;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismStackAlgorithm;
import vnreal.algorithms.myAEF.strategies.AEFAlgorithm;
import vnreal.algorithms.myAEF.strategies.NRMAlgorithm;
import vnreal.algorithms.myAEF.util.Constants;
import vnreal.algorithms.myAdapter.ProduceCaseAEFAdapter;
import vnreal.algorithms.myAdapter.RunAEFAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

@WebServlet("/handle/twostage")
public class HandleTwoStage extends HttpServlet{
    private RequestDataBase requestDataBase = new RequestDataBase();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取参数
        String[] algorithm = req.getParameterValues("algorithms");
//        double resource = Double.valueOf(req.getParameter("resource"));
        String[] res = req.getParameter("resource").split("-");
        double resourceBase = Double.valueOf(res[0]);
        double resourceInc = Double.valueOf(res[1]) - resourceBase;
        double alpha = Double.valueOf(req.getParameter("alpha")); // 底层alpha
        double beta = Double.valueOf(req.getParameter("beta"));
        double arrive = Process.parseFraction(req.getParameter("arrive"));
        double presever = Process.parseFraction(req.getParameter("presever"));
        int total = Integer.valueOf(req.getParameter("time"));

        // 插入到数据库
        Optional<String> optional = Arrays.stream(algorithm).reduce((a, b) -> a + "," + b);
        int id = Process.writeRequestToDB(req, resp, "二阶段协调实验", optional.get());

        // 设置上下文参数
        String arguments = "algorithms=" + optional.get() + "&resource=" + req.getParameter("resource") +
                "&alpha=" + alpha + "&beta=" + beta + "&arrive=" + req.getParameter("arrive")
                + "&presever=" + req.getParameter("presever") + "&time=" + total;
        try {
            requestDataBase.updateRequestArguments(id, arguments);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 设置CORS
        Process.setResp(resp, req.getHeader("origin"));

        // 设置路径
        Constants.resultDir = "results/data/" + String.valueOf(id) + "/";
        Constants.FILE_NAME = Constants.FILE_NAME.replace("file", "data/" + String.valueOf(id));
        if (!Files.exists(Paths.get(Constants.resultDir))) {
            Files.createDirectory(Paths.get(Constants.resultDir));
        }
        // 执行算法
        Thread t = new Thread(() -> {
            // 自动生成测试
            System.out.println("自动生成文件");
            ProduceCaseAEFAdapter produceCaseAEFAdapter = new ProduceCaseAEFAdapter();
            String auxFileName = produceCaseAEFAdapter.generateFile(arrive, presever, total, resourceBase, resourceInc, alpha, beta);
            System.out.println("实验分析");
            // 然后进行测试
              // 获取文件名
            String fileName = auxFileName.replace("_aux.txt", ".xml");
//            RunAEFAdapter runAEFAdapter = new RunAEFAdapter();
            String algorithmFlat = optional.get();
            AlgorithmParameter parameter = RunAEFAdapter.initParam();
            if (algorithmFlat.indexOf("AEF_Advance") > 0) {
                new RunAEFAdapter().process(new AEFAlgorithm(parameter, true),
                        fileName, id, "二阶段协调实验", total);
            }
            if (algorithmFlat.indexOf("AEF_Baseline") > 0 ){
                new RunAEFAdapter().process(new AEFAlgorithm(parameter, false),
                        fileName, id, "二阶段协调实验", total);
            }
            if (algorithmFlat.indexOf("D-ViNE_SP") > 0) {
                new RunAEFAdapter().process(new CoordinatedMapping(parameter), fileName,
                        id, "二阶段协调实验", total);
            }
            if (algorithmFlat.indexOf("subgrapIsomorphism") > 0) {
                new RunAEFAdapter().process(new SubgraphIsomorphismStackAlgorithm(parameter), fileName,
                        id, "二阶段协调实验", total);
            }
            if (algorithmFlat.indexOf("NRM") > 0) {
                new RunAEFAdapter().process(new NRMAlgorithm(parameter), fileName, id, "二阶段协调实验", total);
            }
            // 计算后更新数据库状态
            try {
                requestDataBase.updateRequest(id, "执行完成");
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println("二阶段协调实验task 执行完成");
        });
        t.start();
    }
}
