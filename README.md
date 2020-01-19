# 微信中控服务

# 获取会话密钥session_key
## 请求接口                           
>GET /wx/mini/v1/{appId}/{secret}/code2Session
## 入参
### 路径参数
| 参数名称   | 说明   | 是否必须   | 默认   | 
| :---- | :---- | :---- | :---- |
| appId   | 小程序appId   | 是   | -   | 
| secret   | 小程序密钥   | 是   | -   | 

### 查询参数
| 参数名称   | 说明   | 是否必须   | 默认   | 
|:----|:----|:----|:----|
| code   | 登录凭证   | 是   | -   | 

## 出参
正确返回

```
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

# 数据解密
## 请求接口
>GET /wx/mini/v1/{appId}/decode
## 入参
### 路径参数
| 参数名称   | 说明   | 是否必须   | 默认   | 
|:----|:----|:----|:----|
| appId   | 小程序appId   | 是   | -   | 

### 查询参数
| 参数名称   | 说明   | 是否必须   | 默认   | 
|:----|:----|:----|:----|
| sessionkey   | 会话凭证   | 是   | -   | 
| encryptedData   | 加密数据   | 是   | -   | 
| iv   | 初始向量   | 是   | -   | 

## 出参
成功返回

```
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

# 获取unionId
## 请求接口
>GET /wx/mini/v1/{appId}/unionId
## 入参
### 路径参数
| 参数名称   | 说明   | 是否必须   | 默认   | 
|:----|:----|:----|:----|
| appId   | 小程序appId   | 是   | -   | 

### 查询参数
| 参数名称   | 说明   | 是否必须   | 默认   | 
|:----|:----|:----|:----|
| token   | 会话凭证   | 是   | -   | 

## 出参
成功返回

```
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

# 获取openId
## 请求接口
>GET /wx/mini/v1/{appId}/openId
## 入参
### 路径参数
| 参数名称   | 说明   | 是否必须   | 默认   | 
|:----|:----|:----|:----|
| appId   | 小程序appId   | 是   | -   | 

### 查询参数
| 参数名称   | 说明   | 是否必须   | 默认   | 
|:----|:----|:----|:----|
| token   | 会话凭证   | 是   | -   | 

## 出参
成功返回

```
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

# 获取access token

## 请求接口
>GET /wx/mini/v1/{appId}/{secret}/access_token
## 入参
### 路径参数
| 参数名称   | 说明   | 是否必须   | 默认   | 
| :---- | :---- | :---- | :---- |
| appId   | 小程序appId   | 是   | -   | 
| secret   | 小程序密钥   | 是   | -   | 

## 出参
成功返回

```
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


# 获取小程序码

## getUnlimited
获取小程序码，适用于需要的码数量极多的业务场景。通过该接口生成的小程序码，永久有效，数量暂无限制。
 
### 请求接口
 > POST /wx/mini/v1/{appId}/{secret}/qrcode/unlimited
 
### 入参
#### 路径参数
| 参数名称   | 说明   | 是否必须   | 默认   | 
| :---- | :---- | :---- | :---- |
| appId   | 小程序appId   | 是   | -   | 
| secret   | 小程序密钥   | 是   | -   | 
#### 请求体参数
| 参数名称   | 类型 | 说明   | 是否必须   | 默认   | 
|:----|:----|:----|:----:|:----|
| scene | String | scene 字段的值会作为 query 参数传递给小程序/小游戏。用户扫描该码进入小程序/小游戏后，开发者可以获取到二维码中的 scene 值，再做处理逻辑。   | 是   | -   |
| page | String | 必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index, 根路径前不要填加 /,不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面   | 否   | 主页   |
| width| Integer | 二维码的宽度，单位px，最小280px，最大1280px | 否 | 430 |
| autoColor| Boolean | 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false | 否 | false |
| lineColor | json字符串 | auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示 | 否 | {"r":0,"g":0,"b":0} |
| isHyaline | Boolean | 是否需要透明底色，为 true 时，生成透明底色的小程序 | 否 | false |



### 出参

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