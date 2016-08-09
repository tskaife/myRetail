package com.trevor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="application")
public class ApplicationConfig {

	private String targetProducApitUrl;

	public String getTargetProducApitUrl() {
		return targetProducApitUrl;
	}

	public void setTargetProducApitUrl(String targetProducApitUrl) {
		this.targetProducApitUrl = targetProducApitUrl;
	}
}
