package com.lhever.simpleim.router.basic.util;

import com.lhever.common.core.response.CommonResponse;
import com.lhever.common.core.utils.JsonUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

public class ResponseUtils {

    private ResponseUtils() {

    }

    public static void notFound(ChannelHandlerContext ctx, FullHttpRequest request) {
        response(ctx, request, CommonResponse.clone("404", "not found"));
    }

    public static void response(ChannelHandlerContext ctx, FullHttpRequest request, Object generalResponse) {
        final byte[] jsonBytes = JsonUtils.object2Json(generalResponse).getBytes();
        final FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(jsonBytes));
        final HttpHeaders headers = response.headers();
        headers.set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
        headers.setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        final boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            headers.set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            ctx.write(response);
        }
        ctx.flush();
    }
}
