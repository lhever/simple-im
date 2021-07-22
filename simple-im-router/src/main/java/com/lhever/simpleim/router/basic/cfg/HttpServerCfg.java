package com.lhever.simpleim.router.basic.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.lhever.simpleim.router.basic.http.HttpServer;

@Configuration
public class HttpServerCfg {

    @Bean(name = "httpServer", initMethod = "start")
    public HttpServer httpServer() {
        return new HttpServer();
    }
}
