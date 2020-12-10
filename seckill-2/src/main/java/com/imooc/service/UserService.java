package com.imooc.service;

import com.imooc.error.BusinessException;
import com.imooc.service.model.UserModel;

public interface UserService {

    // 通过用户ID获取用户对象的方法
    UserModel getUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;

    /**
     *
     * @param telphone          用户注册手机
     * @param encryptPassword   用户加密后的密码
     */
    UserModel validateLogin(String telphone, String encryptPassword) throws BusinessException;
}
