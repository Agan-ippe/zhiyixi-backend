package com.zhiyixi.config;

import io.github.briqt.spark4j.SparkClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/09/23   18:09
 * @Version 1.0
 * @Description 讯飞星火大模型配置类
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "xing-huo.client")
public class XingHuoConfig {

    private String appId;

    private String apiSecret;

    private String apiKey;

    @Bean
    public SparkClient sparkClient() {
        SparkClient sparkClient = new SparkClient();
        sparkClient.apiKey = apiKey;
        sparkClient.apiSecret = apiSecret;
        sparkClient.appid = appId;
        return sparkClient;
    }
}
