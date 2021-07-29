package com.lhever.simpleim.router.service;

import com.lhever.simpleim.common.pojo.User;
import com.lhever.simpleim.router.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    private UserDao userDao;



    public User find(String name, String pwd) {
        return userDao.findUser(name, pwd);
    }
}
