package com.zhiyixi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhiyixi.model.dto.user.UserQueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhiyixi.model.entity.UserDO;
import com.zhiyixi.model.vo.user.LoginUserVO;
import com.zhiyixi.model.vo.user.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author <a href="https://github.com/Agan-ippe">知莫</a>
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-09-08 12:37:21
*/
public interface UserService extends IService<UserDO> {
    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    UserDO getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    UserDO getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(UserDO user);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    LoginUserVO getLoginUserVO(UserDO user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(UserDO user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<UserDO> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<UserDO> getQueryWrapper(UserQueryRequest userQueryRequest);


    /**
     * 更新用户可用余额
     * @param request
     * @return
     */
    boolean updateUserSurplus(HttpServletRequest request);

}
