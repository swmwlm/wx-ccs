package com.github.wx.ccs.entity.dto;

import com.github.wx.ccs.entity.WxApplication;

public class Code2SessionDto extends WxApplication {

    String code;

    public Code2SessionDto(String appId, String secret, String code) {
        super(appId, secret);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
