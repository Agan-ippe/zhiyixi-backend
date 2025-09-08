package com.zhiyixi.model.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/09/08   13:37
 * @Version 1.0
 * @Description 已登录用户信息脱敏VO
 */
@Data
public class LoginUserVO implements Serializable {
    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;

}
