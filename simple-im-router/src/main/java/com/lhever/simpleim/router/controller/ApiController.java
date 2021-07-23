package com.lhever.simpleim.router.controller;

import com.lhever.common.core.annotation.ModifyResponse;
import com.lhever.simpleim.router.basic.http.annotation.HttpMethod;
import com.lhever.simpleim.router.basic.http.annotation.PathMapping;
import com.lhever.simpleim.router.basic.http.annotation.RequestParam;
import com.lhever.simpleim.router.basic.http.annotation.RestController;
import org.springframework.stereotype.Component;

@Component
@RestController
@PathMapping(uri = "/router")
public class ApiController {


    @PathMapping(uri = "/login", method = HttpMethod.GET)
    @ModifyResponse
    public String login(@RequestParam(name = "name") String name) {
        return name;
    }

    @PathMapping(uri = "/hello", method = HttpMethod.GET)
    public String login() {
        return "hello";
    }


}
