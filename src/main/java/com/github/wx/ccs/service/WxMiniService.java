package com.github.wx.ccs.service;

import com.github.wx.ccs.entity.ServerResponse;

import java.io.IOException;

import java.util.Map;

public interface WxMiniService {

    void setSessionKey(String appId, String token, String sessionKey);
    void setOpenId(String appId, String token, String openId);
    void setUnionId(String appId, String token, String unionId);
    void setAccessToken(String appId, String accessToken, Integer expire);

    String getSessionKey(String appId, String token);
    String getOpenId(String appId, String token);
    String getUnionId(String appId, String token);
    ServerResponse getAccessToken(String appId, String secret);

    ServerResponse getUnlimitedQrcode(String accessToken, Map<String, Object> params) throws IOException;

}
