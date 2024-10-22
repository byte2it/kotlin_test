package com.test.test

data class OrderItem(val product: String, val quantity: Int)
data class Order(val id: Int, val items: List<OrderItem>)
data class OrderToStore(val id: Int, val items: List<OrderItem>, val cost: Double)
data class Response(val cost: Double, val summary: List<OrderItem>)
data class ErrorResponse(val error: String, val message: String)
