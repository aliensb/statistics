package com.zsmarter.framework.statistics.upload.service.aio;

import com.zsmarter.framework.statistics.upload.service.FileWriterService;
import com.zsmarter.framework.statistics.upload.utils.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
@Service("aioFileWriterService")
public class AioFileWriterServiceImp implements FileWriterService {
    @Value("${parentPath}")
    private String parentPath;
    public String writeToFile(String userId, String data){
        if(StringUtil.isBlank(userId)||StringUtil.isBlank(data)||"{}".equals(data)||"[]".equals(data)){
            return "01";
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        File directory = new File(parentPath + File.separator + df.format(new Date()));
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File userInfoFile = new File(directory.getPath() + File.separator + userId + ".txt");
        Path path = Paths.get(userInfoFile.toURI());
        AsynchronousFileChannel fileChannel=null;
        try {
            fileChannel=AsynchronousFileChannel.open(path,
                    StandardOpenOption.CREATE,StandardOpenOption.WRITE);
            AsyncAppender appender=new AsyncAppender(fileChannel);
            byte[] bytes=data.getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            buffer.put(bytes);
            buffer.flip();
            appender.append(buffer);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "00";
    }

}
