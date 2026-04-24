package com.ljh.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
public class SequenceDO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    @Setter
    private Integer currentValue;
    @Setter
    private Integer step;

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}
