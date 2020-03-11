package com.prop.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ���zip������
 */
public class ZipWrite {
    private final static int BUFFER_SIZE=1024;

    public static void compress(File source, ZipOutputStream zos, String name, boolean KeepDirStructure) throws IOException {
        byte[] buffer=new byte[BUFFER_SIZE];
        if(source.isFile()){//�ļ�����
            //��zip������м���zipʵ��
            zos.putNextEntry(new ZipEntry(name));//��name����ѹ���ļ�
            //copy�ļ�
            int len;
            FileInputStream in=new FileInputStream(source);
            while((len=in.read(buffer))!=-1){
                zos.write(buffer,0,len);
            }
            //complete
            in.close();
            zos.closeEntry();
        }else{//Ŀ¼����
            File[] files=source.listFiles();
            if(files==null || files.length==0){
                //��Ŀ¼
                if(KeepDirStructure){
                    //����Ŀ¼�ṹ
                    zos.putNextEntry(new ZipEntry(name+"/"));
                    zos.closeEntry();
                }
            }else{
                //Ŀ¼�����ļ�
                for(File file:files){
                    if(KeepDirStructure){
                        compress(file, zos, name+"/"+file.getName(), KeepDirStructure);
                    }else{
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }
}
