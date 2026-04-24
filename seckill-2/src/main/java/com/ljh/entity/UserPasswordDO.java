package com.ljh.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class UserPasswordDO implements Serializable {
    @Setter
    private Integer id;
    private String encryptPassword;
    @Setter
    private Integer userId;
    @Serial
    private static final long serialVersionUID = 1L;

    public void setEncryptPassword(String encryptPassword) {
        this.encryptPassword = encryptPassword == null ? null : encryptPassword.trim();
    }
}
