package com.trevor.controller;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trevor.Utils;
import com.trevor.model.ErrorJson;
import com.trevor.model.JsonObject;
import com.trevor.model.MessageJson;
import com.trevor.model.Price;
import com.trevor.model.Product;

@RestController
public class ProductController {

	@Autowired
	public StringRedisTemplate redisTemplate;

	/**
	 * Takes the get web request /product/(id) and returns the product information 
	 * in json format.
	 * 
	 * @param id	Product Id
	 * @return		Product json
	 */
	@RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
	public JsonObject getProduct(@PathVariable("id") Integer id) {
		String name = getProductName(id);
		if (name == null) {
			return new ErrorJson("Well, that's embarasing for Target's api.");
		}

		Price price = getPrice(id);
		if (price == null) {
			return new ErrorJson("Something went wrong getting the price from redis, did you try turning off and back on again?");
		}

		Product product = new Product(id, name, price);

		return product;
	}

	/**
	 * Returns the price object for the given product id from the redis datastore.
	 * 
	 * @param id	Product id
	 * @return		Price object
	 */
	private Price getPrice(Integer id) {
		String priceJson = redisTemplate.opsForValue().get(id.toString());
		Price price;
		if (priceJson == null) {
			//If price isn't in redis, just use this default price
			price = new Price(new BigDecimal(1.23), "USD");
			redisTemplate.opsForValue().set(id.toString(), Utils.convertObjectToJson(price));
		} else {
			//if the price is in redis, convert the json to a price
			ObjectMapper mapper = new ObjectMapper();
			try {
				price = mapper.readValue(priceJson, Price.class);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
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
	protected String getProductName(Integer id) {
		RestTemplate restTemplate = new RestTemplate();
		String fooResourceUrl = "https://api.target.com/products/v3/" + id + "?fields=descriptions&id_type=TCIN&key=43cJWpLjH8Z8oR18KdrZDBKAgLLQKJjz";
		ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl + "/1", String.class);

		//Make sure we get a good status, else return null
		if (response.getStatusCode().value() == HttpStatus.OK.value()) {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root;
			//try to read the body of the response
			try {
				root = mapper.readTree(response.getBody());
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			//getting the online description since that one looked the best.
			JsonNode onlineDescriptionNode = root.findPath("online_description");
			JsonNode onlineDescriptionValueNode = onlineDescriptionNode.path("value");

			//if the node isn't found, return null
			if (onlineDescriptionValueNode.isMissingNode()) {
				return null;
			}

			return onlineDescriptionValueNode.textValue();
		}

		return null;
	}

	/**
	 * Adds the given price json to the database correspoding to the product id in the url.
	 * 
	 * @param id		Product Id
	 * @param priceJson	Price json
	 * @return			Message
	 */
	@RequestMapping(value = "/product/{id}", method = RequestMethod.PUT)
	public JsonObject putProduct(@PathVariable("id") Integer id, @RequestBody String priceJson) {

		ObjectMapper mapper = new ObjectMapper();
		Price price = null;
		//try to read the json sent as a price object
		try {
			price = mapper.readValue(priceJson, Price.class);
		} catch (IOException e) {
			e.printStackTrace();
			return new ErrorJson("Are you sure you sent me a price?");
		}

		//insert the price object into redis using the id given
		redisTemplate.opsForValue().set(id.toString(), Utils.convertObjectToJson(price));

		return new MessageJson("Price update, stay classy");
	}
}
