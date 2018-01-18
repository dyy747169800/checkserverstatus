package com.dyy.util;

import java.util.HashMap;
import java.util.Map;

/**
 * http请求参数
 *
 * @author 74716
 * @create 2017-12-13 13:28
 **/
public class HttpRequestParam {

    private String url;
    private String charset = "utf-8";
    private String jsonParams;
    private Map<String,String> params = new HashMap<>();
    private Map<String,String> cookies = new HashMap<>();
    private Map<String,String> headers = new HashMap<>();

    public HttpRequestParam(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getJsonParams() {
        return jsonParams;
    }

    public void setJsonParams(String jsonParams) {
        this.jsonParams = jsonParams;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    public void setParams(String paramKey,String paramValue) {
        this.params.put(paramKey,paramValue);
    }
    public void setHeaders(String headerKey,String headerValue) {
        this.headers.put(headerKey,headerValue);
    }
    public void setCookies(String cookieKey,String cookieValue) {
        this.cookies.put(cookieKey,cookieValue);
    }

}
