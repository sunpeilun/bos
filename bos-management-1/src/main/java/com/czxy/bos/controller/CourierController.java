package com.czxy.bos.controller;

import com.czxy.bos.domain.base.Courier;
import com.czxy.bos.domain.vo.DataGridResult;
import com.czxy.bos.service.CourierService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courier")
public class CourierController {

    @Autowired
    private CourierService courierService;

//    @RequiresRoles()// 必须具备xxx角色才可以访问这个方法
    @RequiresPermissions("courier:add")
    @PostMapping
    public ResponseEntity<Void> addCourier(Courier courier){
        try {
            courierService.addCourier(courier);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    /**
     * 无条件分页查询
     * @param page
     * @param rows
     * @return
     */
//    @GetMapping
//    public ResponseEntity<DataGridResult> findCourierByPage(Integer page,Integer rows){
//        try {
//            DataGridResult result = courierService.findCourierByPage(page, rows);
//            return new ResponseEntity<>(result,HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    /**
     * 有条件查询
     * @param page
     * @param rows
     * @param courier
     * @return
     */
    @GetMapping
    public ResponseEntity<DataGridResult> findCourierByPageAndCondition(Integer page,Integer rows,Courier courier){
        try {
            DataGridResult result = courierService.findCourierByPageAndCondition(page, rows,courier);
            return new ResponseEntity<>(result,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @DeleteMapping("/{idStr}")
    public ResponseEntity<Void> deleteCouriers(@PathVariable("idStr") String idStr){
        try {
            courierService.deleteCouriers(idStr);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    /**
     * 查找所有未关联定区的快递员
     *
     *
     */
    @GetMapping("/findNoAssociationCouriers")
    public ResponseEntity<List<Courier>> findNoAssociationCouriers(){
        try {
            List<Courier> list = courierService.findNoAssociationCouriers();
            return new ResponseEntity<>(list,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping("/findAssociationCouriers")
    public ResponseEntity<DataGridResult> findAssociationCouriers(String fixedAreaId){
        List<Courier> list = courierService.findAssociationCouriers(fixedAreaId);

        DataGridResult result = new DataGridResult();
        result.setTotal(100l);
        result.setRows(list);

        return new ResponseEntity<>(result,HttpStatus.OK);

    }


}
