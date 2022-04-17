package com.ljh.service.impl;

import com.ljh.dao.UserDOMapper;
import com.ljh.dao.UserPasswordDOMapper;
import com.ljh.entity.UserDO;
import com.ljh.entity.UserDOExample;
import com.ljh.entity.UserPasswordDO;
import com.ljh.entity.UserPasswordDOExample;
import com.ljh.error.BusinessErrorEnum;
import com.ljh.error.BusinessException;
import com.ljh.service.UserService;
import com.ljh.service.model.UserModel;
import com.ljh.validator.ValidationResult;
import com.ljh.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDOMapper userDOMapper;
    private final UserPasswordDOMapper userPasswordDOMapper;
    private final ValidatorImpl validator;

    public UserServiceImpl(UserDOMapper userDOMapper, UserPasswordDOMapper userPasswordDOMapper, ValidatorImpl validator) {
        this.userDOMapper = userDOMapper;
        this.userPasswordDOMapper = userPasswordDOMapper;
        this.validator = validator;
    }

    @Override
    public UserModel getUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO == null) return null;
        UserPasswordDO userPasswordDO = this.selectByUserId(userDO.getId());
        return this.convertModelFromEntity(userDO, userPasswordDO);
    }

    @Override
    @Transactional
    public void register(UserModel userModel) throws BusinessException {
        // 校验入参
        if (userModel == null)
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR);
        ValidationResult result = validator.validate(userModel);
        if (result.isHasErrors())
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, result.getErrMsg());

        // model → entity
        UserDO userDO = this.convertEntityFromModel(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "手机号已重复注册");
        }

        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = this.convertUserPasswordDOFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserModel validateLogin(String telephone, String encryptPassword) throws BusinessException {
        // 通过用户的手机获取用户信息
        UserDOExample userDOExample = new UserDOExample();
        userDOExample.createCriteria().andTelephoneEqualTo(telephone);
        UserDO userDO = userDOMapper.selectByExample(userDOExample).get(0);
        if (userDO == null) throw new BusinessException(BusinessErrorEnum.USER_LOGIN_FAIL);
        UserPasswordDO userPasswordDO = this.selectByUserId(userDO.getId());
        UserModel userModel = this.convertModelFromEntity(userDO, userPasswordDO);

        // 验证密码是否正确
        if (!StringUtils.equals(encryptPassword, userModel.getEncryptPassword()))
            throw new BusinessException(BusinessErrorEnum.USER_LOGIN_FAIL);

        return userModel;
    }

    private UserPasswordDO selectByUserId(Integer userId) {
        UserPasswordDOExample userPasswordDOExample = new UserPasswordDOExample();
        userPasswordDOExample.createCriteria().andUserIdEqualTo(userId);
        return userPasswordDOMapper.selectByExample(userPasswordDOExample).get(0);
    }

    private UserPasswordDO convertUserPasswordDOFromModel(UserModel userModel) {
        if (userModel == null) return null;
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncryptPassword(userModel.getEncryptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    private UserDO convertEntityFromModel(UserModel userModel) {
        if (userModel == null) return null;
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }

    private UserModel convertModelFromEntity(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (userDO == null) return null;
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);
        if (userPasswordDO != null) userModel.setEncryptPassword(userPasswordDO.getEncryptPassword());
        return userModel;
    }
}
