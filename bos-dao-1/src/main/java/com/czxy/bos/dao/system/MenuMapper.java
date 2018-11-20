package com.czxy.bos.dao.system;

import com.czxy.bos.domain.system.Menu;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface MenuMapper extends Mapper<Menu> {

    @Select("select m.* from t_user u,t_user_role ur,t_role r,t_role_menu rm,t_menu m\n" +
            "where u.ID = ur.USER_ID and ur.ROLE_ID = r.ID and r.ID = rm.role_id and rm.menu_id = m.ID\n" +
            "and u.id = #{userid}")
    public List<Menu> findMenuByUser(int userid);

}
