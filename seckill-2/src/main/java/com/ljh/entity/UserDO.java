package com.ljh.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class UserDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Setter
    private Integer id;
    private String name;
    @Setter
    private Byte gender;
    @Setter
    private Integer age;
    private String telephone;
    private String registerMode;
    private String thirdPartyId;

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public void setRegisterMode(String registerMode) {
        this.registerMode = registerMode == null ? null : registerMode.trim();
    }

    public void setThirdPartyId(String thirdPartyId) {
        this.thirdPartyId = thirdPartyId == null ? null : thirdPartyId.trim();
    }
}
