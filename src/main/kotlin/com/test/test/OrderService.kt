package com.test.test

interface OrderService {
  fun createOrder(order: OrderToStore)
  fun getOrder(id: Int): OrderToStore?
  fun getOrders(): List<OrderToStore>
}
