package com.test.test

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class TestApplicationTests {

	@Autowired
	lateinit var mockMvc: MockMvc

	@Autowired lateinit var mapper: ObjectMapper

	@Test
	fun `we should POST an order by id and items`() {

		val requestBody = """{"id": 17,"items":[{"product": "orange","quantity": 1},{"product": "apple","quantity" : 3}]}"""
		val responseBody = """{"cost":2.05,"summary":[{"product":"orange","quantity":1},{"product":"apple","quantity":3}]}"""

		mockMvc.perform(MockMvcRequestBuilders
			.post("/order")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.content().string(responseBody))
	}
}
