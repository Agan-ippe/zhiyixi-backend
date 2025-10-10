package com.zhiyixi.model.vo.chart;

import lombok.Data;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/10/03   11:01
 * @Version 1.0
 * @Description 智能BI的返回结果
 */
@Data
public class BiResponse {

    private String genChart;

    private String genResult;

    private Long chartId;
}
