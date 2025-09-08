package com.zhiyixi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiyixi.model.entity.ChartDO;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 93988
* @description 针对表【chart(数据分析表)】的数据库操作Mapper
* @createDate 2025-09-08 13:00:57
* @Entity com.zhiyixi.model.entity.ChartDO
*/
@Mapper
public interface ChartMapper extends BaseMapper<ChartDO> {

}




