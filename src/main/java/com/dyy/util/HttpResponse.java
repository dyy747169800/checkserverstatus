package com.dyy.util;

/**
 * http请求工具类响应实体
 *
 * @author 74716
 * @create 2017-12-13 14:06
 **/
public class HttpResponse {

    private Integer statusCode;
    private String responseStr;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponseStr() {
        return responseStr;
    }

    public void setResponseStr(String responseStr) {
        this.responseStr = responseStr;
    }
}
