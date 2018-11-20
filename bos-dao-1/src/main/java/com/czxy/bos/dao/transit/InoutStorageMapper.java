package com.czxy.bos.dao.transit;

import com.czxy.bos.domain.transit.InOutStorageInfo;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface InoutStorageMapper extends Mapper<InOutStorageInfo> {

    @Select("SELECT * FROM t_in_out_storage_info where TRANSIT_INFO_ID = #{transitInfoId} order by ID ASC")
    public List<InOutStorageInfo> findInOutStorageInfoByTransitInfoId(int transitInfoId);

}
