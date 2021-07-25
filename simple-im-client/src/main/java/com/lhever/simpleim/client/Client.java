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

import com.lhever.common.core.exception.CommonException;
import com.lhever.common.core.utils.StringUtils;
import com.lhever.common.core.utils.ThreadUtils;
import com.lhever.simpleim.client.handler.io.ClientHeartBeatHandler;
import com.lhever.simpleim.client.handler.io.ClientIdleHandler;
import com.lhever.simpleim.client.handler.business.ClientLoginHandler;
import com.lhever.simpleim.client.handler.business.ClientHandler;
import com.lhever.simpleim.client.config.ClientConfig;
import com.lhever.simpleim.client.config.RegCenter;
import com.lhever.simpleim.common.codec.LengthBasedByteBufDecoder;
import com.lhever.simpleim.common.codec.NettyCodecHandler;
import com.lhever.simpleim.common.util.Scan;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Getter
@Setter
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private String serverIp;
    private Integer serverPort;

    private String clientIp;
    private Integer clientPort;

    private String passWord;
    private String userName;


    public Client(String serverIp, Integer serverPort, String clientIp, Integer clientPort, String userName, String passWord) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.userName = userName;
        this.passWord = passWord;
    }

    EventLoopGroup group = new NioEventLoopGroup(4);
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


                            ch.pipeline().addLast(new ClientLoginHandler(userName, passWord));

                            ch.pipeline().addLast(new ClientHandler());


                        }
                    });
            // 发起异步连接操作
            ChannelFuture future = b.connect(new InetSocketAddress(serverIp, serverPort),
                    new InetSocketAddress(clientIp, clientPort)).addListener(new ChannelFutureListener() {
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        logger.info("connect to server success!");
                        //启动控制台输入
                        startScanConsole(channelFuture.channel());
                    } else {
                        logger.info("failed to connect the server! ");
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
                    } catch (InterruptedException e) {
                        logger.error("sleep error", e);
                    }
                    try {
                        logger.info("client reconnecting....");
                        connect();// 发起重连操作
                    } catch (Throwable e) {
                        logger.error("client reconnect error", e);
                    }
                }
            });
        }
    }

    /**
     * 启动控制台输入
     */
    private static void startScanConsole(Channel channel) {
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
        String userName = ClientConfig.USER_NAME;
        String passWord = ClientConfig.PASS_WORD;

        String serverIp = ClientConfig.SERVER_IP;
        Integer serverPort = ClientConfig.SERVER_PORT;
        if (ClientConfig.ENABLE_DISCOVERY) {
//            String url = StringUtils.appendAll("http://", ClientConfig.DISCOVERY_IP, ":", ClientConfig.DISCOVERY_PORT, ClientConfig.DISCOVERY_CONTEXT, "/api/getServer");
//            String address = HttpClientSingleton.get().doGet(url, null, null);

            RegCenter regCenter = new RegCenter(ClientConfig.ZK_ADDRESS,
                    ClientConfig.ZK_NAMESPACE, ClientConfig.ZK_ROOTPATH);
            regCenter.init();
            ThreadUtils.sleep(2000);
            String address = regCenter.getOnlineServer();
            if (StringUtils.isBlank(address)) {
                throw new CommonException("cannot get server address");
            }
            String[] split = address.split(":");
            if (split.length < 2) {
                throw new CommonException("bad address: " + address);
            }
            serverIp = split[0];
            serverPort = Integer.parseInt(split[1]);
        }
        String clientIp = ClientConfig.CLIENT_IP;
        Integer clientPort = ClientConfig.CLIENT_PORT;

        new Client(serverIp, serverPort, clientIp, clientPort, userName, passWord).connect();
    }

}
