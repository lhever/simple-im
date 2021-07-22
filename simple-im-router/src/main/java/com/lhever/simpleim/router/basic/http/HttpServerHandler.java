package com.lhever.simpleim.router.basic.http;

import com.lhever.common.core.consts.CommonConsts;
import com.lhever.simpleim.router.basic.util.ResponseUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpServerHandler extends ChannelInboundHandlerAdapter {

    public static final String FAVICON = "/favicon.ico";

    private HttpRouter httpRouter;


    HttpServerHandler(HttpRouter httpRouter) {
        this.httpRouter = httpRouter;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.handlerAdded");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.channelRegistered");
        super.channelRegistered(ctx);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.channelActive");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            final FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();
            if (FAVICON.equals(uri)) {
                ctx.close();
                return;
            }
            if (uri.contains(CommonConsts.QUESTION_MARK)) {
                uri = uri.substring(0, uri.indexOf(CommonConsts.QUESTION_MARK));
            }
            final HttpMethodHandler httpMethodHandler = httpRouter.getRoute(new HttpHandlerPath(uri, request.method()));
            if (httpMethodHandler != null) {
                ResponseUtils.response(ctx, request, httpMethodHandler.call(request));
            } else {
                ResponseUtils.notFound(ctx, request);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.channelReadComplete");
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.channelInactive");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.channelUnregistered");
        super.channelUnregistered(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("NettyServerHandler.handlerRemoved");
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
