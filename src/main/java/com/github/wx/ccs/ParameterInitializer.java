package com.github.wx.ccs;

import com.github.wx.ccs.utils.HttpRequest;
import com.github.wx.ccs.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = {"classpath:application.properties"},encoding = "utf-8")
public class ParameterInitializer implements CommandLineRunner {

	private static Logger log = LoggerFactory.getLogger(WxCCSApplication.class);

	@Value("${spring.user.defined.http.proxy.host}")
	private String proxyHost;
	
	@Value("${spring.user.defined.http.proxy.port}")
	private Integer proxyPort;

	
	@Override
	public void run(String... args) {
		printSettingInformation();
	}

	public void printSettingInformation() {
		if(Util.isNotEmpty(proxyHost) && Util.isNotEmpty(proxyPort)) {
			HttpRequest.proxyHost(proxyHost);
			HttpRequest.proxyPort(proxyPort);
			log.info("http proxy host：{}",proxyHost);
			log.info("http proxy port：{}",proxyPort);
		}
	}

}
