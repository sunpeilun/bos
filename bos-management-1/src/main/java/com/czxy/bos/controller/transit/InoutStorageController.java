package com.czxy.bos.controller.transit;

import com.czxy.bos.domain.transit.InOutStorageInfo;
import com.czxy.bos.service.transit.InoutStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inoutStorage")
public class InoutStorageController {

    @Autowired
    private InoutStorageService inoutStorageService;

    @PostMapping
    public ResponseEntity<String> saveInoutStorage(InOutStorageInfo inOutStorageInfo){

        try {
            String info = inoutStorageService.saveInoutStorage(inOutStorageInfo);
            return new ResponseEntity<>(info,HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
