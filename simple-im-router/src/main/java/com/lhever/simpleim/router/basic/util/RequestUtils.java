package com.lhever.simpleim.router.basic.util;

import com.google.common.collect.Maps;
import com.lhever.common.core.consts.CommonConsts;
import com.lhever.common.core.utils.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class RequestUtils {

    private RequestUtils() {
    }

    public static Map<String, List<String>> getParameterMap(final FullHttpRequest request) {
        final String uri = request.uri();
        if (StringUtils.isNotBlank(uri)) {
            if (uri.contains(CommonConsts.QUESTION_MARK)) {
                return new QueryStringDecoder(uri).parameters();
            }
        }
        return Maps.newTreeMap();
    }

    public static List<String> getParameterValue(final FullHttpRequest request, final String name) {
        return getParameterMap(request).get(name);
    }

    public static String getParameterFirstValue(final FullHttpRequest request, final String name) {
        return getParameterValue(request, name).iterator().next();
    }

    public static <T> T postEntity(final FullHttpRequest request, final Class<T> clazz) {
        final ByteBuf jsonBuf = request.content();
        final String json = jsonBuf.toString(CharsetUtil.UTF_8);
        return JsonUtils.json2Object(json, clazz);
    }
}
