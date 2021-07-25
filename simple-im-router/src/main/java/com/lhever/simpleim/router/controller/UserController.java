package com.lhever.simpleim.router.controller;

import com.lhever.common.core.annotation.ModifyResponse;
import com.lhever.simpleim.router.basic.http.annotation.HttpMethod;
import com.lhever.simpleim.router.basic.http.annotation.PathMapping;
import com.lhever.simpleim.router.basic.http.annotation.RequestParam;
import com.lhever.simpleim.router.basic.http.annotation.RestController;
import com.lhever.simpleim.router.pojo.User;
import com.lhever.simpleim.router.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RestController
@PathMapping(uri = "/router/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PathMapping(uri = "/login", method = HttpMethod.GET)
    @ModifyResponse
    public Map<String, Object> login(@RequestParam(name = "name") String name,
                                     @RequestParam(name= "pwd") String pwd) {
        User user = userService.find(name, pwd);
        if (user == null) {
            return null;
        }

        Map<String, Object> result = new HashMap() {{
            put("id", user.getId());
            put("name", user.getName());
        }};
        return result;

    }



}
