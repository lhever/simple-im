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
package com.lhever.simpleim.client.basic;

import com.lhever.simpleim.common.msg.AuthReq;
import com.lhever.simpleim.common.msg.AuthResp;
import com.lhever.simpleim.common.util.JsonUtils;
import com.lhever.simpleim.common.util.LoginUtil;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;

/**
 * Handler implementation for the object echo client.  It initiates the
 * ping-pong traffic between the object echo client and server by sending the
 * first message to the server.
 */
public class ClientLoginHandler extends ChannelInboundHandlerAdapter {

    private String passWord;
    private String userName;

    /**
     * Creates a client-side handler.
     */
    public ClientLoginHandler(String userName, String password) {
        this.userName = userName;
        this.passWord = password;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("ClientAuthHandler channel active !!!!");

        System.out.println("client send auth req");
        // Send the first message if this handler is a client-side handler.
        ChannelFuture future = ctx.writeAndFlush(buildAuthReq());
        future.addListener(FIRE_EXCEPTION_ON_FAILURE); // Let object serialisation exceptions propagate.
        ctx.fireChannelActive();
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端被断开");
        super.channelInactive(ctx);
    }

    private AuthReq buildAuthReq() {
        AuthReq req = new AuthReq(userName, passWord);
        return req;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Echo back the received object to the server.
        if (msg != null && msg instanceof AuthResp) {
            AuthResp resp = (AuthResp) msg;

            if (resp.getSuccess() != null && resp.getSuccess()) {


                System.out.println("客户端登陆成功");

                LoginUtil.markAsLogin(ctx.channel());

                ctx.fireChannelRead(msg);

            } else {

                System.out.println("client auth error" + JsonUtils.obj2Json(msg));
                ctx.close();
            }

        } else {
            System.out.println("客户端收到的不是认证响应，透传消息");
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.fireChannelReadComplete();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.fireExceptionCaught(cause);
    }
}
