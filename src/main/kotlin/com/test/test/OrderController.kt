package com.test.test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class OrderController {
    @Autowired
    private lateinit var orderService: OrderService


    @PostMapping(value = ["/order"])
    fun createOrder(@RequestBody order: Order): ResponseEntity<Response> {
        orderService.createOrder(order)

        var sum: Double = 0.0
        order.items.forEach { item ->
            if (item.product == "apple"){
                val billedQuantity = item.quantity.rem(2) + (item.quantity/2)
               sum += (billedQuantity * 0.60)
            }
            else if (item.product == "orange"){
                val billedQuantity = item.quantity.rem(3) + ((item.quantity/3) * 2)
                sum += (billedQuantity * 0.25)
            }
        }
        val response = Response(sum, order.items)
        return ResponseEntity(response, HttpStatus.CREATED)
    }
}