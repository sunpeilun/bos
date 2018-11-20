package com.czxy.bos.controller.take_delivery;

import com.czxy.bos.domain.take_delivery.WayBill;
import com.czxy.bos.domain.vo.DataGridResult;
import com.czxy.bos.service.take_delivery.WayBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wayBill")
public class WayBillController {

    @Autowired
    private WayBillService wayBillService;

    /**
     * 运单快速录入的方法
     *
     * @param wayBill
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveWayBill(WayBill wayBill){
        try{
            wayBillService.saveWayBill(wayBill);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 需要接受页面的哪些信息？
     * 答：waybill的信息
     *
     *
     * @return
     */
    @GetMapping("/pageQuery")
    public ResponseEntity<DataGridResult> pageQuery(WayBill wayBill,Integer page,Integer rows){
        DataGridResult result = wayBillService.conditionQuery(wayBill, page, rows);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }








}
