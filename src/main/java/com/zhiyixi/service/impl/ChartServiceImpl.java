package com.zhiyixi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiyixi.common.ErrorCode;
import com.zhiyixi.constant.CommonConstant;
import com.zhiyixi.exception.BusinessException;
import com.zhiyixi.exception.ThrowUtils;
import com.zhiyixi.manager.AiManager;
import com.zhiyixi.mapper.ChartMapper;
import com.zhiyixi.model.dto.chart.AIGenerateChartRequest;
import com.zhiyixi.model.dto.chart.ChartQueryRequest;
import com.zhiyixi.model.entity.ChartDO;
import com.zhiyixi.model.entity.UserDO;
import com.zhiyixi.model.vo.chart.BiResponse;
import com.zhiyixi.service.ChartService;
import com.zhiyixi.service.UserService;
import com.zhiyixi.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author 93988
* @description 针对表【chart(数据分析表)】的数据库操作Service实现
* @createDate 2025-09-08 13:00:57
*/
@Slf4j
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, ChartDO>
    implements ChartService{

    @Resource
    private UserService userService;

    @Resource
    private AiManager aiManager;


    @Override
    public QueryWrapper<ChartDO> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
        QueryWrapper<ChartDO> queryWrapper = new QueryWrapper<>();
        if (chartQueryRequest == null) {
            return queryWrapper;
        }
        String sortField = chartQueryRequest.getSortField();
        String sortOrder = chartQueryRequest.getSortOrder();
        Long id = chartQueryRequest.getId();
        String chartName = chartQueryRequest.getChartName();
        Long userId = chartQueryRequest.getUserId();
        String goal = chartQueryRequest.getGoal();
        String chartType = chartQueryRequest.getChartType();
        // 拼接查询条件
        queryWrapper.eq(id != null && id > 0, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(chartName), "chart_name", chartName);
        queryWrapper.eq(StringUtils.isNotBlank(goal), "goal", goal);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "user_id", userId);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chart_type", chartType);
        queryWrapper.eq("is_delete", 0);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public BiResponse getChartByAi(String csvData, AIGenerateChartRequest aiGenerateChartRequest, HttpServletRequest request) {
        String chartName = aiGenerateChartRequest.getChartName();
        String goal = aiGenerateChartRequest.getGoal();
        String chartType = aiGenerateChartRequest.getChartType();
        // 仅提供登录用户使用
        UserDO loginUser = userService.getLoginUser(request);
        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(chartName) && chartName.length() > 100,
                ErrorCode.PARAMS_ERROR, "图表名称过长");
        // 校验用户剩余使用次数
        boolean hasSurplus = userService.updateUserSurplus(request);
        ThrowUtils.throwIf(!hasSurplus, ErrorCode.OPERATION_ERROR, "剩余次数不足");
        // 构造用户输入，输入示例
        // 分析需求：
        // xxx，
        // 请使用xx图展示
        // 原始数据：
        // xxx
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求:").append("\n");
        String userGoal = goal;
        // 拼接分析目标
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType + "展示";
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        // 读取拼接用户上传的文件
        userInput.append(csvData).append("\n");
        // 调用AI接口
        String aiGenerateResult = aiManager.sendMsgToXingHuo(true, userInput.toString());
        String[] split = aiGenerateResult.split("'【【【'");
        if (split.length < 3) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI生成失败");
        }
        String genChart = split[1].trim();
        String genResult = split[2].trim();
        // 提交到数据库
        ChartDO chartDO = new ChartDO();
        chartDO.setUserId(loginUser.getId());
        chartDO.setChartName(chartName);
        chartDO.setGoal(goal);
        chartDO.setChartData(csvData);
        chartDO.setChartType(chartType);
        chartDO.setGenChartInfo(genChart);
        chartDO.setGenChartResult(genResult);
        log.info("saving chart to database");
        boolean saveResult = this.save(chartDO);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交数据库失败");
        }
        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(genChart);
        biResponse.setGenResult(genResult);
        biResponse.setChartId(chartDO.getId());
        return biResponse;
    }
}




