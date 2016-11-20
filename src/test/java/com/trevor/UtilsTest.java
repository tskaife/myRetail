package com.trevor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.trevor.model.Customer;

public class UtilsTest {

	@Test
	public void testConvertObjectToJson() {
		Customer price = new Customer(1, "USD");
		String priceJson = Utils.convertObjectToJson(price);
		
		assertNotNull(priceJson);
		assertEquals(priceJson, "{\"value\":1.00,\"currency_code\":\"USD\"}");
	}

}
