package com.zhiyixi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhiyixi.model.entity.UserDO;
import com.zhiyixi.mapper.UserMapper;
import com.zhiyixi.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author 93988
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2025-09-08 12:37:21
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO>
    implements UserService {

}




