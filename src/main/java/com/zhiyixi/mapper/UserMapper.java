package com.zhiyixi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhiyixi.model.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 93988
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2025-09-08 12:37:21
* @Entity com.zhiyixi.model.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

}




