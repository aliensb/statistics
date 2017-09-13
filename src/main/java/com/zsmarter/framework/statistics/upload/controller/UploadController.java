package com.zsmarter.framework.statistics.upload.controller;

import com.zsmarter.framework.statistics.upload.service.FileWriterService;
import com.zsmarter.framework.statistics.upload.service.bio.FileWriterServiceBio;
import com.zsmarter.framework.statistics.upload.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

@Controller
public class UploadController {
    @Autowired
    private FileWriterService fileWriterService;
    @Autowired
    private FileWriterServiceBio fileWriterServiceBio;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostMapping("/upload")
    public @ResponseBody String index(@RequestBody String params){
        if(StringUtil.isBlank(params)){
            logger.error("参数不能为空");
        }
        if(!params.trim().startsWith("[")){
            try {
                params=URLDecoder.decode(params.trim(),"utf-8");
                if(params.endsWith("=")){
                    params=params.substring(0,params.lastIndexOf('='));
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("参数错误："+params);
                e.printStackTrace();
            }
        }
        fileWriterService.doWrite(params);
        return "ok";
    }
    @PostMapping("/uploadbio")
    public @ResponseBody String uploadWithBio(@RequestBody String params){
        if(StringUtil.isBlank(params)){
            logger.error("参数不能为空");
        }
        if(!params.trim().startsWith("[")){
            try {
                params=URLDecoder.decode(params.trim(),"utf-8");
                if(params.endsWith("=")){
                    params=params.substring(0,params.lastIndexOf('='));
                }
            } catch (UnsupportedEncodingException e) {
                logger.error("参数错误："+params);
            }
        }

        fileWriterServiceBio.write(params);
        return "ok";
    }
}
