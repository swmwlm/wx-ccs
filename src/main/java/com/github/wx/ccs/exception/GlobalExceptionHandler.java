package com.github.wx.ccs.exception;

import com.github.wx.ccs.entity.ServerResponse;
import com.github.wx.ccs.enums.ResponseCode;
import com.github.wx.ccs.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@RestControllerAdvice
public class GlobalExceptionHandler {


    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(value = Exception.class)
    public ServerResponse defaultErrorHandler(HttpServletRequest request, Exception e) {
        return errorHandler(request, e);
    }


    public static ServerResponse errorHandler(HttpServletRequest request, Exception e) {
        Enumeration enumeration = request.getParameterNames();
        logger.error("Request url：{}", request.getRequestURL().toString());
        StringBuffer paramsStr = new StringBuffer("Request params:");
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement().toString();
            paramsStr.append(name).append(":").append(request.getParameter(name)).append(",");
        }
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            return ServerResponse.fail(ResponseCode.NOT_FOUND);
        } else if (e instanceof NullPointerException) {
            logger.error("Exception detail: ", e);
            logger.error(paramsStr.toString());
            e.printStackTrace();
            return ServerResponse.fail(ResponseCode.NULL_POINTER);
        } else if (e instanceof ServiceException) {
            String message = e.getMessage();
            if (Util.isEmpty(message) && Util.isNotEmpty(e.getCause())) message = e.getCause().getMessage();
            if (Util.isEmpty(((ServiceException) e).getCode())) {
                return ServerResponse.fail(ResponseCode.BAD_REQUEST, message);
            } else {
                return ServerResponse.fail(((ServiceException) e).getCode(), message);
            }
        } else if (e instanceof MissingServletRequestParameterException) {
            logger.error("Exception detail: ", e);
            logger.error(paramsStr.toString());
            e.printStackTrace();
            return ServerResponse.fail(ResponseCode.WITHOUT_PARAMETER, e.getMessage());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            logger.error("Exception detail: ", e);
            e.printStackTrace();
            return ServerResponse.fail(ResponseCode.REQUEST_METHOD_NOT_SUPPORTED, e.getMessage());
        } else {
            logger.error("Exception detail: ", e);
            logger.error(paramsStr.toString());
            e.printStackTrace();
            return ServerResponse.fail(ResponseCode.UN_KNOWN, e.getMessage());
        }
    }
}
