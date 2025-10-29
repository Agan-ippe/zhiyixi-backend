package com.zhiyixi.manager;

import com.zhiyixi.common.ErrorCode;
import com.zhiyixi.exception.BusinessException;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/10/26   17:54
 * @Version 1.0
 * @Description RedisLimiter 通用的基础限流服务
 */
@Service
public class RedisLimiterManager {

    @Resource
    private RedissonClient redissonClient;

    public void doRateLimiter(String key) {
        // 为每个用户创建独立的限流器
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(key);

        // 设置限流规则：每秒5次
        rateLimiter.trySetRate(RateType.OVERALL, 5, 1, RateIntervalUnit.SECONDS);

        // 请求一个令牌
        boolean canOp = rateLimiter.tryAcquire(1);
        if (!canOp) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, "操作过于频繁，请稍后再试");
        }
    }
}
