package com.lhever.simpleim.server;

import com.lhever.simpleim.common.codec.LengthBasedByteBufDecoder;
import com.lhever.simpleim.common.codec.NettyCodecHandler;
import com.lhever.simpleim.server.basic.ServerHeartBeatHandler;
import com.lhever.simpleim.server.basic.ServerIdleHandler;
import com.lhever.simpleim.server.basic.ServerLoginHandler;
import com.lhever.simpleim.server.business.ServerHandler;
import com.lhever.simpleim.server.config.ServerConfig;
import com.lhever.simpleim.server.reg.ZkRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private Integer serverPort;
    private String serverIp;
    private ZkRegister zkRegister;

    public Server(String serverIp, Integer serverPort) {
        this.serverPort = serverPort;
        this.serverIp = serverIp;
    }


    public void bind() throws Exception {
        // 配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 100)
                .option(NioChannelOption.SO_KEEPALIVE, true)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws IOException {
                        ch.pipeline().addLast(new ServerIdleHandler());
                        ch.pipeline().addLast(new LengthBasedByteBufDecoder());
                        ch.pipeline().addLast(new NettyCodecHandler());
                        ch.pipeline().addLast(ServerHeartBeatHandler.getInstance());
                        ch.pipeline().addLast(new ServerLoginHandler());
                        ch.pipeline().addLast(new ServerHandler());

                    }
                });

        // 绑定端口，同步等待成功
        b.bind(serverIp, serverPort).addListener(
                new ChannelFutureListener() {
                    @Override
                    public void operationComplete(ChannelFuture channelFuture) throws Exception {
                        if (channelFuture.isSuccess()) {
                            logger.info("server started success using port:{}", serverPort);
                        }else {
                            logger.info("server started failed using port: {}", serverPort, channelFuture.cause());
                            System.exit(0);
                        }
                    }
                }
        ).sync();

        logger.info("Netty server start success with ip:{} and port:{} ", serverIp, serverPort);

        this.zkRegister = new ZkRegister();
        zkRegister.register(this.serverIp, this.serverPort);
    }

    public static void main(String[] args) throws Exception {
        new Server(ServerConfig.SERVER_IP, ServerConfig.SERVER_PORT).bind();
    }
}

