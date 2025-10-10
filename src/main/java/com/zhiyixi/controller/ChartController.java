package com.zhiyixi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiyixi.annotation.AuthCheck;
import com.zhiyixi.common.BaseResponse;
import com.zhiyixi.common.DeleteRequest;
import com.zhiyixi.common.ErrorCode;
import com.zhiyixi.common.ResultUtils;
import com.zhiyixi.constant.CommonConstant;
import com.zhiyixi.constant.UserConstant;
import com.zhiyixi.exception.BusinessException;
import com.zhiyixi.exception.ThrowUtils;
import com.zhiyixi.manager.AiManager;
import com.zhiyixi.model.dto.chart.*;
import com.zhiyixi.model.entity.ChartDO;
import com.zhiyixi.model.entity.UserDO;
import com.zhiyixi.model.vo.chart.BiResponse;
import com.zhiyixi.service.ChartService;
import com.zhiyixi.service.UserService;
import com.zhiyixi.utils.ExcelUtils;
import com.zhiyixi.utils.SqlUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/09/08   16:09
 * @Version 1.0
 * @Description 数据分析接口
 */
@Slf4j
@RestController
@Api(tags = "数据分析接口")
@RequestMapping("/chart")
public class ChartController {
    @Resource
    private ChartService chartService;

    @Resource
    private UserService userService;

    @Resource
    private AiManager aiManager;

    // region 增删改查

    /**
     * 创建
     *
     * @param chartAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("创建图表")
    public BaseResponse<Long> addChart(@RequestBody ChartAddRequest chartAddRequest, HttpServletRequest request) {
        if (chartAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChartDO chartDO = new ChartDO();
        BeanUtils.copyProperties(chartAddRequest, chartDO);
        UserDO loginUser = userService.getLoginUser(request);
        chartDO.setUserId(loginUser.getId());
        boolean result = chartService.save(chartDO);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newChartId = chartDO.getId();
        return ResultUtils.success(newChartId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation("删除图表")
    public BaseResponse<Boolean> deleteChart(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserDO user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        ChartDO oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldChart.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = chartService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param chartUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @ApiOperation("更新图表(仅管理员)")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateChart(@RequestBody ChartUpdateRequest chartUpdateRequest) {
        if (chartUpdateRequest == null || chartUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChartDO chartDO = new ChartDO();
        BeanUtils.copyProperties(chartUpdateRequest, chartDO);
        long id = chartUpdateRequest.getId();
        // 判断是否存在
        ChartDO oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = chartService.updateById(chartDO);
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    @ApiOperation("根据id获取图表")
    public BaseResponse<ChartDO> getChartById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChartDO chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(chart);
    }

    /**
     * 分页获取列表（封装类）
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/")
    @ApiOperation("分页获取图表列表")
    public BaseResponse<Page<ChartDO>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                       HttpServletRequest request) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ChartDO> pageResult = chartService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(pageResult);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/")
    @ApiOperation("分页获取当前用户创建的图表列表")
    public BaseResponse<Page<ChartDO>> listMyChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                         HttpServletRequest request) {
        if (chartQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserDO loginUser = userService.getLoginUser(request);
        chartQueryRequest.setUserId(loginUser.getId());
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ChartDO> pageResult = chartService.page(new Page<>(current, size),
                getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(pageResult);
    }

    // endregion

    /**
     * 编辑（用户）
     *
     * @param chartEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    @ApiOperation("编辑图表(创建者)")
    public BaseResponse<Boolean> editChart(@RequestBody ChartEditRequest chartEditRequest, HttpServletRequest request) {
        if (chartEditRequest == null || chartEditRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        ChartDO chartDO = new ChartDO();
        BeanUtils.copyProperties(chartEditRequest, chartDO);
        UserDO loginUser = userService.getLoginUser(request);
        long id = chartEditRequest.getId();
        // 判断是否存在
        ChartDO oldChart = chartService.getById(id);
        ThrowUtils.throwIf(oldChart == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可编辑
        if (!oldChart.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean result = chartService.updateById(chartDO);
        return ResultUtils.success(result);
    }

    // endregion

    /**
     * 获取查询包装类
     *
     * @param chartQueryRequest
     * @return
     */
    private QueryWrapper<ChartDO> getQueryWrapper(ChartQueryRequest chartQueryRequest) {
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
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(StringUtils.isNotBlank(chartType), "chart_type", chartType);
        queryWrapper.eq("is_deleted", 0);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    // endregion

    /**
     * AI生成图表
     *
     * @param file                   目标文件
     * @param aiGenerateChartRequest
     * @param request
     * @return
     */
    @PostMapping("/ai/generate")
    @ApiOperation("AI生成图表")
    public BaseResponse<BiResponse> AIGenerateChart(@RequestPart("file") MultipartFile file,
                                                    AIGenerateChartRequest aiGenerateChartRequest,
                                                    HttpServletRequest request) {
        String chartName = aiGenerateChartRequest.getChartName();
        String goal = aiGenerateChartRequest.getGoal();
        String chartType = aiGenerateChartRequest.getChartType();
        // 仅提供登录用户使用
        UserDO loginUser = userService.getLoginUser(request);
        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(chartName) && chartName.length() > 100,
                ErrorCode.PARAMS_ERROR, "图表名称过长");
        // 校验文件
        // 校验文件大小
        // 校验文件类型，仅支持 .xlsx

        // 构造用户输入
        // 输入示例
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
        String csvDate = ExcelUtils.excelToCsv(file);
        userInput.append(csvDate).append("\n");
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
        chartDO.setChartData(csvDate);
        chartDO.setChartType(chartType);
        chartDO.setGenChartInfo(genChart);
        chartDO.setGenChartResult(genResult);
        log.info("saving chart to database");
        boolean saveResult = chartService.save(chartDO);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "提交数据库失败");
        }
        BiResponse biResponse = new BiResponse();
        biResponse.setGenChart(genChart);
        biResponse.setGenResult(genResult);
        biResponse.setChartId(chartDO.getId());
        return ResultUtils.success(biResponse);
    }
}
