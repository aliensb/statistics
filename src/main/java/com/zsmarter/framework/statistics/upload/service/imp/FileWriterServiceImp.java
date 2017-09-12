package com.zsmarter.framework.statistics.upload.service.imp;

import com.zsmarter.framework.statistics.upload.service.FileWriterService;

import com.zsmarter.framework.statistics.upload.utils.JsonUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sun.nio.ch.FileChannelImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    @Override
    public void wireFile(String str) {
        String userId="10086";
        String lineSeparator=System.getProperty("line.separator");
        str+=lineSeparator;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        File directory=new File(parentPath+File.separator+df.format(new Date()));
        if(!directory.exists()){
            directory.mkdirs();
        }
        Set<OpenOption> openOptions=new HashSet<OpenOption>();
        openOptions.add(StandardOpenOption.CREATE);
        openOptions.add(StandardOpenOption.WRITE);

       // FileOutputStream outputStream=null;
        FileChannel fileChannel=null;
        List<String> lists= JsonUtils.paseJsonArray(str);
        Map<String,List<JSONObject>> param=new HashMap<>();
        Set<String> oid=new HashSet<>();
        for(int i=0;i<lists.size();i++){
            userId= (String) JSONObject.fromObject(lists.get(i)).get("oid");
            if(oid.add(userId)){
                List<JSONObject> values=new ArrayList<>();
                values.add(JSONObject.fromObject(lists.get(i)));
                param.put(userId,values);
            }else {
                param.get(userId).add(JSONObject.fromObject(lists.get(i)));
            }
        }

        try{
            //outputStream=new FileOutputStream(userInfoFile,true);
            //fileChannel=outputStream.getChannel();
           // fileChannel=FileChannel.open(u)
            for(int i=0;i<lists.size();i++){

                File userInfoFile=new File(directory.getPath()+File.separator+userId+".txt");
                //File userInfoFile=new File(userId+".txt");
                Path path= Paths.get(userInfoFile.toURI());
                if(!userInfoFile.exists()){
                    fileChannel= FileChannel.open(path, openOptions);
                    //fileChannel=FileChannel.open(path, StandardOpenOption.APPEND);
                }else {
                    fileChannel=FileChannel.open(path, StandardOpenOption.APPEND);
                }
                ByteBuffer buffer=ByteBuffer.wrap(lists.get(i).getBytes());
                buffer.flip();
                fileChannel.write(buffer);
                buffer.clear();
            }
        }catch (Exception e) {
            e.printStackTrace();
            return;
        }
        finally {
            if(fileChannel!=null){
                try {
                    fileChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            if(outputStream!=null){
//                try {
//                    outputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }
}
