package com.czxy.bos.service.system;

import com.czxy.bos.dao.system.UserMapper;
import com.czxy.bos.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public User findUserByUsername(String username){
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("username",username);
        return userMapper.selectOneByExample(example);
    }


}
