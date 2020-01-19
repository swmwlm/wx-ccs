package com.github.wx.ccs.constant;

public interface Api {

    String GET_MINI_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    String CODE_TO_SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    String GET_MINI_UNLIMITED_QRCODE = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";

}
