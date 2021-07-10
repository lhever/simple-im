/*
 * Copyright 2013-2018 Lilinfeng.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lhever.simpleim.client;

import com.lhever.simpleim.common.consts.NettyConstants;
import com.lhever.simpleim.common.LengthBasedByteBufDecoder;
import com.lhever.simpleim.common.NettyCodecHandler;
import com.sunnick.easyim.util.Scan;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Lilinfeng
 * @version 1.0
 * @date 2014年3月15日
 */
public class NettyClient {

    private static final Logger LOG = LoggerFactory.getLogger(NettyClient.class);

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private String serverIp;
    private Integer serverPort;

    private String clientIp;
    private Integer clientPort;

    public NettyClient(String serverIp, Integer serverPort, String clientIp, Integer clientPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public String getClientIp() {
        return clientIp;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public String getServerIp() {
        return serverIp;
    }


    EventLoopGroup group = new NioEventLoopGroup();

    public void connect() throws Exception {
        // 配置客户端NIO线程组
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {

                            ch.pipeline().addLast(new LengthBasedByteBufDecoder());

                            ch.pipeline().addLast(new NettyCodecHandler());

                            ch.pipeline().addLast(new ClientIdleHandler());


                            ch.pipeline().addLast(ClientHeartBeatHandler.getInstance());


                            ch.pipeline().addLast(new ClientAuthHandler());


                        }
                    });
            // 发起异步连接操作
            ChannelFuture future = b.connect(new InetSocketAddress(serverIp, serverPort),
                    new InetSocketAddress(clientIp, clientPort)).addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        System.out.println("connect to server success!");
                        //启动控制台输入
                        startScanConsole(channelFuture.channel());
                    } else {
                        System.out.println("failed to connect the server! ");
                    }
                }
            }).sync();
            // 当对应的channel关闭的时候，就会返回对应的channel。
            // Returns the ChannelFuture which will be notified when this channel is closed. This method always returns the same future instance.
            future.channel().closeFuture().sync();
        } finally {
            // 所有资源释放完成之后，清空资源，再次发起重连操作
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        TimeUnit.SECONDS.sleep(8);
                        try {
                            System.out.println("客户端发起重连操作");
                            connect();// 发起重连操作
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 启动控制台输入
     */
    private static void startScanConsole(Channel channel){
        Scan scan = new Scan(channel);
        Thread thread = new Thread(scan);
        thread.setName("scan-thread");
        thread.start();
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new NettyClient(NettyConstants.SERVER_IP, NettyConstants.SERVER_PORT, NettyConstants.CLIENT_IP, NettyConstants.CLIENT_PORT).connect();
    }

}
