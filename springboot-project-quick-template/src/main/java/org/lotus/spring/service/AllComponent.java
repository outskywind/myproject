package org.lotus.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by quanchengyun on 2018/4/28.
 */
@Component
public class AllComponent {

    @Autowired
    @Qualifier("appServiceAop")
    Appservice appservice;


}
