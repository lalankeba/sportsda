package com.laan.sportsda;

import com.laan.sportsda.controller.IndexController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SportsdaApplicationTests {

	@Autowired
	private IndexController indexController;

	@Test
	void contextLoads() {
		assertThat(indexController).isNotNull();
	}

}
