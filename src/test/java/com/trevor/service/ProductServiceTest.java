package com.trevor.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.yaml.snakeyaml.Yaml;

import com.trevor.ApplicationConfig;

public class ProductServiceTest {
	ProductService productService;

	@Before
	public void setUp() throws Exception {
		productService = new ProductService();
		Yaml yaml = new Yaml();
		InputStream ios = this.getClass().getClassLoader().getResourceAsStream("application.yaml");
		try {
			Map<String, Object> config = (Map<String, Object>) yaml.load(ios);
			ApplicationConfig applicationConfig = new ApplicationConfig();
			applicationConfig.setTargetProducApitUrl((String) ((Map<String, Object>)config.get("application")).get("targetProducApitUrl"));
			productService.applicationConfig = applicationConfig;
		} finally {
			ios.close();
		}
	}

	@Test
	public void testGetProductName() {
		String name = productService.getProductName(13860428);

		assertNotNull(name);
		assertEquals(name, "The Big Lebowski [Blu-ray]");
	}

}
