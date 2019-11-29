package com.dafy.a.api;

import com.dafy.c.api.ServiceCApi;
import com.dafy.c.api.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RestApiImpl implements ServiceCApi {

    Logger log = LoggerFactory.getLogger(RestApiImpl.class);

    @Override
    public Response<String> getName() {
        log.info("service c ---------");
        return new Response<>("0","ok","service-c");
    }
}
