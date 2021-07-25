package com.lhever.simpleim.router.basic.http;

import com.lhever.common.core.utils.NetUtils;
import com.lhever.simpleim.router.basic.cfg.RouterConfig;
import com.lhever.simpleim.router.basic.listener.SpringContextHolder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.EventExecutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;

public class HttpServer {

    private static final Logger logger = LoggerFactory.getLogger(HttpRouter.class);

    @Autowired
    private SpringContextHolder springContextHolder;

    public void start() {
        int port = RouterConfig.SERVER_PORT;
        final NioEventLoopGroup bossGroup = new NioEventLoopGroup(2, new DefaultThreadFactory("boss-thread"));
        final NioEventLoopGroup workerGroup = new NioEventLoopGroup(4, new DefaultThreadFactory("worker-thread"));
        EventExecutorGroup businessGroup = new DefaultEventExecutorGroup(6, new DefaultThreadFactory("biz-thread"));//业务
        try {
            HttpRouter httpRouter = new HttpRouter();
            httpRouter.loadControllerClass(springContextHolder.getApplicationContext());
            final ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.ERROR))
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) {
                            final ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("httpServerCodec", new HttpServerCodec());
                            pipeline.addLast("httpObjectAggregator", new HttpObjectAggregator(65536));
                            pipeline.addLast("httpContentCompressor", new HttpContentCompressor());
                            pipeline.addLast("chunkedWriteHandler", new ChunkedWriteHandler());
                            pipeline.addLast(businessGroup, new HttpServerHandler(httpRouter));
                        }
                    });

            final Channel serverChannel = bootstrap.bind(new InetSocketAddress(port)).sync().channel();
            logger.info("http://{}:{}/ Start-up success", getIp(), port);
            serverChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("", e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    private String getIp() {
        try {
            return NetUtils.getLocalIP();
        } catch (Exception e) {
            return "127.0.0.1";
        }
    }
}

