package com.lhever.simpleim.router.controller;

import com.lhever.common.core.annotation.ModifyResponse;
import com.lhever.simpleim.common.msg.CreateGroupReq;
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
    public void createGroup(CreateGroupReq req) {
        groupService.createGroup(req);
    }



}
