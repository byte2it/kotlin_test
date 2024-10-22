package com.test.test

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class OrderServiceImpl : OrderService {

  //val orders = ConcurrentHashMap<Int, Order>()

  override fun createOrder(order: Order) {
    //orders[order.id] = order
  }
}
