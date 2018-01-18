package com.dyy.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * http请求工具类
 * @author: 段杨宇
 * @date: 16:06 2017/12/14
 *
 * <dependency>
 *     <groupId>org.apache.httpcomponents</groupId>
 *     <artifactId>httpclient</artifactId>
 *     <version>4.4.1</version>
 * </dependency>
 **/
public class HttpClientUtils {

    private static RequestConfig config = RequestConfig.custom()
                                            .setConnectionRequestTimeout(5000)
                                            .setConnectTimeout(5000)
                                            .setSocketTimeout(5000)
                                            .setRedirectsEnabled(true)
                                            .build();
    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    private static CloseableHttpResponse response;

    public static HttpResponse httpPost(HttpRequestParam httpRequestParam){
        HttpResponse httpResponse;
        if(httpRequestParam == null || httpRequestParam.getUrl() == null){
            return null;
        }
        Map<String, String> params = httpRequestParam.getParams();
        String charset = httpRequestParam.getCharset();
        String requestUrl = httpRequestParam.getUrl();
        Map<String, String> cookies = httpRequestParam.getCookies();
        Map<String, String> headers = httpRequestParam.getHeaders();
        HttpPost post = new HttpPost(requestUrl);
        post.setConfig(config);
        try {
            //设置参数
            if(!isNull(params)){
                post.setEntity(parseParams(params,charset));
            }
            //设置cookie
            if(!isNull(cookies)){
                addCookies(post,cookies);
            }
            //设置请求头
            if(!isNull(headers)){
                addHeaders(post,headers);
            }
            //发起请求
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            //请求的响应体
            httpResponse = parseResponse(response);
            //关闭流
            EntityUtils.consume(entity);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if(null != response){
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return httpResponse;
    }

    public static HttpResponse httpGet(HttpRequestParam httpRequestParam) {
        String paramsStr;
        HttpResponse httpResponse;
        HttpGet get;
        if(httpRequestParam == null || httpRequestParam.getUrl() == null){
            return null;
        }
        Map<String, String> params = httpRequestParam.getParams();
        String charset = httpRequestParam.getCharset();
        String requestUrl = httpRequestParam.getUrl();
        Map<String, String> cookies = httpRequestParam.getCookies();
        Map<String, String> headers = httpRequestParam.getHeaders();
        try {
            // 设置参数
            if(!isNull(params)){
                paramsStr = EntityUtils.toString(parseParams(params,charset));
                // Get请求
                get = new HttpGet(requestUrl + "?" + paramsStr);
            }else {
                get = new HttpGet(requestUrl);
            }
            get.setConfig(config);
            if(!isNull(cookies)){
                addCookies(get,cookies);
            }
            //设置请求头
            if(!isNull(headers)){
                addHeaders(get,headers);
            }
            // 发送请求
            response = httpClient.execute(get);
            httpResponse = parseResponse(response);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if(null != response){
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return httpResponse;
    }

    /**
     * 提交数据为json格式
     * @return
     */
    public static HttpResponse httpPostForJson(HttpRequestParam httpRequestParam){
        HttpResponse httpResponse;
        if(httpRequestParam == null || httpRequestParam.getUrl() == null){
            return null;
        }
        String charset = httpRequestParam.getCharset();
        String requestUrl = httpRequestParam.getUrl();
        String jsonParams = httpRequestParam.getJsonParams();
        Map<String, String> cookies = httpRequestParam.getCookies();
        Map<String, String> headers = httpRequestParam.getHeaders();
        HttpPost post = new HttpPost(requestUrl);
        post.setConfig(config);
        try {
            //设置参数
            if(jsonParams != null){
                StringEntity stringEntity = new StringEntity(jsonParams,charset);
                stringEntity.setContentType("application/json");
                stringEntity.setContentEncoding(charset);
                post.setEntity(stringEntity);
            }
            //设置cookie
            if(!isNull(cookies)){
                addCookies(post,cookies);
            }
            //设置请求头
            if(!isNull(headers)){
                addHeaders(post,headers);
            }
            //发起请求
            response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            //请求的响应体
            httpResponse = parseResponse(response);
            //关闭流
            EntityUtils.consume(entity);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if(null != response){
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return httpResponse;
    }

    private static boolean isNull(Collection c) {
        return c == null || c.size() == 0;
    }

    private static boolean isNull(Map c) {
        return c == null || c.size() == 0;
    }

    private static void addCookies(HttpRequestBase hRB,Map<String,String> cookies){
        StringBuilder cookieStr = new StringBuilder();
        for (Map.Entry<String,String> cookie:cookies.entrySet()){
            cookieStr.append(cookie.getKey()).append("=").append(cookie.getValue()).append(";");
        }
        cookieStr = new StringBuilder(cookieStr.substring(0, cookieStr.length() - 1));
        hRB.setHeader(new BasicHeader("Cookie", cookieStr.toString()));
    }

    private static void addHeaders(HttpRequestBase hRB,Map<String,String> headers){
        for (Map.Entry<String,String> head:headers.entrySet()){
            hRB.setHeader(new BasicHeader(head.getKey(),head.getValue()));
        }
    }

    private static UrlEncodedFormEntity parseParams(Map<String,String> params,String charset) throws UnsupportedEncodingException {
        List<BasicNameValuePair> formparams = new ArrayList<>();
        for (Map.Entry<String,String> param:params.entrySet()){
            formparams.add(new BasicNameValuePair(param.getKey(),param.getValue()));
        }
        return new UrlEncodedFormEntity(formparams,charset);
    }

    private static HttpResponse parseResponse(CloseableHttpResponse response) throws IOException {
        if(response == null){
            return null;
        }
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        httpResponse.setResponseStr(EntityUtils.toString(entity,"utf-8"));
        //关闭流
        EntityUtils.consume(entity);
        return httpResponse;
    }



    public static void main(String[] args) {
        HttpRequestParam httpRequestParam = new HttpRequestParam("https://192.168.8.183/server/server_uploadMachineStrategy");
        httpRequestParam.setParams("message","eyJuZXdNYWNoaW5lSWQiOiI0ZTg1NDVhNGVjZTI3NDUzZTgzNjc5MjllYWYzNjU5ZSIsInN0cmF0ZWd5Q29kZSI6IjU2ZTg2Yjc1Nzg5YTU4OWQwNzYyYjRmZGQ1NWNhYTNiNzMwYzU2YmNkODFjNjg2NjU2MWUwYTcwZGM0MjgzN2EiLCJzdHJhdGVneURlc2MiOiJhbGx0ZXN0MSIsInRva2VuIjoiNzcwZDc3YmY3Yzc3NDFjYjk3NzYzMGVjMGVhNjc5OGUifQo%3D");
        HttpResponse re = httpPost(httpRequestParam);
        System.out.println(re.getResponseStr());
    }
}
