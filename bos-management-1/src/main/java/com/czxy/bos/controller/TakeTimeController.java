package com.czxy.bos.controller;

import com.czxy.bos.domain.base.TakeTime;
import com.czxy.bos.service.TakeTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/taketime")
public class TakeTimeController {

    @Autowired
    private TakeTimeService  takeTimeService;


    @GetMapping("/findAll")
    public ResponseEntity<List<TakeTime>> findAllTakeTimes(){
        try {
            List<TakeTime> list = takeTimeService.findAllTakeTime();
            return new ResponseEntity<>(list,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }




}
