package com.github.wx.ccs.constant;

public interface Api {

    String GET_MINI_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

    String CODE_TO_SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    String GET_MINI_UNLIMITED_QRCODE = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=%s";

    /**
     * 第一步：授权登录
     * 通过 code 获取 access_token
     * <br/>
     * 参数说明
     * <br/>
     * 参数	      是否必须	说明
     * <br/>
     * appid	    是	  应用唯一标识，在微信开放平台提交应用审核通过后获得
     * <br/>
     * secret	    是	  应用密钥 AppSecret，在微信开放平台提交应用审核通过后获得
     * <br/>
     * code	        是	  填写第一步获取的 code 参数
     * <br/>
     * grant_type	是	  填 authorization_code
     * <br/>
     * 返回说明
     * <br/>
     * 正确的返回：
     * <pre>
     * {
     *   "access_token": "ACCESS_TOKEN",
     *   "expires_in": 7200,
     *   "refresh_token": "REFRESH_TOKEN",
     *   "openid": "OPENID",
     *   "scope": "SCOPE"
     * }
     * </pre>
     */
    String OAUTH2 = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    /**
     * 刷新access_token
     */
    String REFRESH_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s";

    /**
     * 第二步 获取用户个人信息（UnionID 机制）
     */
    String USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";
}
