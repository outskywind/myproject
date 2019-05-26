package com.dafy.api;


import com.dafy.api.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("service-b")
public interface ServiceBApi {

    @RequestMapping("/getName")
    Response<String> getName();
}
