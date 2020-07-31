//package com.adam.demo.interceptor;
//
//import com.adam.demo.web.ProviderController;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.MethodParameter;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//import java.time.Duration;
//
//@ControllerAdvice
//public class MyResponseBodyAdvice implements ResponseBodyAdvice {
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    private ObjectMapper mapper = new ObjectMapper();
//
//    @Override
//    public boolean supports(MethodParameter returnType, Class converterType) {
//        if (returnType.hasMethodAnnotation(GetMapping.class)) {
//            return true;
//        }
//
//        if (returnType.hasMethodAnnotation(PostMapping.class) &&
//                StringUtils.equals(ProviderController.class.getName(), returnType.getExecutable().getDeclaringClass().getName())) {
//            return true;
//        }
//
//        return false;
//    }
//
//    @Override
//    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
//        try {
//            String redisKey = RedisCacheInterceptor.createRedisKey(((ServletServerHttpRequest) request).getServletRequest());
//            String redisValue;
//            if (body instanceof String) {
//                redisValue = (String) body;
//            } else {
//                redisValue = mapper.writeValueAsString(body);
//            }
////            this.redisTemplate.opsForValue().set(redisKey, redisValue, Duration.ofHours(1));
//            //缓存时间设置为10分钟
//            this.redisTemplate.opsForValue().set(redisKey, redisValue, Duration.ofMinutes(10));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return body;
//    }
//}
