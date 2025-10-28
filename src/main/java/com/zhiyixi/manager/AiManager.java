package com.zhiyixi.manager;

import com.zhiyixi.common.ErrorCode;
import com.zhiyixi.exception.BusinessException;
import io.github.briqt.spark4j.SparkClient;
import io.github.briqt.spark4j.constant.SparkApiVersion;
import io.github.briqt.spark4j.model.SparkMessage;
import io.github.briqt.spark4j.model.request.SparkRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/09/25   18:41
 * @Version 1.0
 * @Description 对接星火大模型平台
 */
@Slf4j
@Service
public class AiManager {

    @Resource
    private SparkClient sparkClient;


    /**
     * 发送消息到星火大模型平台
     * @param isNeedTemplate 是否使用预设，进行 AI 生成； true 使用 、false 不使用 ，false 的情况是未约束 AI ，不一定能返回预期的结果
     * @param content 内容
     *                示例：
     *                分析需求：
     *                分析网站用户增长情况，请使用柱状图展示
     *                原始数据：
     *                日期,用户数
     *                1号,10
     *                2号,20
     *                3号,30
     * @return AI返回的内容
     *          '【【【'
     *          图表对应的 JSON 代码
     *          '【【【'
     *          结论
     */
    public String sendMsgToXingHuo(boolean isNeedTemplate, String content) {
        List<SparkMessage> messages = new ArrayList<>();
        if (isNeedTemplate) {
            // AI 生成问题的预设条件
            String predefinedInformation = "请严格按照下面的输出格式生成结果,且不得添加任何多余内容(例如无关文字、注释、代码块标记或反引号):\n" +
                    "\n" +
                    "'【【【'" +
                    "{生成 Echarts V5 的 option 配置对象 JSON 代码,要求为合法 JSON 格式且不含任何额外内容(如注释或多余字符)} '【【【' 结论:{\n" +
                    "提供对数据的详细分析结论,内容应尽可能准确、详细,不允许添加其他无关文字或注释}\n" +
                    "\n" +
                    "示例:\n" +
                    "输入: 分析需求:分析网站用户增长情况,请使用柱状图展示 原始数据:日期,用户数 1号,10 2号,20 3号,30\n" +
                    "\n" +
                    "期望输出: '【【【' \n\"title\": { \"text\": \"分析网站用户增长情况\" }, \n\"xAxis\": { \n\"typ\": \"category\", \n\"data\": [\"1号\", \"2号\", \"3号\"] }, \n\"yAxis\": { \"type\": \"value\"}, \n\"series\": [ { \n\"name\": \"用户数\", \n\"type\": \"bar\", \n\"data\": [10, 20, 30] \n} ] '【【【' 结论: 从数据看，网站用户数由1号的10人增长到2号的20人，再到3号的30人，呈现出明显的上升趋势。这表明在这段时间内网站用户吸引力增强，可能与推广活动、内容更新或其他外部因素有关。";

            messages.add(SparkMessage.systemContent(predefinedInformation + "\n" + "----------------------------------"));
        }
        messages.add(SparkMessage.userContent(content));
        // 构造请求
        SparkRequest sparkRequest = SparkRequest.builder()
                // 消息列表
                .messages(messages)
                // 模型回答的tokens的最大长度,非必传,取值为[1,4096],默认为2048
                .maxTokens(2048)
                // 核采样阈值。用于决定结果随机性,取值越高随机性越强即相同的问题得到的不同答案的可能性越高 非必传,取值为[0,1],默认为0.5
                .temperature(0.6)
                // 指定请求版本
                .apiVersion(SparkApiVersion.V3_5)
                .build();
        // 同步调用
        String responseContent = sparkClient.chatSync(sparkRequest).getContent().trim();
        if (!isNeedTemplate) {
            return responseContent;
        }
        log.info("星火 AI 返回的结果 {}", responseContent);
        AtomicInteger atomicInteger = new AtomicInteger(1);
        while (responseContent.split("'【【【'").length < 3) {
            responseContent = sparkClient.chatSync(sparkRequest).getContent().trim();
            if (atomicInteger.incrementAndGet() >= 4) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "星火 AI 生成失败");
            }
        }
        return responseContent;
    }
}
