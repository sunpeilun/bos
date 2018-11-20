package com.czxy.bos.dao.system;

import com.czxy.bos.domain.system.Permission;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface PermissionMapper extends Mapper<Permission> {

    @Select("select p.* from t_user u,t_user_role ur,t_role r,t_role_permission rp,t_permission p\n" +
            "WHERE u.ID = ur.USER_ID and ur.ROLE_ID = r.ID and r.ID = rp.ROLE_ID and rp.PERMISSION_ID = p.ID\n" +
            "and u.ID = #{userid}")
    public List<Permission> findPermissionByUser(int userid);

}
