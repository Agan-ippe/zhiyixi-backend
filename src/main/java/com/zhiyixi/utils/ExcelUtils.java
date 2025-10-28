package com.zhiyixi.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/09/17   15:44
 * @Version 1.0
 * @Description excel工具类
 */
@Slf4j
public class ExcelUtils {
    /**
     * excel 转换为 csv
     * @param multipartFile
     * @return
     */
    public static String excelToCsv(MultipartFile multipartFile, String fileSuffix){
        // 读取数据
        List<Map<Integer,String>> list = null;
        // 判断文件格式
        ExcelTypeEnum fileType = ExcelTypeEnum.CSV;
        fileSuffix = "." + fileType;
        ExcelTypeEnum[] values = ExcelTypeEnum.values();
        // 排除 csv文件
        for (ExcelTypeEnum item : values) {
            if (item.getValue().equals(fileSuffix)) {
                fileType = item;
                break;
            }
        }
        // 表格文件转 csv
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(fileType)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("读取excel文件失败",e);
        }
        if (CollUtil.isEmpty(list)) {
            log.error("文件内容为空");
            return "";
        }
        // 转换为csv
        StringBuilder sb = new StringBuilder();
        // 读取表头
        LinkedHashMap<Integer, String> headMap = (LinkedHashMap<Integer, String>) list.get(0);
        // 过滤
        List<String> headList = filterDateIsNull(headMap);
        sb.append(StrUtil.join(",", headList)).append("\n");
        // 读取数据
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap<Integer, String>) list.get(i);
            // 过滤
            List<String> dataList = filterDateIsNull(dataMap);
            sb.append(StrUtil.join(",", dataList)).append("\n");
        }
        return sb.toString();
    }

    /**
     * 过滤数据是 null 的数据
     * @param targetMap
     * @return
     */
    static List<String> filterDateIsNull(LinkedHashMap<Integer,String> targetMap){
        return targetMap.values().stream().filter(StrUtil::isNotBlank).collect(Collectors.toList());
    }
}
