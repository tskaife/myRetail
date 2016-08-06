package com.trevor.controller;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trevor.model.Price;
import com.trevor.model.Product;

@RestController
public class ProductController {
	
	@Autowired
	public StringRedisTemplate redisTemplate;
	
	@RequestMapping(value="/product/{id}",method=RequestMethod.GET)
	public Product getProduct(@PathVariable("id") Integer id){
		
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "https://api.target.com/products/v3/"+id+"?fields=descriptions&id_type=TCIN&key=43cJWpLjH8Z8oR18KdrZDBKAgLLQKJjz";
		ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl + "/1", String.class);
		if(response.getStatusCode().value() == HttpStatus.OK.value()){
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			try {
				root = mapper.readTree(response.getBody());
				JsonNode onlineDescriptionNode = root.findPath("online_description");
				JsonNode onlineDescriptionValueNode = onlineDescriptionNode.path("value");
				String description = onlineDescriptionValueNode.textValue();
				
				String priceJson = redisTemplate.opsForValue().get(id.toString());
				
				Price price;
				if(priceJson == null){
					price = new Price(new BigDecimal(1.23), "USD");
				}else{
					price = mapper.readValue(priceJson, Price.class);
				}
				
				Product product = new Product(id, description, price);
				
				return product;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
}
