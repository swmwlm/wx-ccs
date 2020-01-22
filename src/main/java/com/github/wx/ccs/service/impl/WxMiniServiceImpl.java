package com.github.wx.ccs.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.wx.ccs.constant.Api;
import com.github.wx.ccs.constant.ParamConfig;
import com.github.wx.ccs.constant.Prefix;
import com.github.wx.ccs.entity.ServerResponse;
import com.github.wx.ccs.enums.ErrorMessage;
import com.github.wx.ccs.service.WxMiniService;
import com.github.wx.ccs.utils.HttpRequest;
import com.github.wx.ccs.utils.RedisUtil;
import com.github.wx.ccs.utils.Util;
import io.netty.buffer.ByteBufInputStream;
import io.netty.handler.codec.base64.Base64Encoder;
import org.apache.catalina.Server;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.http.HTTPBinding;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static io.netty.buffer.Unpooled.buffer;

@Service
public class WxMiniServiceImpl implements WxMiniService {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ParamConfig paramConfig;

    @Override
    public void setSessionKey(String appId, String token, String sessionKey) {
        String key = appId + "_" + Prefix.MINI_SESSION_KEY + token;
        redisUtil.setex(key, sessionKey, paramConfig.getMiniProgramSessionKeyExpireTime());
    }

    @Override
    public void setOpenId(String appId, String token, String openId) {
        String key = appId + "_" + Prefix.MINI_OPEN_ID + token;
        redisUtil.setex(key, openId, paramConfig.getMiniProgramSessionKeyExpireTime());
    }

    @Override
    public void setUnionId(String appId, String token, String unionId) {
        String key = appId + "_" + Prefix.MINI_UNION_ID + token;
        redisUtil.setex(key, unionId, paramConfig.getMiniProgramSessionKeyExpireTime());
    }

    @Override
    public void setAccessToken(String appId, String accessToken, Integer expire) {
        String key = appId + "_" + Prefix.MINI_ACCESS_TOKEN;
        // 早两分钟过期
        redisUtil.setex(key, accessToken, expire - 120);
    }

    @Override
    public String getSessionKey(String appId, String token) {
        String key = appId + "_" + Prefix.MINI_SESSION_KEY + token;
        return redisUtil.get(key);
    }

    @Override
    public String getOpenId(String appId, String token) {
        String key = appId + "_" + Prefix.MINI_OPEN_ID + token;
        return redisUtil.get(key);
    }

    @Override
    public String getUnionId(String appId, String token) {
        String key = appId + "_" + Prefix.MINI_UNION_ID + token;
        return redisUtil.get(key);
    }

    @Override
    public ServerResponse getAccessToken(String appId, String secret) {
        String key = appId + "_" + Prefix.MINI_ACCESS_TOKEN;
        String accessToken = redisUtil.get(key);
        if (Util.isNotEmpty(accessToken)) return ServerResponse.success(accessToken);
        String requestUrl = String.format(Api.GET_MINI_ACCESS_TOKEN, appId, secret);
        HttpRequest response = HttpRequest.get(requestUrl);
        String body = response.body();
        Map map = (Map) JSON.parse(body);
        if (Util.isNotEmpty(map.get("errcode"))) {
            int errcode = (int) map.get("errcode");
            return ServerResponse.fail(errcode, ErrorMessage.get(errcode).getMessage());
        }
        accessToken = map.get("access_token").toString();
        this.setAccessToken(appId, accessToken, Integer.valueOf(map.get("expires_in").toString()));
        return ServerResponse.success(accessToken);
    }

    @Override
    public ServerResponse getUnlimitedQrcode(String accessToken,  Map<String, Object> params) throws IOException {
        if (Util.isEmpty(accessToken)) ServerResponse.fail(ErrorMessage.WITHOUT_ACCESS_TOKEN);
        String requestUrl = String.format(Api.GET_MINI_UNLIMITED_QRCODE, accessToken);
        HttpRequest httpRequest = HttpRequest.post(requestUrl);
        httpRequest.contentType("application/json");
        httpRequest.send(JSON.toJSONString(params));
        BufferedInputStream in = httpRequest.buffer();
        if (Util.isEmpty(in)) ServerResponse.fail(ErrorMessage.GET_MINI_QRCODE_FAILED);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int len;
        byte[] buffer = new byte[1024];
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        out.close();
        in.close();
        try {
            String json = new String(out.toByteArray());
            Map<String, Object> response = (Map) JSON.parse(json);
            if (Util.isNotEmpty(response.get("errcode"))) {
                int errcode = (int) response.get("errcode");
                return ServerResponse.fail(errcode, Util.isNotEmpty(ErrorMessage.get(errcode)) ? ErrorMessage.get(errcode).getMessage() : response.get("errmsg").toString());
            }
        } catch (Exception e) { }
        String base64 = Base64.encodeBase64String(out.toByteArray());
        return ServerResponse.success("data:image/png;base64," + base64);
    }

}
