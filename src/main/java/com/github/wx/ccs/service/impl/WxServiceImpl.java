package com.github.wx.ccs.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.wx.ccs.constant.Api;
import com.github.wx.ccs.constant.ParamConfig;
import com.github.wx.ccs.constant.Prefix;
import com.github.wx.ccs.entity.UserInfo;
import com.github.wx.ccs.enums.ErrorMessage;
import com.github.wx.ccs.exception.ServiceException;
import com.github.wx.ccs.service.WxService;
import com.github.wx.ccs.utils.HttpRequest;
import com.github.wx.ccs.utils.Md5;
import com.github.wx.ccs.utils.RedisUtil;
import com.github.wx.ccs.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.UUID;

@Service
public class WxServiceImpl implements WxService {

    private final Logger log = LoggerFactory.getLogger(WxService.class);

    final RedisUtil redisUtil;

    final ParamConfig paramConfig;

    public WxServiceImpl(RedisUtil redisUtil, ParamConfig paramConfig) {
        this.redisUtil = redisUtil;
        this.paramConfig = paramConfig;
    }

    @Override
    public String ouath2(String appId, String appSecret, String code) throws UnsupportedEncodingException {
        String api = String.format(Api.OAUTH2, appId, appSecret, URLDecoder.decode(code,"UTF-8"));
        HttpRequest response = HttpRequest.get(api);
        String body = response.body();
        log.info("authorize: code: {}, body: {}, appId: {}, secret: {}", code, body, appId, appSecret);
        Map<String, Object> map = (Map<String, Object>) JSON.parse(body);
        if (Util.isNotEmpty(map.get("errcode"))) {
            log.info("failed to authorized：{}", body);
            int errcode = (int) map.get("errcode");
            throw new ServiceException(Util.isNotEmpty(ErrorMessage.get(errcode)) ? ErrorMessage.get(errcode).getMessage() : response.get("errmsg").toString(), errcode);
        }
        String token = Md5.md5(UUID.randomUUID().toString());
        setAccessToken(appId, map.get("access_token").toString(), Integer.valueOf(map.get("expires_in").toString()));
        setOpenId(appId, token, map.get("openid").toString());
        setRefreshToken(appId, token, map.get("refresh_token").toString());
        return token;
    }

    @Override
    public UserInfo userInfo(String appId, String token, boolean refresh) {
        if (Util.isEmpty(token)) throw new ServiceException(ErrorMessage.WITHOUT_TOKEN);
        if (Util.isEmpty(appId)) throw new ServiceException(ErrorMessage.WITHOUT_APP_ID);
        String accessToken = getAccessToken(appId);
        String openId = getOpenId(appId, token);
        if (!refresh && Util.isNotEmpty(accessToken) && Util.isNotEmpty(openId)) {
            UserInfo userInfo = _getUserInfo(appId, openId);
            if (Util.isNotEmpty(userInfo)) return userInfo;
        }
        if (Util.isEmpty(accessToken)) {
            // try to refresh access token, may be access_token is expired.
            String[] result = _fallback(appId, token);
            accessToken = result[0];
            openId = result[1];
        }
        // request user_info
        String api = String.format(Api.USER_INFO, accessToken, openId);
        HttpRequest response = HttpRequest.get(api);
        String body = response.body();
        log.info("request user_info: access_token: {}, body: {}, appId: {}, openId: {}", accessToken, body, appId, openId);
        Map<String, Object> map = (Map<String, Object>) JSON.parse(body);
        if (Util.isNotEmpty(map.get("errcode"))) {
            log.info("failed to request user_user：{}", body);
            int errcode = (int) map.get("errcode");
            throw new ServiceException(Util.isNotEmpty(ErrorMessage.get(errcode)) ? ErrorMessage.get(errcode).getMessage() : response.get("errmsg").toString(), errcode);
        }
        UserInfo userInfo = new UserInfo();
        if (Util.isEmpty(map.get("openid"))) userInfo.setOpenId(map.get("openid").toString());
        if (Util.isEmpty(map.get("nickname"))) userInfo.setNickname(map.get("nickname").toString());
        if (Util.isEmpty(map.get("sex"))) userInfo.setSex(map.get("sex").toString());
        if (Util.isEmpty(map.get("province"))) userInfo.setProvince(map.get("province").toString());
        if (Util.isEmpty(map.get("city"))) userInfo.setCity(map.get("city").toString());
        if (Util.isEmpty(map.get("headimgurl"))) userInfo.setAvatar(map.get("headimgurl").toString());
        if (Util.isEmpty(map.get("unionid"))) userInfo.setUnionId(map.get("unionid").toString());
        _setUserInfo(appId, userInfo.getOpenId(), userInfo);
        return userInfo;
    }

    private String[] _fallback(String appId, String token) {
        String refreshToken = getRefreshToken(appId, token);
        if (Util.isEmpty(refreshToken)) {
            throw new ServiceException(ErrorMessage.UNAUTHORIZED);
        }
        // refresh access token
        String api = String.format(Api.REFRESH_ACCESS_TOKEN, appId, refreshToken);
        HttpRequest response = HttpRequest.get(api);
        String body = response.body();
        String accessToken = "";
        String openId = "";
        log.info("refresh access_token: refresh_token: {}, body: {}, appId: {}", refreshToken, body, appId);
        Map<String, Object> map = (Map<String, Object>) JSON.parse(body);
        if (Util.isNotEmpty(map.get("errcode"))) {
            log.info("failed to refresh access_token：{}", body);
            int errcode = (int) map.get("errcode");
            throw new ServiceException(Util.isNotEmpty(ErrorMessage.get(errcode)) ? ErrorMessage.get(errcode).getMessage() : response.get("errmsg").toString(), errcode);
        }
        if (Util.isNotEmpty(map.get("access_token"))) {
            accessToken = map.get("access_token").toString();
            openId = map.get("openid").toString();
            setAccessToken(appId, accessToken, Integer.valueOf(map.get("expires_in").toString()));
            setOpenId(appId, token, openId);
        } else {
            throw new ServiceException(ErrorMessage.UNAUTHORIZED);
        }
        return new String[]{accessToken, openId};
    }

    private void _setUserInfo(String appId, String openId, UserInfo userInfo) {
        String key = Prefix.UNION_ID + appId + "_" + openId;
        redisUtil.setex(key, com.github.wx.ccs.utils.JSON.stringify(userInfo), paramConfig.getExpireTime());
    }

    private UserInfo _getUserInfo(String appId, String openId) {
        String key = Prefix.UNION_ID + appId + "_" + openId;
        String userInfoJson = redisUtil.get(key);
        return Util.isNotEmpty(userInfoJson) ? com.github.wx.ccs.utils.JSON.parse(userInfoJson, UserInfo.class) : null;
    }

    @Override
    public void setRefreshToken(String appId, String token, String refreshToken) {
        String key = Prefix.REFRESH_TOKEN + appId + "_" + token;
        redisUtil.setex(key, refreshToken, paramConfig.getExpireTime());
    }

    @Override
    public void setOpenId(String appId, String token, String openId) {
        String key = Prefix.OPEN_ID + appId + "_" + token;
        redisUtil.setex(key, openId, paramConfig.getExpireTime());
    }

    @Override
    public void setUnionId(String appId, String token, String unionId) {
        String key = Prefix.UNION_ID + appId + "_" + token;
        redisUtil.setex(key, unionId, paramConfig.getExpireTime());
    }

    @Override
    public void setAccessToken(String appId, String accessToken, Integer expire) {
        String key = Prefix.ACCESS_TOKEN + appId;
        redisUtil.setex(key, accessToken, expire - 120);
    }

    @Override
    public String getRefreshToken(String appId, String token) {
        String key = Prefix.REFRESH_TOKEN + appId + "_" + token;
        return redisUtil.get(key);
    }

    @Override
    public String getOpenId(String appId, String token) {
        String key = Prefix.OPEN_ID + appId + "_" + token;
        return redisUtil.get(key);
    }

    @Override
    public String getUnionId(String appId, String token) {
        String key = Prefix.UNION_ID + appId + "_" + token;
        return redisUtil.get(key);
    }

    @Override
    public String getAccessToken(String appId) {
        String key = Prefix.ACCESS_TOKEN + appId;
        return redisUtil.get(key);
    }

}
