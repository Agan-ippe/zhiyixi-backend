package com.zhiyixi.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/09/08   13:25
 * @Version 1.0
 * @Description 用户更新个人信息请求封装类
 */
@Data
public class UserUpdateMyRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    private static final long serialVersionUID = 1L;
}
