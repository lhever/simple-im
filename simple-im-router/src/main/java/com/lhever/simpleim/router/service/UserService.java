package com.lhever.simpleim.router.service;


import com.lhever.simpleim.common.pojo.User;

public interface UserService {

    User findById();


    User find(String name, String pwd);

}
