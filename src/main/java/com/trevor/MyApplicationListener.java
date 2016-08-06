package com.trevor;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trevor.model.Price;

public class MyApplicationListener implements SpringApplicationRunListener {
	
	private static final Map<Integer, Price> INITIAL_PRICE_MAP = new HashMap<Integer, Price>(){{
		put(15117729, new Price(15.99, "USD"));
		put(16483589, new Price(15.99, "USD"));
		put(16696652, new Price(15.99, "USD"));
		put(16752456, new Price(15.99, "USD"));
		put(15643793, new Price(15.99, "USD"));
		put(13860428, new Price(13.49, "USD"));
	}};
	

	public MyApplicationListener(SpringApplication application, String[] args) { 
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

		ObjectMapper mapper = new ObjectMapper();
		for(Entry<Integer, Price> idPrice : INITIAL_PRICE_MAP.entrySet()){

			String priceJson = "";
			try {
				priceJson = mapper.writer().writeValueAsString(idPrice.getValue());
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			RedisTemplate<Integer, String> redisTemplate = (RedisTemplate<Integer, String>) context.getBean("redisTemplate");
			redisTemplate.opsForValue().set(idPrice.getKey(), priceJson);
		}
	}
}
