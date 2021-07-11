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
package com.lhever.simpleim.server.basic;

import com.lhever.simpleim.common.msg.AuthReq;
import com.lhever.simpleim.common.msg.AuthResp;
import com.lhever.simpleim.common.util.JsonUtils;
import com.lhever.simpleim.common.util.LoginUtil;
import com.lhever.simpleim.common.util.Session;
import com.lhever.simpleim.common.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles both client-side and server-side handler depending on which
 * constructor was called.
 */
@ChannelHandler.Sharable
public class ServerLoginHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(ServerLoginHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg == null ||!(msg instanceof AuthReq)) {
            System.out.println("服务端收到的不是认证请求，透传消息");
            ctx.fireChannelRead(msg);
        } else  {
            AuthReq authReq = (AuthReq) msg;
            System.out.println("server received  auth req:  " + JsonUtils.obj2Json(authReq));
            AuthResp resp = new AuthResp();
            System.out.println("服务端发送认证相应");
            if ("lhever".equals(authReq.getUser()) && "123456".equals(authReq.getPwd())) {
                resp.setSuccess(true);
                resp.setClientId("lhever");
                ctx.writeAndFlush(resp);
                logger.info("[{}],登录成功！,id为{}", authReq.getUser(),authReq.getPwd());

                LoginUtil.markAsLogin(ctx.channel());
                SessionUtil.bindSession(new Session(resp.getClientId(), authReq.getUser()),ctx.channel());

                //如果登录了，只要连接没断，就无需再校验
                ctx.channel().pipeline().remove(this);
                super.channelRead(ctx, msg);

            } else {
                resp.setSuccess(false);
                ctx.writeAndFlush(resp);
                System.out.println("认证不通过，关闭客户端");
                ctx.close();
            }
//            ctx.fireChannelUnregistered();
//            System.out.println("un register lhever");
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if(LoginUtil.hasLogin(ctx.channel())){
            logger.info("登录验证已通过，后续无需再验证!");
        }else{
            logger.info("登录验证未通过，连接断开...");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Session session = SessionUtil.getSessionByChannel(ctx.channel());
        if (session != null ) {
            SessionUtil.unBindSession(session,ctx.channel());
            logger.info("{}下线啦!",session.getUserId());
        }
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常时断开连接
        Session session = SessionUtil.getSessionByChannel(ctx.channel());
        if (session != null ) {
            SessionUtil.unBindSession(session,ctx.channel());
            logger.info("{}下线啦!",session.getUserId());
        }
        ctx.channel().close();

        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.fireChannelReadComplete();

    }


}
