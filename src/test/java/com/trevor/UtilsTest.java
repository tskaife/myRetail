package com.trevor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.trevor.model.Price;

public class UtilsTest {

	@Test
	public void testConvertObjectToJson() {
		Price price = new Price(1, "USD");
		String priceJson = Utils.convertObjectToJson(price);
		
		assertNotNull(priceJson);
		assertEquals(priceJson, "{\"value\":1.00,\"currency_code\":\"USD\"}");
	}

}
