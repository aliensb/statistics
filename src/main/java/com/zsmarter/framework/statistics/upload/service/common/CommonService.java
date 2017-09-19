package com.zsmarter.framework.statistics.upload.service.common;

import java.util.List;
import java.util.Map;

public interface CommonService {
    String doWrite(String str);

    Map<String, List<String>> convertData(String str);

    String convertListToString(List<String> values);
}
