package com.seekerhut.model.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "spring.application")
@Component

public class BaseConfig {
	private String gptkey;
	private String voicekey;

	public String getGptkey() {
		return gptkey;
	}

	public void setGptkey(String gptkey) {
		this.gptkey = gptkey;
	}

	public String getVoicekey() {
		return voicekey;
	}

	public void setVoicekey(String voicekey) {
		this.voicekey = voicekey;
	}
}