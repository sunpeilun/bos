package com.czxy.bos.controller.transit;

import com.czxy.bos.domain.vo.DataGridResult;
import com.czxy.bos.service.transit.TransitInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transitInfo")
public class TransitInfoController {

    @Autowired
    private TransitInfoService transitInfoService;

    @PostMapping
    public ResponseEntity<Void> saveTransitInfo(String wayBillIds){
        try {
            transitInfoService.saveTransitInfo(wayBillIds);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping
    public ResponseEntity<DataGridResult> findTransitInfoByPage(Integer page,Integer rows){
        try {
            DataGridResult result = transitInfoService.findTransitInfoByPage(page, rows);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }






}
