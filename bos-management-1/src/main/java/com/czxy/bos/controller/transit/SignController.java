package com.czxy.bos.controller.transit;

import com.czxy.bos.domain.transit.SignInfo;
import com.czxy.bos.service.transit.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign")
public class SignController {

    @Autowired
    private SignService signService;

    @PostMapping
    public ResponseEntity<Void> saveSing(SignInfo signInfo){

        signService.saveSign(signInfo);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
