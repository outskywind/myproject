package com.dafy.api;


import com.dafy.api.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("service-c")
public interface ServiceCApi {

    @RequestMapping("/getName")
    Response<String> getName();
}
