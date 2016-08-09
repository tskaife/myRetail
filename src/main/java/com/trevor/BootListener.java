package com.trevor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trevor.model.Price;

public class BootListener implements SpringApplicationRunListener {
	
	private static final Map<Integer, Price> INITIAL_PRICE_MAP = new HashMap<Integer, Price>(){{
		put(15117729, new Price(15.99, "USD"));
		put(16483589, new Price(15.99, "USD"));
		put(16696652, new Price(15.99, "USD"));
		put(16752456, new Price(15.99, "USD"));
		put(15643793, new Price(15.99, "USD"));
		put(13860428, new Price(13.49, "USD"));
	}};
	

	public BootListener(SpringApplication application, String[] args) { 
	}

	@Override
	public void started() {
	}

	@Override
	public void environmentPrepared(ConfigurableEnvironment environment) {
	}

	@Override
	public void contextPrepared(ConfigurableApplicationContext context) {
	}

	@Override
	public void contextLoaded(ConfigurableApplicationContext context) {
	}

	@Override
	public void finished(ConfigurableApplicationContext context, Throwable exception) {
		//Seeing if this works as an acceptable way to make sure redis is populated with prices

		for(Entry<Integer, Price> idPrice : INITIAL_PRICE_MAP.entrySet()){
			String priceJson = Utils.convertObjectToJson(idPrice.getValue());
			StringRedisTemplate redisTemplate = context.getBean(StringRedisTemplate.class);
			redisTemplate.opsForValue().set(idPrice.getKey().toString(), priceJson);
		}
	}
}
