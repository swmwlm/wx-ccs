//index.js
//获取应用实例
const app = getApp()


const appId = 'wxa559e4764b47bbc7'
const secret = 'd6e769c4f24a19f3be17b67ea98e865c'
var token = ''

Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false
  },
  getUnionId() {
    wx.request({
      url: `http://192.168.0.62:8080/wx/mini/v1/${appId}/unionId`,
      method: 'GET',
      data: {
        token: wx.getStorageSync('token')
      },
      success(res) {
        wx.showModal({
          title: 'getUnionId',
          content: JSON.stringify(res.data.data),
        })
      }
    })
  },
  getOpenId () {
    wx.request({
      url: `http://192.168.0.62:8080/wx/mini/v1/${appId}/openId`,
      method: 'GET',
      data: {
        token: wx.getStorageSync('token')
      },
      success (res) {
        wx.showModal({
          title: 'getOpenId',
          content: JSON.stringify(res.data.data),
        })
      }
    })
  },
  getUserInfo: function(e) {
    console.log(e)
    const { encryptedData, iv } = e.detail
    console.log(encryptedData)
    console.log(iv)
    // 请求后台解析
    wx.request({
      url: `http://192.168.0.62:8080/wx/mini/v1/${appId}/decode`,
      method: 'GET',
      data: {
        encryptedData,
        iv,
        token: wx.getStorageSync('token')
      },
      success(res) {
        console.log(res)
        wx.showModal({
          title: 'getPhoneNumber',
          content: JSON.stringify(res.data.data),
        })
      }
    })
  },

  getPhoneNumber (e) {
    const { encryptedData, iv} = e.detail
    console.log(encryptedData)
    console.log(iv)
    // 请求后台解析
    wx.request({
      url: `http://192.168.0.62:8080/wx/mini/v1/${appId}/decode`,
      method: 'GET',
      data: {
        encryptedData,
        iv,
        token: wx.getStorageSync('token')
      },
      success (res) {
        console.log(res)
        wx.showModal({
          title: 'getPhoneNumber',
          content: JSON.stringify(res.data.data),
        })
      }
    })
  },
  onLogin () {
    let that = this
    wx.login({
      success (res) {
        wx.request({
          url: `http://192.168.0.62:8080/wx/mini/v1/${appId}/${secret}/code2Session?code=${res.code}`,
          success (e) {
            console.log(e)
            wx.setStorageSync('token', e.data.data.token)
            token = e.data.data.token
          }
        })
      }
    })
  }
})
