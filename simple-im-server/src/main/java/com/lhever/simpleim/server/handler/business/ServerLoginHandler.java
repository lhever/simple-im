/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.lhever.simpleim.server.handler.business;

import com.alibaba.fastjson.TypeReference;
import com.lhever.common.core.response.CommonResponse;
import com.lhever.common.core.support.http.HttpClientSingleton;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.simpleim.common.msg.loginReq;
import com.lhever.simpleim.common.msg.loginResp;
import com.lhever.simpleim.common.util.JsonUtils;
import com.lhever.simpleim.common.util.LoginUtil;
import com.lhever.simpleim.common.util.Session;
import com.lhever.simpleim.common.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles both client-side and server-side handler depending on which
 * constructor was called.
 */
@ChannelHandler.Sharable
public class ServerLoginHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(ServerLoginHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null || !(msg instanceof loginReq)) {
            logger.info("服务端收到的不是认证请求，透传消息");
            ctx.fireChannelRead(msg);
            return;
        }

        loginReq loginReq = (loginReq) msg;
        logger.info("server received  login req:{}", JsonUtils.obj2Json(loginReq));
        loginResp resp = judgeLogin(loginReq);


        if (resp.getSuccess()) {
            logger.info("[{}]登录成功！,id为{}", resp.getUserName(), resp.getUserId());
            LoginUtil.markAsLogin(ctx.channel());
            SessionUtil.bindSession(new Session(resp.getUserId(), loginReq.getUserName()), ctx.channel());


            ctx.writeAndFlush(resp);
            logger.info("server send login success");

            //如果登录了，只要连接没断，就无需再校验
            ctx.channel().pipeline().remove(this);
        } else {
            ctx.writeAndFlush(resp);
            logger.info("server send login failed");
            ctx.close();
        }
    }




    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (LoginUtil.hasLogin(ctx.channel())) {
            logger.info("login success, no check login status later");
        } else {
            logger.info("login failed.....");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Session session = SessionUtil.getSessionByChannel(ctx.channel());
        if (session != null) {
            SessionUtil.unBindSession(session, ctx.channel());
            logger.info("{}({}) log out", session.getUserName(), session.getUserId());
        }
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常时断开连接
        Session session = SessionUtil.getSessionByChannel(ctx.channel());
        if (session != null) {
            SessionUtil.unBindSession(session, ctx.channel());
            logger.info("{}下线啦!", session.getUserId());
        }
        ctx.channel().close();

        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.fireChannelReadComplete();
    }


    public loginResp judgeLogin(loginReq loginReq) {
        String s = HttpClientSingleton.get().doGet("http://127.0.0.1:8889/router/user/login", null, new HashMap() {{
            put("name", loginReq.getUserName());
            put("pwd", loginReq.getPwd());
        }});

       CommonResponse<Map<String, String>> resutlMap =
               JsonUtils.json2Obj(s, new TypeReference<CommonResponse<Map<String, String>>>() {{
        }});
        Map<String, String> data = resutlMap.getData();
        if (data == null || StringUtils.isAnyBlank(data.get("id"), data.get("name"))) {
            loginResp resp = new loginResp();
            resp.setSuccess(false);
            return resp;
        } else {
            loginResp resp = new loginResp();
            resp.setSuccess(true);
            resp.setUserId(data.get("id"));
            resp.setUserName(data.get("name"));
            return resp;
        }
    }









}
