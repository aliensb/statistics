package com.zsmarter.framework.statistics.upload.service.bio.imp;

import com.zsmarter.framework.statistics.upload.service.FileWriterService;
import com.zsmarter.framework.statistics.upload.service.bio.FileWriterHandler;
import com.zsmarter.framework.statistics.upload.service.bio.FileWriterHandlerExecutePool;
import com.zsmarter.framework.statistics.upload.service.bio.FileWriterServiceBio;
import com.zsmarter.framework.statistics.upload.service.common.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FileWriterServiceBioImp implements FileWriterServiceBio {
    @Value("${parentPath}")
    private String parentPath;


    @Autowired
    private FileWriterHandlerExecutePool pool;
    @Autowired
    private CommonService commonService;

    @Override
    public void write(String str) {
        String lineSeparator=System.getProperty("line.separator");
        str+=lineSeparator;//回车符
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        File directory=new File(parentPath+File.separator+df.format(new Date()));
        if(!directory.exists()){
            directory.mkdirs();
        }
        Map<String,List<String>> params= commonService.convertData(str);
        pool.execute(new FileWriterHandler(params,directory));
    }


}
