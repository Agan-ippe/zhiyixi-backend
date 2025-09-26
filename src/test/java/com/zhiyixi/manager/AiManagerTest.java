package com.zhiyixi.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/09/26   18:22
 * @Version 1.0
 * @Description
 */
@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;

    @

    /**
     * 测试 AI 功能
     */
    void domain() {
        String answer = aiManager.sendMsgToXingHuo(true, "分析需求：\n" +
                "分析网站用户的增长情况\n" +
                "原始数据：\n" +
                "日期,用户数\n" +
                "1号,10\n" +
                "2号,20\n" +
                "3号,30\n");
        System.out.println(answer);
    }
}