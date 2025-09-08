package com.zhiyixi.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class UserDO implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 账号
     */
    private String user_account;

    /**
     * 密码
     */
    private String user_password;

    /**
     * 用户昵称
     */
    private String user_name;

    /**
     * 用户头像
     */
    private String user_avatar;

    /**
     * 用户角色：user/admin
     */
    private String user_role;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 更新时间
     */
    private Date update_time;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer is_delete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}