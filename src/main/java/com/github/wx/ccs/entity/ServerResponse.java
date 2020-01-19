package com.github.wx.ccs.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.wx.ccs.enums.ErrorMessage;
import com.github.wx.ccs.enums.ResponseCode;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * 服务端返回对象
 * @param <T>
 * @author  yangyu
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@SuppressWarnings({"unused"})
public class ServerResponse<T> implements Serializable {

    protected Integer code;

    private String msg;

    private T data;

    private Boolean success;

    public ServerResponse() {}

    public ServerResponse(ResponseCode responseCode) {
        this(responseCode.getCode(), responseCode.getDescription());
    }

    public ServerResponse(Integer status, String message) {
        this(status, message, null, false);
    }

    public ServerResponse(Integer code, String message, T data, Boolean success) {
        this.code = code;
        this.msg = message;
        this.data = data;
        this.success = success;
    }

    public static <T> ServerResponse<T> response(Integer status, String message, T data, Boolean success) {
        return new ServerResponse<>(status, message, data, success);
    }

    // Success
    public static <T> ServerResponse<T> success(T data) {
        return new ServerResponse<>(ResponseCode.OK.getCode(), ResponseCode.OK.getDescription(), data, true);
    }

    public static ServerResponse success() {
        return new ServerResponse<>(ResponseCode.OK.getCode(), ResponseCode.OK.getDescription(), new HashMap<>(), true);
    }

    public static ServerResponse fail(ResponseCode code) {
        return fail(code, null);
    }

    public static <T> ServerResponse<T> fail(ResponseCode code, String message) {
        return fail(code, message, null);
    }

    public static <T> ServerResponse<T> fail(ResponseCode code, String message, T data) {
        if (code.getCode().equals(ResponseCode.OK.getCode())) {
            throw new InvalidParameterException("Code type Error, the code of the fail response cannot be OK(200).");
        }
        return new ServerResponse(code.getCode(), message, new HashMap<>(), false);
    }

    public static <T> ServerResponse<T> fail(Integer status, String message) {
        return new ServerResponse(status, message, new HashMap<>(), false);
    }

    public static <T> ServerResponse<T> fail(ErrorMessage error) {
        return ServerResponse.fail(error.getCode(), error.getMessage());
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean hasSuccess() {
        return success;
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", success=" + success +
                '}';
    }


}
