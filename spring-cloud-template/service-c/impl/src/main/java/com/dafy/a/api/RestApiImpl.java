package com.dafy.a.api;

import com.dafy.c.api.ServiceCApi;
import com.dafy.c.api.dto.Response;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiImpl implements ServiceCApi {

    @Override
    public Response<String> getName() {
        return new Response<>("0","ok","service-c");
    }
}
