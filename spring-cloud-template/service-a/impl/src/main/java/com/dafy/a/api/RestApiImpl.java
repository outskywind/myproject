package com.dafy.a.api;



import com.dafy.c.api.ServiceCApi;
import com.dafy.c.api.dto.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiImpl implements ServiceAApi {

    Logger log= LoggerFactory.getLogger(RestApiImpl.class);
    @Autowired
    ServiceCApi cApi;

    @Override
    public Response<String> getName( String id){
        log.info("a service---------");
        if("a".equals(id)){
            return new Response<>("200","ok","service-a");
        }else if ("c".equals(id)){
            return cApi.getName();
        }
        return new Response<>("200","ok","unknown");
    }


}
