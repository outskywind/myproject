package org.lotus.tempalte.dubbo.api;



public interface UserService {

    String  getName(String id);

    String  addUser(String userName,int age,int sex);
}
