package com.dafy.api;

import com.dafy.api.dto.Response;

public class RestApiImpl implements ServiceBApi {
    @Override
    public Response<String> getName() {
        return new Response<>("0","ok","service-b");
    }
}
