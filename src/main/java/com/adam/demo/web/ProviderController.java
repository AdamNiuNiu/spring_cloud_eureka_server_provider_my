package com.adam.demo.web;

import com.adam.demo.entity.User;
import com.adam.demo.entity.XcTaskHis;
import com.adam.demo.service.TaskService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequestMapping("/provider")
@RestController
public class ProviderController {

    @Value("${nameConfig}")
    private String nameConfig;

    @Autowired
    private TaskService service;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String product="MoonCake5";

//    @RequestMapping(value = "/getUserInfo/{id}",method = RequestMethod.POST)
    @RequestMapping(value = "/getUserInfo/{id}")
    public User getUserInfo(@PathVariable("id") String id) {

        User user = new User();
        user.setAge(10);
        user.setUsername("小明");
        user.setPassword("123");
        System.out.println("--------id为："+id+"---------");
        System.out.println("--------服务方的user信息---------");
        return user;
    }


    @RequestMapping(value = "/getNameConfig")
    public String getNameConfig() {

        return nameConfig;
    }


    @GetMapping(value = "/getXctaskhis")
    @ResponseBody
    public List<XcTaskHis> getXctaskhis(String id) {
        List<XcTaskHis> xctaskhisList = service.getXctaskhis(id);
        return xctaskhisList;
    }


    @Valid
    @ApiOperation(value = "测试接口", notes = "通过接口 ,对测试服务连通性", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequestMapping(value = "test", method = RequestMethod.POST)
    public String test() {
        RLock rLock = redissonClient.getLock("test1");
        rLock.lock();
        try {
            Thread.sleep(1000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (log.isDebugEnabled()) {
            log.debug("test welcome.........");
        }
        rLock.unlock();
        return "响应成功!";
    }


    /**
     * 使用线程池定义了20个线程
     * 模拟1000个线程同时执行业务，修改资源
     * 模拟了1000个线程，通过线程池方式提交，每次20个线程抢占分布式锁，抢到分布式锁的执行代码，没抢到的等待
     * @return
     */
    @GetMapping("/submitOrder")
    public String submitOrder(){
        RLock lock = null;
        try {
            //初始化商品的库存数量
            stringRedisTemplate.opsForValue().set("stock5", String.valueOf(100));
            //使用线程池定义了20个线程
            ExecutorService service = Executors.newFixedThreadPool(20);
            for (int i=0;i<1000;i++){
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        RLock lock = redissonClient.getLock(product);
                        lock.lock();//加锁，阻塞
//                      boolean b = lock.tryLock();//非阻塞
                        //获取商品库存数
                        int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock5"));
                        if (stock>0){
                            //下单
                            stock-=1;
                            stringRedisTemplate.opsForValue().set("stock5", String.valueOf(stock));
                            System.err.println("=============当前线程============"+Thread.currentThread().getName()+"订单提交，扣减成功，stock5："+stock);
                        }else {
                            //没库存
                            System.out.println("=============当前线程============"+Thread.currentThread().getName()+"人太多了，已被买光，订单提交失败了!");
                        }
                        lock.unlock();//释放锁
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(lock!=null) {
                lock.unlock();//释放锁
            }
        }
        return "end";
    }



}
