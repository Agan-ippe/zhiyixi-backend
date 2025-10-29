package com.zhiyixi.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/10/23   18:19
 * @Version 1.0
 * @Description redisson配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.redisson")
public class RedissonConfig {
    /**
     * 主机
     */
    private String host;
    /**
     * 端口
     */
    private Integer port;

    /**
     * 密码
     */
    private String password;

    /**
     * 数据库
     */
    private Integer database;

    @Bean
    public RedissonClient redissonClient() {
        String redisAddress = String.format("redis://%s:%s", host, port);
        // 创建配置
        Config config = new Config();
        config.useSingleServer()
                .setAddress(redisAddress)
                .setPassword(password)
                .setDatabase(database);
        config.setCodec(new StringCodec());
        /// useClusterServers() //集群
        /// .addNodeAddress("redis://127.0.0.1:7181");
        // Sync and Async API
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
