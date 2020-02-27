package org.lotus.template.dubbo.service.impl;

import org.apache.dubbo.config.annotation.Service;
import org.lotus.tempalte.dubbo.api.UserService;


@Service
public class UserServiceImpl implements UserService {

    @Override
    public String getName(String id) {
        return null;
    }
}
