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
        val os = OrderToStore(order.id, order.items, sum)
        orderService.createOrder(os)
        val response = Response(sum, order.items)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    @GetMapping(value = ["/order/{id}"])
    fun getOrder(@PathVariable id: Int): ResponseEntity<Any> {
        val order = orderService.getOrder(id)
        return if (order != null)
            ResponseEntity(order, HttpStatus.OK)
        else
            ResponseEntity(ErrorResponse("Order Not Found", "order '$id' not found"), HttpStatus.NOT_FOUND)
    }

    @GetMapping(value = ["/orders"])
    fun getOrders() =
        orderService.getOrders()
}