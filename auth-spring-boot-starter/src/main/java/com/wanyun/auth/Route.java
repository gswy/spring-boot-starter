package com.wanyun.auth;

import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
public class Route {

    /**
     * 请求方式
     */
    private HttpMethod method;

    /**
     * 路径
     */
    private String path;

}
