package com.zhiyixi.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/10/28   16:55
 * @Version 1.0
 * @Description
 */
@SpringBootTest
class RedisLimiterManagerTest {
    @Resource
    private RedisLimiterManager redisLimiterManager;

    @Test
    void doRateLimit() throws InterruptedException {
        String userId = "123";
        for (int i = 0; i < 5; i++) {
            redisLimiterManager.doRateLimiter(userId);
            System.out.println("成功");
        }
        Thread.sleep(1000);
        for (int i = 0; i < 10; i++) {
            redisLimiterManager.doRateLimiter(userId);
            System.out.println("成功");
        }
    }
}