package com.dafy.a.api;


import com.dafy.c.api.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("service-b")
public interface ServiceBApi {

    @RequestMapping("/getName")
    Response<String> getName();
}
