package com.zhiyixi.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 文件常量
 * @author <a href="https://github.com/Agan-ippe">知莫</a>
 */
public interface FileConstant {
    /**
     * 限制文件最大size
     */
    long MAX_FILE_SIZE = 1024 * 1024;

    /**
     * 原始数据文件后缀
     */
    List<String> RAW_DATA_SUFFIX_LIST = Arrays.asList("xlsx", "csv", "xls");

}
