package com.adam.demo.dao;

import com.adam.demo.entity.XcTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface XcTaskRepository extends JpaRepository<XcTask,String> {

//    注意一点：这里的sql语句写的都是java类的名字以及属性名，参数使用“：参数名”的形式传递；
//    这样的方法也是可以直接调用的；
//
//    可以通过自定义一些符合命名规范的方法来进行特殊的查询
//    命名为findByxxxxAndxxxx ，xxxx为属性名，后面可以跟着before，in等字段做条件查询，
//    如果需要分页，则返回为Page类型的值，同时参数额外传递Pageable； 这些方法可以直接使用，不需要自己写实现；

    //查询某个时间之间的前n条任务
    Page<XcTask> findByUpdateTimeBefore(Pageable pageable, Date updateTime);

    @Modifying
    @Query("update XcTask t set t.version = :version+1 where t.id = :id and t.version = :version")
    int updateTaskVersion(@Param(value = "id") String id,@Param(value = "version") int version);
}
