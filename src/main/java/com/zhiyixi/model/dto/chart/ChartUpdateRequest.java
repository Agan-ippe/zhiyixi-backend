package com.zhiyixi.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/09/09   17:37
 * @Version 1.0
 * @Description
 */
@Data
public class ChartUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 图表名称
     */
    private String chartName;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表数据
     */
    private String chartData;

    /**
     * 图表类型
     */
    private String chartType;

    /**
     * AI生成的图表信息
     */
    private String genChartInfo;

    /**
     * AI生成的分析结论
     */
    private String genChartResult;


    private static final long serialVersionUID = 1L;
}
