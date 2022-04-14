package com.ljh.service;

import com.ljh.error.BusinessException;
import com.ljh.service.model.UserModel;

public interface UserService {

    /**
     * 通过用户 ID 获取用户对象的方法
     */
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    /**
     * 验证登录
     *
     * @param telephone       用户注册手机
     * @param encryptPassword 用户加密后的密码
     */
    UserModel validateLogin(String telephone, String encryptPassword) throws BusinessException;
}
