package com.zhiyixi.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 数据分析表
 * @TableName chart
 */
@TableName(value ="chart")
@Data
public class ChartDO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表数据
     */
    private String chart_data;

    /**
     * 图表类型
     */
    private String chart_type;

    /**
     * AI生成的图表信息
     */
    private String gen_chart_info;

    /**
     * AI生成的分析结论
     */
    private String gen_chart_result;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 是否删除
     */
    private Integer is_delete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}