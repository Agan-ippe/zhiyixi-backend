package com.zhiyixi.model.dto.user;

import com.zhiyixi.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author <a href="https://github.com/Agan-ippe">知莫</a>
 * @Date 2025/09/08   13:24
 * @Version 1.0
 * @Description 用户查询请求封装类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 开放平台id
     */
    private String unionId;

    /**
     * 公众号openId
     */
    private String mpOpenId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
