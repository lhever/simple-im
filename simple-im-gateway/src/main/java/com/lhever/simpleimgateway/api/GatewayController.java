package com.lhever.simpleimgateway.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>类说明：</p>
 *
 * @author lihong10 2021/7/10 20:40
 * @version v1.0
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2021/7/10 20:40
 * @modify by reason:{方法名}:{原因}
 */
@RestController
@RequestMapping(path = "/api")
public class GatewayController {


    @GetMapping(path = "getServer")
    @ResponseBody
    public String getServer() {
        return "127.0.0.1";
    }
}
