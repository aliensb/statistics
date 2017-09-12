package com.zsmarter.framework.statistics.upload.utils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    public static List paseJsonArray(String jsArrayStr){
        if ((StringUtil.isBlank(jsArrayStr))||jsArrayStr.equals("[]")) {
            return new ArrayList();
        }
        JSONArray localJsArray=JSONArray.fromObject(jsArrayStr);

        List list=(List)JSONArray.toList(localJsArray,new JSONObject(),new JsonConfig());
        return list;
    }
}
