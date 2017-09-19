package com.zsmarter.framework.statistics.upload.controller;

import com.zsmarter.framework.statistics.upload.service.FileWriterService;
import com.zsmarter.framework.statistics.upload.service.bio.FileWriterServiceBio;
import com.zsmarter.framework.statistics.upload.service.common.CommonService;
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
import java.util.HashMap;
import java.util.Map;

@Controller
public class UploadController {
    @Autowired
    private CommonService  commonService;
    @Autowired
    private FileWriterServiceBio fileWriterServiceBio;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostMapping("/upload")
    public @ResponseBody
    Map<String,String> index(@RequestBody String params){
        Map<String,String> retMsg=new HashMap<>();

        if(StringUtil.isBlank(params)){
            logger.error("参数不能为空");
            retMsg.put("code","01");
            return retMsg;
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
                retMsg.put("code","01");
                return retMsg;
            }
        }
        if("00".equals(commonService.doWrite(params))){
            retMsg.put("code","00");
            return retMsg;
        }else{
            retMsg.put("code","01");
            return retMsg;
        }

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
