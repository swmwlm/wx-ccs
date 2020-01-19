package com.github.wx.ccs.controller;

import com.alibaba.fastjson.JSON;
import com.github.wx.ccs.constant.Api;
import com.github.wx.ccs.entity.ServerResponse;

import com.github.wx.ccs.enums.ErrorMessage;
import com.github.wx.ccs.service.WxMiniService;
import com.github.wx.ccs.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.InvalidAlgorithmParameterException;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/mini/v1")
public class WxMiniApplicationController {

    private Logger log = LoggerFactory.getLogger(WxMiniApplicationController.class);

    @Autowired
    WxMiniService wxMiniService;

    @GetMapping("/{appId}/unionId")
    public ServerResponse unionId (@PathVariable("appId") String appId,
                                   @RequestParam(value = "token", required = false) String token) {
        if (Util.isEmpty(token)) return ServerResponse.fail(ErrorMessage.WITHOUT_TOKEN);
        String unionId = wxMiniService.getUnionId(appId, token);
        if (Util.isEmpty(unionId)) return ServerResponse.fail(ErrorMessage.UNAUTHORIZED);
        return ServerResponse.success(unionId);
    }

    @GetMapping("/{appId}/openId")
    public ServerResponse openId (@PathVariable("appId") String appId,
                                  @RequestParam("token") String token) {
        if (Util.isEmpty(token))  return ServerResponse.fail(ErrorMessage.WITHOUT_TOKEN);
        String openId = wxMiniService.getOpenId(appId, token);
        if (Util.isEmpty(openId)) return ServerResponse.fail(ErrorMessage.UNAUTHORIZED);
        return ServerResponse.success(wxMiniService.getOpenId(appId, token));
    }

    @GetMapping("/{appId}/decode")
    public ServerResponse decode (@PathVariable("appId") String appId,
                                  @RequestParam(value = "token", required = false) String token,
                                  @RequestParam(value = "encryptedData", required = false) String encryptedData,
                                  @RequestParam(value = "iv", required = false) String iv)  {
        if (Util.isEmpty(token)) return ServerResponse.fail(ErrorMessage.WITHOUT_TOKEN);
        if (Util.isEmpty(encryptedData)) return ServerResponse.fail(ErrorMessage.WITHOUT_ENCRYPT_DATA);
        if (Util.isEmpty(iv)) return ServerResponse.fail(ErrorMessage.WITHOUT_IV);
        String sessionKey = wxMiniService.getSessionKey(appId, token);
        if (Util.isEmpty(sessionKey)) return ServerResponse.fail(ErrorMessage.UNAUTHORIZED);
        try {
            String body = WxMiniDecoder.decode(appId, encryptedData, sessionKey, iv);
            return ServerResponse.success(JSON.parse(body));
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return ServerResponse.fail(ErrorMessage.DECODE_FAILED);
    }

    @GetMapping("/{appId}/{secret}/code2Session")
    public ServerResponse code2Session (@PathVariable("appId") String appId,
                                        @PathVariable("secret") String secret,
                                        @RequestParam("code") String code) throws UnsupportedEncodingException {
        String api = String.format(Api.CODE_TO_SESSION, appId, secret, URLDecoder.decode(code,"UTF-8"));
        HttpRequest response = HttpRequest.get(api);
        String body = response.body();
        log.info("codeToSession返回: code: {}, body: {}, appId: {}, secret: {}", code, body, appId, secret);
        Map map = (Map) JSON.parse(body);
        if (Util.isNotEmpty(map.get("errcode"))) {
            log.info("获取sessionKey失败：{}", body);
            int errcode = (int) map.get("errcode");
            return ServerResponse.fail(errcode,  Util.isNotEmpty(ErrorMessage.get(errcode)) ? ErrorMessage.get(errcode).getMessage() : response.get("errmsg").toString());
        }
        String sessionKey = (String) map.get("session_key");
        String token = Md5.md5(UUID.randomUUID().toString());
        String unionId = (String) map.get("unionid");
        String openId = (String) map.get("openid");
        wxMiniService.setOpenId(appId, token, openId);
        if (Util.isNotEmpty(unionId)) wxMiniService.setUnionId(appId, token, unionId);
        wxMiniService.setSessionKey(appId, token, sessionKey);
        return ServerResponse.success(token);
    }

    @GetMapping("/{appId}/{secret}/access_token")
    public ServerResponse getAccessToken (@PathVariable("appId") String appId,
                                          @PathVariable("secret") String secret) {
        return wxMiniService.getAccessToken(appId, secret);
    }

    @PostMapping(value = "/{appId}/{secret}/qrcode/unlimited")
    public ServerResponse getUnlimitedQrcode (@PathVariable("appId") String appId,
                                              @PathVariable("secret") String secret,
                                              @RequestBody Map<String, Object> params) throws IOException {
        if (params.get("scene") == null) return ServerResponse.fail(ErrorMessage.WITHOUT_SCENE);
        ServerResponse response = wxMiniService.getAccessToken(appId, secret);
        if (response.getSuccess()) {
            String accessToken = (String) response.getData();
            return wxMiniService.getUnlimitedQrcode(accessToken, params);
        }
        return response;
    }


}
