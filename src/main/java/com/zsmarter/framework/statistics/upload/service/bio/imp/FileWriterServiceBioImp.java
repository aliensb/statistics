package com.zsmarter.framework.statistics.upload.service.bio.imp;

import com.zsmarter.framework.statistics.upload.service.bio.FileWriterHandler;
import com.zsmarter.framework.statistics.upload.service.bio.FileWriterHandlerExecutePool;
import com.zsmarter.framework.statistics.upload.service.bio.FileWriterServiceBio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
@Service
public class FileWriterServiceBioImp implements FileWriterServiceBio {
    @Value("${parentPath}")
    private String parentPath;
    @Autowired
    private FileWriterHandlerExecutePool pool;
    @Override
    public void write(String str) {
        String userId="10086";
        String lineSeparator=System.getProperty("line.separator");
        str+=lineSeparator;//回车符
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        File directory=new File(parentPath+File.separator+df.format(new Date()));
        if(!directory.exists()){
            directory.mkdirs();
        }
        File userInfoFile=new File(directory.getPath()+File.separator+userId+".txt");
        pool.execute(new FileWriterHandler(str,userInfoFile));
    }
}
