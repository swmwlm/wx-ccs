package com.github.wx.ccs.entity;

import java.util.Objects;

public class UserInfo {

    // 普通用户的标识，对当前开发者帐号唯一
    private String openId;
    // 普通用户昵称
    private String nickname;
    // 普通用户性别，1 为男性，2 为女性
    private String sex;
    // 普通用户个人资料填写的省份
    private String province;
    // 普通用户个人资料填写的城市
    private String city;
    // 国家，如中国为 CN
    private String country;
    // 用户头像，最后一个数值代表正方形头像大小（有 0、46、64、96、132 数值可选，0 代表 640*640 正方形头像），用户没有头像时该项为空
    private String avatar;
    // 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的 unionid 是唯一的。
    private String unionId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "openId='" + openId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex='" + sex + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", avatar='" + avatar + '\'' +
                ", unionId='" + unionId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(openId, userInfo.openId) &&
                Objects.equals(nickname, userInfo.nickname) &&
                Objects.equals(sex, userInfo.sex) &&
                Objects.equals(province, userInfo.province) &&
                Objects.equals(city, userInfo.city) &&
                Objects.equals(country, userInfo.country) &&
                Objects.equals(avatar, userInfo.avatar) &&
                Objects.equals(unionId, userInfo.unionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openId, nickname, sex, province, city, country, avatar, unionId);
    }

}
