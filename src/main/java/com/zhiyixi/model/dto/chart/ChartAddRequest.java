package com.zhiyixi.model.dto.chart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/09/09   17:35
 * @Version 1.0
 * @Description 数据分析创建请求模型
 */
@Data
@ApiModel(value = "ChartAddRequest", description = "数据分析创建请求模型")
public class ChartAddRequest implements Serializable {

    /**
     * 图表名称
     */
    @ApiModelProperty(value = "图表名称")
    private String chartName;

    /**
     * 分析目标
     */
    @ApiModelProperty(value = "分析目标")
    private String goal;

    /**
     * 图表数据
     */
    @ApiModelProperty(value = "图表数据")
    private String chartData;

    /**
     * 图表类型
     */
    @ApiModelProperty(value = "图表类型")
    private String chartType;

    private static final long serialVersionUID = 1L;
}
