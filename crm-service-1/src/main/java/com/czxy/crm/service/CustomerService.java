package com.czxy.crm.service;

import com.czxy.crm.dao.CustomerMapper;
import com.czxy.crm.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
@Transactional
public class CustomerService {

    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 查找所有未关联的客户信息
     * 为空  is null    不为空  is  not  null
     *
     *
     * @return
     */
    public List<Customer> findNoAssociationCustomers(){
        Example example = new Example(Customer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIsNull("fixedAreaId");
        // 执行查询
        List<Customer> list = customerMapper.selectByExample(example);
        return  list;
    }

    /**
     * 查找fixedAreaId相关联的客户信息
     * @param fixedAreaId
     * @return
     */
    public  List<Customer> findHasAssociationFixedAreaCustomers(String fixedAreaId){
        Example example = new Example(Customer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("fixedAreaId",fixedAreaId);
        List<Customer> list = customerMapper.selectByExample(example);
        return list;
    }

    public void doAssociationFixedAreaCustomer(String idStr,String fixedAreaId){
        //1 根据定区id查找与该定区关联的老customer
        List<Customer> oldList = findHasAssociationFixedAreaCustomers(fixedAreaId);
        //2 解除关系--将fixedAreaID置空
        for(Customer c:oldList){
            c.setFixedAreaId(null);
            customerMapper.updateByPrimaryKey(c);
        }
        //3 建立新关系:先查再改
        String[] ids = idStr.substring(0, idStr.length() - 1).split(",");
        for(String id:ids){
            Customer customer = customerMapper.selectByPrimaryKey(id);
            customer.setFixedAreaId(fixedAreaId);
            customerMapper.updateByPrimaryKey(customer);
        }

    }

    public void saveCustomer(Customer customer){
        customerMapper.insert(customer);
    }


    public Customer findCustomerByTelephone(String telephone){
        Example example = new Example(Customer.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("telephone",telephone);
        // 查找
        Customer customer = customerMapper.selectOneByExample(example);
        return customer;
    }


    public void updateCustomerByTelephone(String telephone) {
        Customer customer = findCustomerByTelephone(telephone);
        if(customer!=null){
            customer.setType(1);
            customerMapper.updateByPrimaryKey(customer);
        }
    }
}
