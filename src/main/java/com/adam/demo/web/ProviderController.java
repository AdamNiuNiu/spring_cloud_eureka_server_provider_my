package com.adam.demo.web;

import com.adam.demo.entity.User;
import com.adam.demo.entity.XcTaskHis;
import com.adam.demo.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/provider")
@RestController
public class ProviderController {

    @Value("${nameConfig}")
    private String nameConfig;

    @Autowired
    private TaskService service;

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

}
