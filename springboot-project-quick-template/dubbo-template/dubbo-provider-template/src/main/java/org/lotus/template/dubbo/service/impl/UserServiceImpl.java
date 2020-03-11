package org.lotus.template.dubbo.service.impl;

import org.apache.dubbo.config.annotation.Service;
import org.lotus.tempalte.dubbo.api.UserService;


@Service
public class UserServiceImpl implements UserService {

    @Override
    public String getName(String id) {
        return null;
    }

    @Override
    public String addUser(String userName, int age, int sex) {
        return null;
    }
}
