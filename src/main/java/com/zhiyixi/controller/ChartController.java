package com.zhiyixi.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhiyixi.annotation.AuthCheck;
import com.zhiyixi.common.BaseResponse;
import com.zhiyixi.common.DeleteRequest;
import com.zhiyixi.common.ErrorCode;
import com.zhiyixi.common.ResultUtils;
import com.zhiyixi.constant.FileConstant;
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
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
     * 分页获取列表（封装类）（管理员可用）
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @Deprecated
    @PostMapping("/list/page")
    @ApiOperation("分页获取图表列表")
    public BaseResponse<Page<ChartDO>> listChartByPage(@RequestBody ChartQueryRequest chartQueryRequest,
                                                       HttpServletRequest request) {
        long current = chartQueryRequest.getCurrent();
        long size = chartQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<ChartDO> pageResult = chartService.page(new Page<>(current, size),
                chartService.getQueryWrapper(chartQueryRequest));
        return ResultUtils.success(pageResult);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param chartQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/my/list/page")
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
                chartService.getQueryWrapper(chartQueryRequest));
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
     * 因为文件解析多次使用，所以将改代码抽离
     *
     * @param multipartFile 源文件
     * @return 返回 csv 字符串
     */
    public String analysisFile(MultipartFile multipartFile){
        ThrowUtils.throwIf(multipartFile == null, ErrorCode.PARAMS_ERROR, "文件为空");
        // 文件大小
        long size = multipartFile.getSize();
        ThrowUtils.throwIf(size > FileConstant.MAX_FILE_SIZE, ErrorCode.SYSTEM_ERROR, "文件大小不能超过1MB");
        // 文件后缀
        String originalFilename = multipartFile.getOriginalFilename();
        String fileSuffix = FileUtil.getSuffix(originalFilename);
        ThrowUtils.throwIf(!FileConstant.RAW_DATA_SUFFIX_LIST.contains(fileSuffix), ErrorCode.PARAMS_ERROR, "文件格式不正确");
        return ExcelUtils.excelToCsv(multipartFile, fileSuffix);
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
        String csvData = analysisFile(file);
        BiResponse biResponse = chartService.getChartByAi(csvData, aiGenerateChartRequest, request);
        return ResultUtils.success(biResponse);
    }
}
