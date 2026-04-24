package com.ljh.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ValidatorImpl {

    private final Validator validator;

    public ValidatorImpl(Validator validator) {
        this.validator = validator;
    }

    /**
     * 实现校验方法并返回校验结果
     */
    public ValidationResult validate(Object bean) {
        ValidationResult result = new ValidationResult();
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(bean);
        if (!constraintViolationSet.isEmpty()) {
            result.setHasErrors(true);
            constraintViolationSet.forEach(constraintViolation -> {
                String propertyName = constraintViolation.getPropertyPath().toString();
                String errMsg = constraintViolation.getMessage();
                result.getErrorMsgMap().put(propertyName, errMsg);
            });
        }
        return result;
    }
}
