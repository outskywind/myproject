package com.dafy.a.api;

import com.dafy.c.api.dto.Response;

public class RestApiImpl implements ServiceBApi {
    @Override
    public Response<String> getName() {
        return new Response<>("0","ok","service-b");
    }
}
