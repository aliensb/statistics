package com.zsmarter.framework.statistics.upload.config;

import com.zsmarter.framework.statistics.upload.service.bio.FileWriterHandlerExecutePool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig {
    @Value("${framework.maxPoolSize}")
    private int maxPoolSize;
    @Value("${framework.queueSize}")
    private int queueSize;
    @Value("${allowedOrigi}")
    private String allowedOrigin;
    @Value("${allowedHeade}")
    private String allowedHeade;
    @Value("${allowedMethod}")
    private String allowedMethod;

    @Bean
    public FileWriterHandlerExecutePool fileWriterHandlerExecutePool(){
        return new FileWriterHandlerExecutePool(maxPoolSize,queueSize);
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin(allowedOrigin);
        corsConfiguration.addAllowedHeader(allowedHeade);
        corsConfiguration.addAllowedMethod(allowedMethod);
        return corsConfiguration;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);
    }

}
