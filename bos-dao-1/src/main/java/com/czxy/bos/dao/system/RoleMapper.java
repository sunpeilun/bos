package com.czxy.bos.dao.system;

import com.czxy.bos.domain.system.Role;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface RoleMapper extends Mapper<Role> {

    @Select("select * from t_role where id in (select role_id from t_user_role where user_id = #{userid})")
    public List<Role> findRoleByUser(int userid);


}
