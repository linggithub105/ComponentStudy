package com.itjing.minio.config;

import io.minio.MinioClient;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lijing
 * @date 2022年06月14日 19:22
 * @description Minio配置
 */
@Configuration
public class MinioConfig {

    /**
     * 连接地址
     */
    @Value("${minio.endpoint}")
    @Getter
    private String endpoint;

    /**
     * 用户名
     */
    @Value("${minio.accessKey}")
    @Getter
    private String accessKey;

    /**
     * 密码
     */
    @Value("${minio.secretKey}")
    @Getter
    private String secretKey;

    /**
     * 域名
     */
    @Value("${minio.nginxHost}")
    @Getter
    private String nginxHost;

    /**
     * 存储空间名称
     * @return
     */
    @Value("${minio.bucketName}")
    @Getter
    private String bucketName;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }

}
