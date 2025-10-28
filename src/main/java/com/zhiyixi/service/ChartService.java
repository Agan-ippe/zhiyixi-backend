package com.zhiyixi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiyixi.model.dto.chart.AIGenerateChartRequest;
import com.zhiyixi.model.dto.chart.ChartQueryRequest;
import com.zhiyixi.model.entity.ChartDO;
import com.zhiyixi.model.vo.chart.BiResponse;

import javax.servlet.http.HttpServletRequest;

/**
* @author 93988
* @description 针对表【chart(数据分析表)】的数据库操作Service
* @createDate 2025-09-08 13:00:57
*/
public interface ChartService extends IService<ChartDO> {

    QueryWrapper<ChartDO> getQueryWrapper(ChartQueryRequest chartQueryRequest);

    BiResponse getChartByAi(String csvData , AIGenerateChartRequest aiGenerateChartRequest, HttpServletRequest request);

}
