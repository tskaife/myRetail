package com.trevor.controller;

import static org.junit.Assert.*;

import org.junit.Test;

public class ProductControllerTest {
	ProductController productController = new ProductController();

	@Test
	public void testGetProductName() {
		String name = productController.getProductName(13860428);
		
		assertNotNull(name);
		assertEquals(name, "The Big Lebowski [Blu-ray]");
	}

}
