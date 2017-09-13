package com.zsmarter.framework.statistics.upload.service.bio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileWriterHandler implements Runnable {
    private Map<String,List<String>> params;
    private File path;
    private String lineSeparator = System.getProperty("line.separator");
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public FileWriterHandler(Map<String,List<String>> params,File path) {
        this.params = params;
        this.path=path;
    }

    @Override
    public void run() {
            Iterator<String> keyIt=params.keySet().iterator();
            while (keyIt.hasNext()){
                String userId=keyIt.next();
               List<String> lists=params.get(userId);
               String values=convertListToString(lists);
               this.writeToFile(userId,values);
            }
    }
    private void writeToFile(String userId,String data){
        BufferedWriter writer=null;
        try {
            writer=new BufferedWriter(new FileWriter(path.getPath()+File.separator+userId+".txt",true));
            writer.write(data);
        }catch (Exception e){
            logger.error("写人文件失败,userId:"+userId+"||数据："+data);
            e.printStackTrace();
        }
        finally {
            if(writer!=null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private String convertListToString(List<String> values) {
        if(values==null||values.size()==0){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            stringBuilder.append(values.get(1) + lineSeparator);
        }
        return stringBuilder.toString();
    }
}
