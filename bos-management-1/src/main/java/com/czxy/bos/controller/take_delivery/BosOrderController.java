package com.czxy.bos.controller.take_delivery;

import com.czxy.bos.domain.take_delivery.Order;
import com.czxy.bos.service.take_delivery.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bosOrder")
public class BosOrderController {

    @Autowired
    private OrderService orderService;



    @PostMapping
    public ResponseEntity<Void> saveOrder(@RequestBody Order order){

        orderService.saveOrder(order);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }


//    @GetMapping
//    public ResponseEntity<Order> findOrderByOrderNum(String orderNum){
//
//
//
//    }



}
