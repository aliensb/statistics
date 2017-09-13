package com.zsmarter.framework.statistics.upload.service.imp;
import com.zsmarter.framework.statistics.upload.service.FileWriterService;
import com.zsmarter.framework.statistics.upload.utils.JsonUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FileWriterServiceImp implements FileWriterService {
    @Value("${parentPath}")
    private String parentPath;

    private String lineSeparator = System.getProperty("line.separator");

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void doWrite(String str) {
        Map<String, List<String>> parmas = convertData(str);
        Iterator<String> kIt = parmas.keySet().iterator();
        while (kIt.hasNext()) {
            String userId = kIt.next();
            List<String> values = parmas.get(userId);
            String data = convertListToString(values);
            writeToFile(userId,data);
        }
    }


    public Map<String, List<String>> convertData(String str) {
        String userId = "";
        List<JSONObject> lists = JsonUtils.paseJsonArray(str);
        Map<String, List<String>> param = new HashMap<>();
        Set<String> oids = new HashSet<>();
        for (int i = 0; i < lists.size(); i++) {
            userId = (String) JSONObject.fromObject(lists.get(i)).get("oid");
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


    public String convertListToString(List<String> values) {
        if(values==null||values.size()==0){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            stringBuilder.append(values.get(i) + lineSeparator);
        }
        return stringBuilder.toString();
    }


    private void writeToFile(String userId, String data) {
        FileChannel fileChannel = null;
        Set<OpenOption> openOptions = new HashSet<OpenOption>();
        openOptions.add(StandardOpenOption.CREATE);
        openOptions.add(StandardOpenOption.WRITE);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        File directory = new File(parentPath + File.separator + df.format(new Date()));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File userInfoFile = new File(directory.getPath() + File.separator + userId + ".txt");
        Path path = Paths.get(userInfoFile.toURI());
        try {
            if (!userInfoFile.exists()) {
                fileChannel = FileChannel.open(path, openOptions);
            } else {
                fileChannel = FileChannel.open(path, StandardOpenOption.APPEND);
            }
            byte[] bytes=data.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            fileChannel.write(buffer);

        } catch (Exception e) {
            logger.error("写人文件失败：userId:"+userId+"||数据:"+data);
            e.printStackTrace();
        } finally {
            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
