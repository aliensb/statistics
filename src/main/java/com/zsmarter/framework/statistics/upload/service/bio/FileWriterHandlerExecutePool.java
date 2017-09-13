package com.zsmarter.framework.statistics.upload.service.bio;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FileWriterHandlerExecutePool {;
    private ExecutorService executor;
    private int maxPoolSize;
    private int queueSize;


    public FileWriterHandlerExecutePool(int maxPoolSize,int queueSize) {

        this.maxPoolSize=maxPoolSize;
        this.queueSize=queueSize;
        executor=new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),maxPoolSize,120L, TimeUnit.SECONDS
                ,new ArrayBlockingQueue<Runnable>(queueSize));
    }
    public void execute(Runnable task){
        executor.execute(task);
    }
}
