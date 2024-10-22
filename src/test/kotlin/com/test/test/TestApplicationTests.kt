package com.test.test

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class TestApplicationTests {

	@Autowired
	lateinit var mockMvc: MockMvc

	@Autowired lateinit var mapper: ObjectMapper

	@Test
	fun `we should POST an order by id and items by offers`() {

		val requestBody = """{"id": 17,"items":[{"product": "orange","quantity": 3},{"product": "apple","quantity" : 2}]}"""
		val responseBody = """{"cost":1.1,"summary":[{"product":"orange","quantity":3},{"product":"apple","quantity":2}]}"""

		mockMvc.perform(MockMvcRequestBuilders
			.post("/order")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.content().string(responseBody))
	}

	@Test
	fun `we should POST an order by id and items without offers`() {

		val requestBody = """{"id": 17,"items":[{"product": "orange","quantity": 2},{"product": "apple","quantity" : 1}]}"""
		val responseBody = """{"cost":1.1,"summary":[{"product":"orange","quantity":2},{"product":"apple","quantity":1}]}"""

		mockMvc.perform(MockMvcRequestBuilders
			.post("/order")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.content().string(responseBody))
	}

	@Test
	fun `we should POST an order by id and some items by offers and some without`() {

		val requestBody = """{"id": 17,"items":[{"product": "orange","quantity": 5},{"product": "apple","quantity" : 3}]}"""
		val responseBody = """{"cost":2.2,"summary":[{"product":"orange","quantity":5},{"product":"apple","quantity":3}]}"""

		mockMvc.perform(MockMvcRequestBuilders
			.post("/order")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.content().string(responseBody))
	}

	@Test
	fun `we should POST an orders by id and get items by id and all items`() {

		val requestBody = """{"id": 17,"items":[{"product": "orange","quantity": 5},{"product": "apple","quantity" : 3}]}"""
		val responseBody = """{"cost":2.2,"summary":[{"product":"orange","quantity":5},{"product":"apple","quantity":3}]}"""

		mockMvc.perform(MockMvcRequestBuilders
			.post("/order")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.content().string(responseBody))


		val requestBody2 = """{"id": 272,"items":[{"product": "orange","quantity": 5},{"product": "apple","quantity" : 3}]}"""
		val responseBody2 = """{"cost":2.2,"summary":[{"product":"orange","quantity":5},{"product":"apple","quantity":3}]}"""

		mockMvc.perform(MockMvcRequestBuilders
			.post("/order")
			.accept(MediaType.APPLICATION_JSON)
			.contentType(MediaType.APPLICATION_JSON)
			.content(requestBody2))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful)
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.content().string(responseBody2))


		mockMvc.perform(get("/order/17"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("\$.cost").value(2.2))
			.andExpect(jsonPath("\$.items[0].quantity").value(5))
			.andDo(MockMvcResultHandlers.print())

		mockMvc.perform(get("/order/272"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("\$.cost").value(2.2))
			.andExpect(jsonPath("\$.items[1].quantity").value(3))
			.andDo(MockMvcResultHandlers.print())

		mockMvc.perform(get("/orders"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("\$[0].cost").value(2.2))
			.andExpect(jsonPath("\$[1].items[1].quantity").value(3))
			.andExpect(jsonPath("\$[1].items[0].product").value("orange"))
			.andDo(MockMvcResultHandlers.print())
	}
}
