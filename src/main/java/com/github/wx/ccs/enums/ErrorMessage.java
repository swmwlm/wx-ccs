package com.github.wx.ccs.enums;

public enum  ErrorMessage {

    SUCCESS(0, "ok"),
    BUSY(-1, "error: system busy"),
    UNAUTHORIZED(1000, "unauthorized"),
    WITHOUT_TOKEN(1001, "without parameter: token"),
    WITHOUT_ENCRYPT_DATA(1002, "without parameter: encrypted data"),
    WITHOUT_IV(1003, "without parameter: iv"),
    DECODE_FAILED(1004, "error: decode failed"),
    WITHOUT_ACCESS_TOKEN(1005, "without parameter: access token"),
    WITHOUT_SCENE(1006, "without parameter: scene"),
    GET_MINI_QRCODE_FAILED(1007, "get qrcode failed"),
    INVALID_CODE(40029, "invalid parameter: code"),
    INVALID_APP_ID(40013, "invalid parameter: appId"),
    INVALID_APP_SECRET(40125, "invalid parameter: secret");

    private int code;
    private String message;

    ErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ErrorMessage get(int code) {
        for (ErrorMessage e : ErrorMessage.values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }
}
