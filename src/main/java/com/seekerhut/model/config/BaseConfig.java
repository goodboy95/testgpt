package com.seekerhut.model.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "config")
@Component

public class BaseConfig {
	private Key key;

	public static class Key {
		private String testKey;

		public String getTestKey() {
			return testKey;
		}

		public void setTestKey(String testKey) {
			this.testKey = testKey;
		}
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}
}