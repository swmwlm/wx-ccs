 # 微信中控服务

 <!-- TOC -->

 - [移动应用](#移动应用)
     - [授权登录](#授权登录)
     - [获取用户信息](#获取用户信息)
 - [小程序](#小程序)
     - [获取会话密钥session_key](#获取会话密钥session_key)
     - [数据解密](#数据解密)
     - [获取unionId](#获取unionid)
     - [获取openId](#获取openid)
     - [获取access token](#获取access-token)
     - [获取小程序码](#获取小程序码)

 <!-- /TOC -->

 # 移动应用

 ## 授权登录

 第三方发起微信授权登录请求，微信用户允许授权第三方应用后，微信会拉起应用或重定向到第三方网站，并且带上授权临时票据code参数，应用服务调用中控服务授权登录接口初始化access_token、openId、refresh_token，返回token用于后续获取用户信息

 ### 请求接口
 > GET /wx/v1/{appId}/{secret}/ouath2
 ### 入参
 #### 路径参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 | :---- | :---- | :---- | :---- |
 | appId   | appId  | 是   | -   |
 | secret   | appSecret   | 是   | -   |

 #### 查询参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 |:----|:----|:----|:----|
 | code   | 用户同意授权后获取到的code   | 是   | -   |

 ### 出参
 正确返回

 ```javascript
 {
   code: 200,
   msg: 'ok',
   data: 'token',
   success: true
 }
 ```
 错误返回


 ## 获取用户信息

 传递第一步获取到的token，由中控服务拉取用户信息

 ### 请求接口
 > GET /wx/v1/{appId}/{token}/userInfo
 ### 入参
 #### 路径参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 | :---- | :---- | :---- | :---- |
 | appId   | appId  | 是   | -   |
 | token   | 授权登录成功后服务返回的token   | 是   | -   |

 #### 查询参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 |:----|:----|:----|:----|
 | refresh   | 是否获取最新的用户信息   | 否   | false |

 ### 出参
 正确返回

 ```javascript
 {
   code: 200,
   msg: 'ok',
   data: {
     openId: '普通用户的标识，对当前开发者帐号唯一',
     nickname: '普通用户昵称',
     sex: '普通用户性别，1 为男性，2 为女性',
     province: '普通用户个人资料填写的省份',
     city: '普通用户个人资料填写的城市',
     country: '国家，如中国为 CN',
     avatar: '用户头像，最后一个数值代表正方形头像大小（有 0、46、64、96、132 数值可选，0 代表 640*640 正方形头像），用户没有头像时该项为空',
     unionId: '用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的 unionid 是唯一的。'
   },
   success: true
 }
 ```
 错误返回

 | 错误编码|错误消息|原因|解决方案|
 | :---- | :---- | :---- | :---- |
 | 1000   | unauthorized  | 未进行授权登录或者access_token已过期   | -   |

 # 小程序

 ## 获取会话密钥session_key
 ### 请求接口
 >GET /wx/mini/v1/{appId}/{secret}/code2Session
 ### 入参
 #### 路径参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 | :---- | :---- | :---- | :---- |
 | appId   | 小程序appId   | 是   | -   |
 | secret   | 小程序密钥   | 是   | -   |

 #### 查询参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 |:----|:----|:----|:----|
 | code   | 用户同意授权后获取到的code   | 是   | -   |

 ### 出参
 正确返回

 ```javascript
 {
   code: 200,
   msg: 'ok',
   data: 'token',
   success: true
 }
 ```
 错误返回

 | 错误编码|错误消息|原因|解决方案|
 | :---- | :---- | :---- | :---- |
 | 40013   | invalid parameter: appId   | appid错误   | 检查appId是否正确   |
 | 40029   | invalid parameter: code   | code错误   | 检查code是否正确   |
 | 40125   | invalid parameter: secret   | app秘钥错误   | 检查app secret是否正确   |

 ## 数据解密
 ### 请求接口
 >GET /wx/mini/v1/{appId}/decode
 ### 入参
 #### 路径参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 |:----|:----|:----|:----|
 | appId   | 小程序appId   | 是   | -   |

 #### 查询参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 |:----|:----|:----|:----|
 | sessionkey   | 会话凭证   | 是   | -   |
 | encryptedData   | 加密数据   | 是   | -   |
 | iv   | 初始向量   | 是   | -   |

 ### 出参
 成功返回

 ```javascript
 {
   code: 200,
   success: true,
   msg: 'ok',
   data: {
     // 解密数据
   }
 }
 ```
 错误返回

 | 错误编码   | 错误消息   | 原因   | 解决方案   |
 |:----|:----|:----|:----|
 | 40013   | invalid parameter: appId   | appid错误   | 检查appId是否正确   |
 | 40029   | invalid parameter: code   | code错误   | 检查code是否正确   |
 | 40125   | invalid parameter: secret   | app秘钥错误   | 检查app secret是否正确   |
 | 1000   | unauthorized   | 未授权   | 传递的token无效   |
 | 1001   | without parameter: token   | 未传递token   |    |
 | 1002   | without parameter: encrypted data   | 未传递加密数据   |    |
 | 1003   | without parameter: iv   | 未传递初始向量   |    |
 | 1004   | error: decode failed   | 解码失败   |    |

 ## 获取unionId
 ### 请求接口
 >GET /wx/mini/v1/{appId}/unionId
 ### 入参
 #### 路径参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 |:----|:----|:----|:----|
 | appId   | 小程序appId   | 是   | -   |

 #### 查询参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 |:----|:----|:----|:----|
 | token   | 会话凭证   | 是   | -   |

 ### 出参
 成功返回

 ```javascript
 {
   code: 200,
   success: true,
   msg: 'ok',
   data: 1212313213132 // unionId
 }
 ```
 错误返回

 | 错误编码   | 错误消息   | 原因   | 解决方案   |
 |:----|:----|:----|:----|
 | 1000   | unauthorized   | 未授权   | 传递的token无效   |
 | 1001   | without parameter: token   | 未传递token   |    |

 ## 获取openId
 ### 请求接口
 >GET /wx/mini/v1/{appId}/openId
 ### 入参
 #### 路径参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 |:----|:----|:----|:----|
 | appId   | 小程序appId   | 是   | -   |

 #### 查询参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 |:----|:----|:----|:----|
 | token   | 会话凭证   | 是   | -   |

 ### 出参
 成功返回

 ```javascript
 {
   code: 200,
   success: true,
   msg: 'ok',
   data: 1212313213132 // openId
 }
 ```
 错误返回

 | 错误编码   | 错误消息   | 原因   | 解决方案   |
 |:----|:----|:----|:----|
 | 1000   | unauthorized   | 未授权   | 传递的token无效   |
 | 1001   | without parameter: token   | 未传递token   |    |

 ## 获取access token

 ### 请求接口
 >GET /wx/mini/v1/{appId}/{secret}/access_token
 ### 入参
 #### 路径参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 | :---- | :---- | :---- | :---- |
 | appId   | 小程序appId   | 是   | -   |
 | secret   | 小程序密钥   | 是   | -   |

 ### 出参
 成功返回

 ```javascript
 {
   code: 200,
   success: true,
   msg: 'ok',
   data: 1212313213132 // access token
 }
 ```
 错误返回

 | 错误编码|错误消息|原因|解决方案|
 | :---- | :---- | :---- | :---- |
 | 40013   | invalid parameter: appId   | appid错误   | 检查appId是否正确   |
 | 40125   | invalid parameter: secret   | app秘钥错误   | 检查app secret是否正确   |


 ## 获取小程序码

 ### getUnlimited
 获取小程序码，适用于需要的码数量极多的业务场景。通过该接口生成的小程序码，永久有效，数量暂无限制。

 #### 请求接口
  > POST /wx/mini/v1/{appId}/{secret}/qrcode/unlimited

 #### 入参
 ##### 路径参数
 | 参数名称   | 说明   | 是否必须   | 默认   |
 | :---- | :---- | :---- | :---- |
 | appId   | 小程序appId   | 是   | -   |
 | secret   | 小程序密钥   | 是   | -   |
 ##### 请求体参数
 | 参数名称   | 类型 | 说明   | 是否必须   | 默认   |
 |:----|:----|:----|:----:|:----|
 | scene | String | scene 字段的值会作为 query 参数传递给小程序/小游戏。用户扫描该码进入小程序/小游戏后，开发者可以获取到二维码中的 scene 值，再做处理逻辑。   | 是   | -   |
 | page | String | 必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index, 根路径前不要填加 /,不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面   | 否   | 主页   |
 | width| Integer | 二维码的宽度，单位px，最小280px，最大1280px | 否 | 430 |
 | autoColor| Boolean | 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false | 否 | false |
 | lineColor | json字符串 | auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示 | 否 | {"r":0,"g":0,"b":0} |
 | isHyaline | Boolean | 是否需要透明底色，为 true 时，生成透明底色的小程序 | 否 | false |



 #### 出参

 成功返回

 ```javascript
 {
     code: 200,
     success: true,
     msg: "ok",
     data: 'base64' // 返回base64图片
 }
 ```
 错误返回

 | 错误编码|错误消息|原因|解决方案|
 | :---- | :---- | :---- | :---- |
 | 40013   | invalid parameter: appId   | appid错误   | 检查appId是否正确   |
 | 40125   | invalid parameter: secret   | app秘钥错误   | 检查app secret是否正确   |
 | 40169   | invalid length for scene, or the data is not json string hint | scene参数有误| - |