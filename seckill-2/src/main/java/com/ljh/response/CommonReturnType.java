package com.ljh.response;

public class CommonReturnType {
    /**
     * 返回 success / fail
     */
    private String status;
    /**
     * if (status = success)，返回 Json 数据
     * if (status = fail)，错误码格式
     */
    private Object data;

    public static CommonReturnType create(Object result) {
        return CommonReturnType.create("success", result);
    }

    public static CommonReturnType create(String status, Object result) {
        CommonReturnType type = new CommonReturnType();
        type.setStatus(status);
        type.setData(result);
        return type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
