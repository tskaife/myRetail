package com.trevor.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trevor.model.Message;
import com.trevor.model.Price;
import com.trevor.model.Product;
import com.trevor.service.ProductService;

@RestController
public class ProductController {

	@Autowired
	public ProductService productService;

	/**
	 * Takes the get web request /product/(id) and returns the product information 
	 * in json format.
	 * 
	 * @param id	Product Id
	 * @return		Product json
	 */
	@RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
	public Product getProduct(@PathVariable("id") Integer id) {
		String name = productService.getProductName(id);
		if (name == null) {
			throw new IllegalStateException("For some reason I couldn't get the It couldn't get the product name from Target's api.");
		}

		Price price = productService.getPrice(id);
		if (price == null) {
			throw new IllegalStateException("Something went wrong getting the price from redis, did you try turning off and back on again?");
		}

		Product product = new Product(id, name, price);

		return product;
	}

	/**
	 * Adds the given price json to the database correspoding to the product id in the url.
	 * 
	 * @param id		Product Id
	 * @param priceJson	Price json
	 * @return			Message
	 */
	@RequestMapping(value = "/product/{id}", method = RequestMethod.PUT)
	public Message putProduct(@PathVariable("id") Integer id, @RequestBody String priceJson) {

		ObjectMapper mapper = new ObjectMapper();
		Price price = null;
		//try to read the json sent as a price object
		try {
			price = mapper.readValue(priceJson, Price.class);
		} catch (IOException e) {
			throw new IllegalStateException("Are you sure you sent me a price?", e);
		}
		
		productService.updatePrice(id, price);

		return new Message("Price update, stay classy");
	}
}
