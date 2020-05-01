package com.github.wx.ccs.exception;

import com.github.wx.ccs.enums.ErrorMessage;

public class ServiceException extends RuntimeException {

    private  Integer code;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public ServiceException(ErrorMessage error) {
        super(error.getMessage());
        this.code = error.getCode();
    }

    public Integer getCode() {
        return code;
    }

}