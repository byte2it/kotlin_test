package com.test.test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.concurrent.ConcurrentHashMap

@RestController
class OrderController {
    @Autowired
    private lateinit var orderService: OrderService


    @PostMapping(value = ["/order"])
    fun createCustomer(@RequestBody order: Order): ResponseEntity<Response> {
        orderService.createOrder(order)

        var sum: Double = 0.0
        order.items.forEach { item ->
            if (item.product == "apple"){
               sum += (item.quantity * 0.6)
            }
            else if (item.product == "orange"){
                sum += (item.quantity * 0.25)
            }
        }
        val response = Response(sum, order.items)
        return ResponseEntity(response, HttpStatus.CREATED)
    }
}