package com.lhever.simpleim.router.controller;

import com.lhever.common.core.annotation.ModifyResponse;
import com.lhever.simpleim.common.dto.router.RouterCreateGroupReq;
import com.lhever.simpleim.common.dto.router.RouterCreateGroupResp;
import com.lhever.simpleim.router.basic.http.annotation.HttpMethod;
import com.lhever.simpleim.router.basic.http.annotation.PathMapping;
import com.lhever.simpleim.router.basic.http.annotation.RestController;
import com.lhever.simpleim.router.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RestController
@PathMapping(uri = "/router/group")
public class GroupController {

    @Autowired
    private GroupService groupService;


    @PathMapping(uri = "/create", method = HttpMethod.POST)
    @ModifyResponse
    public RouterCreateGroupResp createGroup(RouterCreateGroupReq req) {
      return   groupService.createGroup(req);
    }



}
