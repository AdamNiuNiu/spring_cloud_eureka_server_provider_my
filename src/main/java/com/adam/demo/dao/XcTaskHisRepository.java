package com.adam.demo.dao;


import com.adam.demo.entity.XcTaskHis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Administrator.
 */
public interface XcTaskHisRepository extends JpaRepository<XcTaskHis,String> {

    @Modifying
    @Query("select t from XcTaskHis t where t.id = :id")
    List<XcTaskHis> getXctaskhis(@Param(value = "id") String id);
}
