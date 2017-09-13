package com.zsmarter.framework.statistics.upload.controller;

import com.zsmarter.framework.statistics.upload.service.FileWriterService;
import com.zsmarter.framework.statistics.upload.service.bio.FileWriterServiceBio;
import com.zsmarter.framework.statistics.upload.utils.StringUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UploadController {
    @Autowired
    private FileWriterService fileWriterService;
    @Autowired
    private FileWriterServiceBio fileWriterServiceBio;
    private static final Logger logger=Logger.getLogger(UploadController.class);
    @PostMapping("/upload")
    public @ResponseBody String index(@RequestBody String params){
        if(StringUtil.isBlank(params)){
            logger.error("参数不能为空");
        }
        fileWriterService.doWrite(params);
        return "ok";
    }
    @PostMapping("/uploadbio")
    public @ResponseBody String uploadWithBio(@RequestBody String params){
        if(StringUtil.isBlank(params)){
            logger.error("参数不能为空");
        }
        fileWriterServiceBio.write(params);
        return "ok";
    }
}
