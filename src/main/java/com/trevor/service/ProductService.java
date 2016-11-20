package com.trevor.service;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trevor.ApplicationConfig;
import com.trevor.Utils;
import com.trevor.model.Customer;

@Service
public class ProductService {

	@Autowired
	public StringRedisTemplate redisTemplate;
	
	@Autowired
	public ApplicationConfig applicationConfig;

	/**
	 * Returns the price object for the given product id from the redis datastore.
	 * 
	 * @param id	Product id
	 * @return		Price object
	 */
	public Customer getPrice(Integer id) {
		String priceJson = redisTemplate.opsForValue().get(id.toString());
		Customer price;
		if (priceJson == null) {
			//If price isn't in redis, just use this default price
			price = new Customer(new BigDecimal(1.23), "USD");
			redisTemplate.opsForValue().set(id.toString(), Utils.convertObjectToJson(price));
		} else {
			//if the price is in redis, convert the json to a price
			ObjectMapper mapper = new ObjectMapper();
			try {
				price = mapper.readValue(priceJson, Customer.class);
			} catch (IOException e) {
				e.printStackTrace();
				throw new IllegalStateException("Couldn't read the price from redis for the given id.", e);
			}
		}
		return price;
	}

	/**
	 * Returns the name of the product retrieved from Target's product api
	 * 
	 * @param id	Product Id
	 * @return		Product Name
	 */
	public String getProductName(Integer id) {
		RestTemplate restTemplate = new RestTemplate();
		String url = String.format(applicationConfig.getTargetProducApitUrl(), id);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		//Make sure we get a good status, else return null
		if (response.getStatusCode().value() == HttpStatus.OK.value()) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			//try to read the body of the response
			try {
				root = mapper.readTree(response.getBody());
			} catch (IOException e) {
				e.printStackTrace();
				throw new IllegalStateException("Had troubles reading the response from Target's api.",e);
			}
			//getting the online description since that one looked the best.
			JsonNode onlineDescriptionNode = root.findPath("online_description");
			JsonNode onlineDescriptionValueNode = onlineDescriptionNode.path("value");

			//if the node isn't found, return null
			if (onlineDescriptionValueNode.isMissingNode()) {
				throw new IllegalStateException("Couldn't find the online_description from Target's api response.");
			}

			return onlineDescriptionValueNode.textValue();
		}

		return null;
	}
	
	/**
	 * Updates the price in redis with the given id.
	 * 
	 * @param id	Product id
	 * @param price	Price object
	 */
	public void updatePrice(Integer id, Customer price){
		//insert the price object into redis using the id given
		redisTemplate.opsForValue().set(id.toString(), Utils.convertObjectToJson(price));
	}
}
