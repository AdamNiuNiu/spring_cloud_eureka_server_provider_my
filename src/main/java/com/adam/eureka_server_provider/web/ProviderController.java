package com.adam.eureka_server_provider.web;

import com.adam.eureka_server_provider.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/provider")
@RestController
public class ProviderController {

    @Value("${nameConfig}")
    private String nameConfig;

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

}
