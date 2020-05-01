package com.github.wx.ccs.controller;

import com.github.wx.ccs.entity.ServerResponse;
import com.github.wx.ccs.entity.UserInfo;
import com.github.wx.ccs.service.WxService;
import com.github.wx.ccs.utils.Util;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/v1")
public class WxApplicationController {

    private final WxService wxService;

    public WxApplicationController(WxService wxService) {
        this.wxService = wxService;
    }

    @GetMapping("/{appId}/{secret}/oauth2")
    ServerResponse<String> oauth(@PathVariable("appId") String appId,
                                       @PathVariable("secret") String secret,
                                       @RequestParam("code") String code) throws UnsupportedEncodingException {

        return ServerResponse.success(wxService.ouath2(appId, secret, code));
    }

    @GetMapping("/{appId}/access_token")
    ServerResponse<String> accessToken(@PathVariable("appId") String appId) {

        return ServerResponse.success(wxService.getAccessToken(appId));
    }

    @GetMapping("/{appId}/{token}/userInfo")
    ServerResponse<UserInfo> userInfo(@PathVariable("appId") String appId,
                                      @PathVariable("token") String token,
                                      @RequestParam("refresh") Boolean refresh) {

        return ServerResponse.success(wxService.userInfo(appId, token, Util.isEmpty(refresh) ? false : refresh));
    }

}
