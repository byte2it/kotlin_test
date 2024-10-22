package com.test.test

data class OrderItem(val product: String, val quantity: Int)
data class Order(val id: Int, val items: List<OrderItem>)
data class Response(val cost: Double, val summary: List<OrderItem>)
