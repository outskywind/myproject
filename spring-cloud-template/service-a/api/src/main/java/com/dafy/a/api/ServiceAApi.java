package com.dafy.a.api;


import com.dafy.c.api.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-a")
public interface ServiceAApi {
    @RequestMapping("/getName")
    Response<String> getName(@RequestParam String id);

}
