package com.ljh.controller;

import com.ljh.error.BusinessErrorEnum;
import com.ljh.error.BusinessException;
import com.ljh.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class BaseController {

    public static final String CONTENT_TYPE_FORMED = "application/x-www-form-urlencoded";

    // 定义 @ExceptionHandler 解决未被 Controller 层吸收的 Exception
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object handlerException(HttpServletRequest request, Exception ex) {
        Map<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        } else {
            responseData.put("errCode", BusinessErrorEnum.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", BusinessErrorEnum.UNKNOWN_ERROR.getErrMsg());
        }
        return CommonReturnType.create("fail", responseData);
    }
}
