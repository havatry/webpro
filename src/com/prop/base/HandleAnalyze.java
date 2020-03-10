package com.prop.base;

import com.alibaba.fastjson.JSON;
import com.prop.bean.Page;
import com.prop.util.Process;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import vnreal.algorithms.AbstractAlgorithm;
import vnreal.algorithms.AlgorithmParameter;
import vnreal.algorithms.AvailableResources;
import vnreal.algorithms.CoordinatedMapping;
import vnreal.algorithms.isomorphism.SubgraphIsomorphismStackAlgorithm;
import vnreal.algorithms.myAEF.strategies.AEFAlgorithm;
import vnreal.algorithms.myAEF.strategies.NRMAlgorithm;
import vnreal.algorithms.myAdapter.RunAEFAdapter;
import vnreal.algorithms.myAdapter.SimulationRCRGFAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * �����ֶ������Ľӿ�
 */
@WebServlet("/analyze/handle")
public class HandleAnalyze extends HttpServlet{
    protected static final String UPLOAD_DIR = "upload";
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 100; // 100MB
    private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 120; // 120MB
    private static final int MEMORY_THRESHOLD = 1024 * 1024 * 30; // 30MB
    private RequestDataBase requestDataBase = new RequestDataBase();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> map = parseField(req);
        // ��ȡ�û���uid
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String uid = Process.parseUid(req, resp);
        String algorithm = (String) map.get("algorithm");
        // �����������ݿ�
        int current_id = 0;
        try {
            current_id = requestDataBase.insertRequest("�ֶ�����", algorithm, uid, sdf.format(new Date()));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Process.setResp(resp, req.getHeader("origin"));
        // ���ļ�д�뱾��
        String uploadPath = req.getServletContext().getRealPath("./") + File.separator + UPLOAD_DIR + File.separator + String.valueOf(current_id);
        String targetFile = uploadFile((FileItem) map.get("file"), uploadPath);
        // ���������ִ�в���
        try {
            requestDataBase.updateRequestArguments(current_id, "algorithm="+algorithm+"&file="+((FileItem)map.get("file")).getName());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        vnreal.algorithms.myRCRGF.util.Constants.WRITE_FILE =
                (vnreal.algorithms.myAEF.util.Constants.resultDir = "results/data" + File.separator + String.valueOf(current_id) + File.separator);
        // �����߳�ִ������, ���ظ�ǰ��һ������
        Task task = new Task(algorithm, targetFile, current_id);
        new Thread(task).start();
    }

    private ServletFileUpload upload0() {
        // �����ϴ�����
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(MEMORY_THRESHOLD); // ���ļ����ݳ���30MB ����ʱĿ¼�洢
        factory.setRepository(new File(System.getProperty("java.io.tmpdir"))); // ��ʱĿ¼����
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setFileSizeMax(MAX_FILE_SIZE); // �����ϴ��ļ����Ĵ�С
        upload.setSizeMax(MAX_REQUEST_SIZE); // ���������ϴ���С�������ļ�������������
        upload.setHeaderEncoding("UTF-8"); // ��������
        return upload;
    }

    private class Task implements Runnable {
        private String algorithm;
        private String filename;
        private Integer id;

        public Task(String algorithm, String filename, Integer id) {
            this.algorithm = algorithm;
            this.filename = filename;
            this.id = id;
        }

        private AlgorithmParameter initParam() {
            AlgorithmParameter algorithmParameter = new AlgorithmParameter();
            algorithmParameter.put("linkMapAlgorithm", "bfs");
            algorithmParameter.put("distanceConstraint", "70.0");
            algorithmParameter.put("advanced", "false");
            algorithmParameter.put("eppstein", "false");
            algorithmParameter.put("AEFAdvanced", "true");
            return algorithmParameter;
        }

        @Override
        public void run() {
            AlgorithmParameter parameter = initParam();
            AbstractAlgorithm abstractAlgorithm = null;
            switch (algorithm) {
                case "AEF_Baseline":
                    abstractAlgorithm = new AEFAlgorithm(parameter, false);
                    break;
                case "AEF_Advance":
                    abstractAlgorithm = new AEFAlgorithm(parameter, true);
                    break;
                case "RCRGF":
                    SimulationRCRGFAdapter simulationRCRGFAdapter = new SimulationRCRGFAdapter();
                    // ִ����ɸ������ݿ��¼
                    updateDBStatus(id, simulationRCRGFAdapter.doRCRGF(filename, id, "�ֶ�����"));
                    System.out.println("task ִ�����");
                    return;
                case "SubgraphIsomorphism":
                    abstractAlgorithm = new SubgraphIsomorphismStackAlgorithm(parameter);
                    break;
                case "NRM":
                    abstractAlgorithm = new NRMAlgorithm(parameter);
                    break;
                case "D-ViNE_SP":
                    abstractAlgorithm = new CoordinatedMapping(parameter);
                    break;
                case "Greedy":
                    abstractAlgorithm = new AvailableResources(parameter);
                    break;
            }
            RunAEFAdapter runAEFAdapter = new RunAEFAdapter();
            runAEFAdapter.process(abstractAlgorithm, filename, id, "�ֶ�����", 50);
            // ִ����ɺ� �޸����ݿ�ļ�¼
            updateDBStatus(id, abstractAlgorithm.getStati().get(0).getRatio() == 100);
            System.out.println("task ִ�����");
        }
    }

    private boolean updateDBStatus(Integer id, boolean succ) {
        try {
            return requestDataBase.updateRequest(id, succ ? "ӳ��ɹ�" : "ӳ��ʧ��");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Map<String, Object> parseField(HttpServletRequest request) {
        ServletFileUpload upload = upload0();
        Map<String, Object> map = new HashMap<>();
        try {
            List<FileItem> formsItems = upload.parseRequest(request);
            for (FileItem item : formsItems){
                if (item.isFormField()) {
                    map.put(item.getFieldName(), new String(item.getString().getBytes("ISO8859-1"), "UTF-8"));
                } else {
                    map.put(item.getFieldName(), item);
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return map;
    }

    // �ϴ�������ļ���uploadĿ¼��
    private String uploadFile(FileItem item, String uploadPath) {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        // �������е��ļ�
        String fileName = new File(item.getName()).getName();
        String filePath = uploadPath + File.separator + fileName;
        File storeFile = new File(filePath);
        System.out.println("�������ļ�д�� " + filePath + " ��");
        // �����ļ���Ӳ��
        try {
            item.write(storeFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
