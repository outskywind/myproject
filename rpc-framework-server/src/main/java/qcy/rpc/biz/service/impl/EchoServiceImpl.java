package qcy.rpc.biz.service.impl;


import org.springframework.stereotype.Service;

import qcy.rpc.biz.service.EchoService;

@Service("echoService")
public class EchoServiceImpl implements EchoService {

    public String echo(String message) {
        return message == null ? "null" : message;
    }

}
