package com.zsmarter.framework.statistics.upload.config;

import com.zsmarter.framework.statistics.upload.service.bio.FileWriterHandlerExecutePool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    @Value("${framework.maxPoolSize}")
    private int maxPoolSize;
    @Value("${framework.queueSize}")
    private int queueSize;
    @Bean
    public FileWriterHandlerExecutePool fileWriterHandlerExecutePool(){
        return new FileWriterHandlerExecutePool(maxPoolSize,queueSize);
    }
}
