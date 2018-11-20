package com.czxy.bos.service;

import com.czxy.bos.dao.TakeTimeMapper;
import com.czxy.bos.domain.base.TakeTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TakeTimeService {

    @Autowired
    private TakeTimeMapper takeTimeMapper;


    /**
     * 查找所有工作时间
     *
     */
    public List<TakeTime> findAllTakeTime(){
        return takeTimeMapper.selectAll();
    }



}
