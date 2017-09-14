package com.zsmarter.framework.statistics.upload.service;

import java.util.List;
import java.util.Map;

public interface FileWriterService {
    String doWrite(String str);
    Map<String, List<String>> convertData(String str);
    String convertListToString(List<String> values);

}
