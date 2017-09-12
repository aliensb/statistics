package com.zsmarter.framework.statistics.upload.service.bio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriterHandler implements Runnable {
    private String str;
    private File path;

    public FileWriterHandler(String str,File path) {
        this.str = str;
        this.path=path;
    }

    @Override
    public void run() {
        BufferedWriter wirter=null;
        try {
            wirter=new BufferedWriter(new FileWriter(path,true));
            wirter.write(str);
            wirter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if(wirter!=null){
                try {
                    wirter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
