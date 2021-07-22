package com.lhever.simpleim.router.basic.http;

import io.netty.handler.codec.http.HttpMethod;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HttpHandlerPath {

    private String uri;

    private HttpMethod method;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpHandlerPath that = (HttpHandlerPath) o;
        return uri.equals(that.uri) &&
                method.equals(that.method);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, method);
    }
}
