package com.czxy.crm.controller;

import com.czxy.crm.domain.Customer;
import com.czxy.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 查询所有未关联的客户信息
     * 思路：需要有参数吗？答：不需要
     *     何为未关联？其实就是fixedAreaId = null
     *
     *     ---->
     *     查找出fixedAreaid=null的Customer信息
     */
    @GetMapping("/noAssociationCustomers")
    public ResponseEntity<List<Customer>> findNoAssociationCustomers(){
        try {
            List<Customer> list = customerService.findNoAssociationCustomers();
            if(list.size()!=0){
                return new ResponseEntity<>(list,HttpStatus.OK);
            }
            return new ResponseEntity<>(list,HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * 查找出所有与xxx定区关联的客户的信息
     * 思路：需要参数吗？答：需要，fixedAreaId
     * 只要查出fixedAreaId=xxx的客户信息
     */
    @GetMapping("/hasAssociationFixedAreaCustomers")
    public ResponseEntity<List<Customer>> findHasAssociationFixedAreaCustomers(String fixedAreaId){
        try {
            List<Customer> list = customerService.findHasAssociationFixedAreaCustomers(fixedAreaId);
            if(list.size()!=0){
                return new ResponseEntity<>(list,HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);// 204 成功执行，但是没有返回值
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    /**
     *  功能：将新的customerIDs和fixedAreaid重新建立关联
     *  idStr：新的customerId的字符串
     *  fixedAreaId：定区id
     */
    @GetMapping("/doAssociationFixedAreaCustomers")
    public ResponseEntity<Void> doAssociationFixedAreaCustomers(String idStr,String fixedAreaId){
        // 调用Service的方法
        try {
            customerService.doAssociationFixedAreaCustomer(idStr,fixedAreaId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


    @PostMapping("/saveCustomer")
    public ResponseEntity<Void> saveCustomer(@RequestBody Customer customer){
        try {
            customerService.saveCustomer(customer);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @GetMapping("/findCustomerByTelephone")
    public ResponseEntity<Customer> findCustomerByTelephone(String telephone){
        Customer customer = customerService.findCustomerByTelephone(telephone);

        return new ResponseEntity<>(customer,HttpStatus.OK);

    }

    @GetMapping("/updateType")
    public ResponseEntity<Void> updateType(String telephone){

        customerService.updateCustomerByTelephone(telephone);

        return new ResponseEntity<>(HttpStatus.OK);
    }




}
