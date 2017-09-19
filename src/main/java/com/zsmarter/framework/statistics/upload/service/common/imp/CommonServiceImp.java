package com.zsmarter.framework.statistics.upload.service.common.imp;

import com.zsmarter.framework.statistics.upload.service.FileWriterService;
import com.zsmarter.framework.statistics.upload.service.common.CommonService;
import com.zsmarter.framework.statistics.upload.utils.JsonUtils;
import com.zsmarter.framework.statistics.upload.utils.StringUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CommonServiceImp implements CommonService {
    private String lineSeparator = System.getProperty("line.separator");

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //在此配置是使用aio还是nio
    @Autowired()
    @Qualifier("nioFileWriterService")
    private FileWriterService fileWriterService;
    @Override
    public String doWrite(String str) {
        //将前端传过来的参数转为一个userid对象一个list的数据集合
        Map<String, List<String>> parmas = convertData(str);
        if(parmas==null){
            return "01";
        }
        Iterator<String> kIt = parmas.keySet().iterator();
        String retCode="00";
        while (kIt.hasNext()) {
            String userId = kIt.next();
            List<String> values = parmas.get(userId);
            //将数据集合转为string
            String data = convertListToString(values);
            if("01".equals(data)){
                return "01";
            }
            if("01".equals(fileWriterService.writeToFile(userId,data))){
                return "01";
            }
        }
        return retCode;
    }


    @Override
    public Map<String, List<String>> convertData(String str) {
        String userId = "";
        List<JSONObject> lists = null;
        try{
            lists= JsonUtils.paseJsonArray(str);
        }catch (Exception e){
            logger.error("参数转化为json数组出错,参数："+str);
            return null;
        }
        Map<String, List<String>> param = new HashMap<>();
        Set<String> oids = new HashSet<>();
        for (int i = 0; i < lists.size(); i++) {
            if(null==lists.get(i)){
                continue;
            }
            userId = (String) JSONObject.fromObject(lists.get(i)).get("oid");
            if(StringUtil.isBlank(userId)){
                continue;
            }
            if (oids.add(userId)) {
                List<String> values = new ArrayList<>();
                values.add((lists.get(i)).toString());
                param.put(userId, values);
            } else {
                param.get(userId).add((lists.get(i)).toString());
            }
        }
        oids.clear();
        return param;
    }


    @Override
    public String convertListToString(List<String> values) {
        if(values==null||values.size()==0){
            return "01";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            stringBuilder.append(values.get(i) + lineSeparator);
        }
        return stringBuilder.toString();
    }

}
