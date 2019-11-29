package com.dafy.gateway;



import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    Logger logger = LoggerFactory.getLogger(RestController.class);

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping("/rest/a/getName")
    public Response<String>   getName(String id){
        logger.info("gateway ---------");
        return post(null,"http://localhost:57200/getName?id="+id,new TypeReference<Response<String>>(){});
    }


    private  <T> T  post(Object args, String url, TypeReference<T> responseType){
        String response = doPost(url,args);
        return JSONObject.parseObject(response,responseType);
    }

    private <T> String doPost(String url,T postBody) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<T> requestEntity = new HttpEntity<>(postBody, requestHeaders);
        try{
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            return response.getBody();
        }catch (Exception e){
            logger.error("post error params={} ..",requestEntity,e);
        }
        return "";
    }

}
