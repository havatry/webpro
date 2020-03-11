package com.prop.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 打包zip工具类
 */
public class ZipWrite {
    private final static int BUFFER_SIZE=1024;

    public static void compress(File source, ZipOutputStream zos, String name, boolean KeepDirStructure) throws IOException {
        byte[] buffer=new byte[BUFFER_SIZE];
        if(source.isFile()){//文件操作
            //向zip输出流中加入zip实体
            zos.putNextEntry(new ZipEntry(name));//以name创建压缩文件
            //copy文件
            int len;
            FileInputStream in=new FileInputStream(source);
            while((len=in.read(buffer))!=-1){
                zos.write(buffer,0,len);
            }
            //complete
            in.close();
            zos.closeEntry();
        }else{//目录操作
            File[] files=source.listFiles();
            if(files==null || files.length==0){
                //空目录
                if(KeepDirStructure){
                    //保持目录结构
                    zos.putNextEntry(new ZipEntry(name+"/"));
                    zos.closeEntry();
                }
            }else{
                //目录下有文件
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
