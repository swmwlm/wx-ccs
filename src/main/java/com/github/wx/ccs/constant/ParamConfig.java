package com.github.wx.ccs.constant;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:application.properties"},encoding = "utf-8")
public class ParamConfig {

    @Value("${spring.user.defined.http.proxy.host}")
    private String proxyHost;

    @Value("${spring.user.defined.http.proxy.port}")
    private Integer proxyPort;


    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

}
