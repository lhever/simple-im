package com.lhever.simpleim.server.handler.business;

import com.alibaba.fastjson.JSON;
import com.lhever.common.core.exception.CommonErrorCode;
import com.lhever.common.core.response.CommonResponse;
import com.lhever.common.core.support.http.HttpClientSingleton;
import com.lhever.common.core.utils.CollectionUtils;
import com.lhever.simpleim.common.dto.router.RouterCreateGroupReq;
import com.lhever.simpleim.common.dto.router.RouterCreateGroupResp;
import com.lhever.simpleim.common.msg.CreateGroupReq;
import com.lhever.simpleim.common.msg.CreateGroupResp;
import com.lhever.simpleim.common.util.JsonUtils;
import com.lhever.simpleim.common.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ServerCreateGroupHandler extends SimpleChannelInboundHandler<CreateGroupReq> {

    private static Logger logger = LoggerFactory.getLogger(ServerCreateGroupHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupReq msg) throws Exception {

        CreateGroupResp response = createGroup(ctx,msg);
        logger.info("返回给客户端:{}", JSON.toJSONString(response));

     /*   for (String userId : msg.getUserIds()){
            SessionUtil.getChannelByUserId(userId).writeAndFlush(response);
        }*/

    }

    private CreateGroupResp createGroup(ChannelHandlerContext ctx, CreateGroupReq packet) {
        RouterCreateGroupReq createReq = new RouterCreateGroupReq();
        String createId = SessionUtil.getUserIdByChannel(ctx.channel());
        createReq.setGroupName(packet.getGroupName());
        createReq.setCreateId(createId);
        createReq.setUserIds(CollectionUtils.getNotBlank(packet.getUserIds()));

        String json = HttpClientSingleton.get().doPostBody("http://127.0.0.1:8889/router/group/create", null, JsonUtils.obj2Json(createReq));
        CommonResponse<RouterCreateGroupResp> yunyaoResp = JsonUtils.json2Obj(json, new Type[]{RouterCreateGroupResp.class}, CommonResponse.class);


        CreateGroupResp response = new CreateGroupResp();
        if (yunyaoResp != null && CommonErrorCode.SUCCESS.getCode().equals(yunyaoResp.getCode())) {
            response.setSuccess(true);
            BeanUtils.copyProperties(yunyaoResp.getData(), response);
        } else {
            response.setSuccess(false);
        }
        return response;
    }

    private List<String> getUserNames(List<String> users) {
        List<String> userNames = new ArrayList<>();
        for(String id : users) {
            Channel channel = SessionUtil.getChannelByUserId(id);
            if (channel != null){
                String name = SessionUtil.getSessionByChannel(channel).getUserName();
                userNames.add(name);
            }
        }
        return userNames;
    }
}
