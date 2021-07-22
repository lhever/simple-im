package com.lhever.simpleim.router.controller;

import com.lhever.common.core.response.CommonResponse;
import com.lhever.simpleim.router.basic.http.annotation.HttpMethod;
import com.lhever.simpleim.router.basic.http.annotation.PathMapping;
import com.lhever.simpleim.router.basic.http.annotation.RequestParam;
import com.lhever.simpleim.router.basic.http.annotation.RestController;
import org.springframework.stereotype.Component;

@Component
@RestController
@PathMapping(uri = "/router")
public class ApiController {


    @PathMapping(uri = "/router", method = HttpMethod.GET)
    public CommonResponse login(@RequestParam(name = "name") String name) {
        return CommonResponse.clone(name);

    }








}
