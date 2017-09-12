package com.zsmarter.framework.statistics.upload.service.bio;

import com.zsmarter.framework.statistics.upload.controller.UploadController;
import org.apache.log4j.Logger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FileWriterHandlerExecutePool {;
    private ExecutorService executor;
    private int maxPoolSize;
    private int queueSize;
    private static final Logger logger=Logger.getLogger(UploadController.class);

    public FileWriterHandlerExecutePool(int maxPoolSize,int queueSize) {
        System.out.println(maxPoolSize+"::::"+queueSize);
        this.maxPoolSize=maxPoolSize;
        this.queueSize=queueSize;
        logger.info("Runtime.getRuntime().availableProcessors("+Runtime.getRuntime().availableProcessors());
        executor=new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,120L, TimeUnit.SECONDS
                ,new ArrayBlockingQueue<Runnable>(queueSize));
    }
    public void execute(Runnable task){
        executor.execute(task);
    }
}
