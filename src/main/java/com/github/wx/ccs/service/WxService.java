package com.github.wx.ccs.service;

import com.github.wx.ccs.entity.UserInfo;
import java.io.UnsupportedEncodingException;

public interface WxService {

    void setRefreshToken(String appId, String token, String refreshToken);
    void setOpenId(String appId, String token, String openId);
    void setUnionId(String appId, String token, String unionId);
    void setAccessToken(String appId, String accessToken, Integer expire);

    String getRefreshToken(String appId, String token);
    String getOpenId(String appId, String token);
    String getUnionId(String appId, String token);
    String getAccessToken(String appId);

    String ouath2(String appId, String appSecret, String code) throws UnsupportedEncodingException;
    UserInfo userInfo(String appId, String token, boolean refresh);

}
