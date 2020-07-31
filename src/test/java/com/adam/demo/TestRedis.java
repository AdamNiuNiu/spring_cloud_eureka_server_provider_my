package com.adam.demo;

import com.adam.demo.dao.XcTaskHisRepository;
import com.adam.demo.entity.XcTaskHis;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@SpringBootTest
public class TestRedis {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private XcTaskHisRepository xcTaskHisRepository;

//     RedisTemplate常用方法
//     RedisTemplate中定义了对5种数据结构操作
//     redisTemplate.opsForValue();//操作字符串
//     redisTemplate.opsForHash();//操作hash
//     redisTemplate.opsForList();//操作list
//     redisTemplate.opsForSet();//操作set
//     redisTemplate.opsForZSet();//操作有序set

    @Test
    public void testSave() {
        for (int i = 0; i < 100; i++) {
            this.redisTemplate.opsForValue().set("key_" + i, "value_" + i);
        }
        Set<String> keys = this.redisTemplate.keys("key_*");
        for (String key : keys) {
            String value = this.redisTemplate.opsForValue().get(key);
            System.out.println(value);
            this.redisTemplate.delete(key);
        }
    }


    @Test
    public void  testGetTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        //16:42:50
        System.out.println(format.format(date));
    }


    @Test
    public void  testGetSqlTime() {
        XcTaskHis one = xcTaskHisRepository.getOne("109");
        System.out.println("=====获取到数据库时间====="+one.getCreateTime());
    }
}
