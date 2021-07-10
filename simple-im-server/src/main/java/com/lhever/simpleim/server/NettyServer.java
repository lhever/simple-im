package com.lhever.simpleim.server;

import com.lhever.simpleim.common.consts.NettyConstants;
import com.lhever.simpleim.common.LengthBasedByteBufDecoder;
import com.lhever.simpleim.common.NettyCodecHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NettyServer {

    private static final Logger LOG = LoggerFactory.getLogger(NettyServer.class);

    private Integer serverPort;
    private String serverIp;

    public NettyServer(String serverIp, Integer serverPort) {
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

                        ch.pipeline().addLast(new ServerAuthHandler());

                    }
                });

        // 绑定端口，同步等待成功
        b.bind(serverIp, serverPort).sync();

        LOG.info("Netty server start ok : " + (serverIp + " : " + serverPort));
    }

    public static void main(String[] args) throws Exception {
        new NettyServer(NettyConstants.SERVER_IP, NettyConstants.SERVER_PORT).bind();
    }
}

