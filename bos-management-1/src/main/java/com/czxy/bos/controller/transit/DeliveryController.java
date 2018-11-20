package com.czxy.bos.controller.transit;

import com.czxy.bos.domain.transit.DeliveryInfo;
import com.czxy.bos.service.transit.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;


    @PostMapping
    public ResponseEntity<Void> saveDelivery(DeliveryInfo deliveryInfo){

        deliveryService.saveDelivery(deliveryInfo);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }




}
