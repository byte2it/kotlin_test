package com.test.test

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class OrderServiceImpl : OrderService {

  val orders = ConcurrentHashMap<Int, OrderToStore>()

  override fun createOrder(order: OrderToStore) {
    orders[order.id] = order
  }

  override fun getOrder(id: Int) = orders[id]

  override fun getOrders(): List<OrderToStore> =
    orders.values.toList()
}
