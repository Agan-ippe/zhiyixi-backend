package com.zhiyixi.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/09/08   13:23
 * @Version 1.0
 * @Description 用户创建请求封装类
 */
@Data
public class UserAddRequest implements Serializable {
    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
