package com.zsmarter.framework.statistics.upload.service.nio;
import com.zsmarter.framework.statistics.upload.service.FileWriterService;
import com.zsmarter.framework.statistics.upload.utils.StringUtil;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service("nioFileWriterService")
public class NioFileWriterServiceImp implements FileWriterService {
    @Value("${parentPath}")
    private String parentPath;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public String writeToFile(String userId, String data) {
        if(StringUtil.isBlank(userId)||StringUtil.isBlank(data)||"{}".equals(data)||"[]".equals(data)){
            return "01";
        }
        FileChannel fileChannel = null;
        Set<OpenOption> openOptions = new HashSet<>();
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
            return "01";
        } finally {
            if (fileChannel != null) {
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "00";
    }
}
