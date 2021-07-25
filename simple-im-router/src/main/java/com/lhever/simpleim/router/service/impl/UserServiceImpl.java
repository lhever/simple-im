package com.lhever.simpleim.router.service.impl;

import com.lhever.simpleim.common.pojo.User;
import com.lhever.simpleim.router.dao.UserDao;
import com.lhever.simpleim.router.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;




    public User findById() {
        return null;
    }


    @Override
    public User find(String name, String pwd) {
        return userDao.findUser(name, pwd);
    }
}
