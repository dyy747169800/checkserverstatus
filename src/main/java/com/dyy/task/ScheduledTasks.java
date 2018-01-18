package com.dyy.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dyy.config.EmailConfig;
import com.dyy.domain.MyEmail;
import com.dyy.util.HttpClientUtils;
import com.dyy.util.HttpRequestParam;
import com.dyy.util.HttpResponse;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 定时任务
 *
 * @author 段杨宇
 * @create 2017-12-13 15:59
 **/
@Component
@Configurable
@EnableScheduling
public class ScheduledTasks{
    private static final Logger log = Logger.getLogger(ScheduledTasks.class);
    @Value("${stmp.sendMailTo}")
    private  String sendMailTo;
    @Value("${checkServer.urls}")
    private  String checkServerUrls;
    @Autowired
    private EmailConfig emailConfig;

    private long laseSendDateTime = 0L;

    private static final Integer EVERY_DAY = 8;

   //每2分钟执行一次
//    @Scheduled(cron = "0 0/2 * * * ?")
    @Scheduled(cron = "0/5 * * * * ?")
    public void reportCurrentByCron(){
        Long localDateTime = getStartTimeOfDay(System.currentTimeMillis());
        if(localDateTime.equals(laseSendDateTime)){
                return;
            }

            if(!StringUtils.isEmpty(checkServerUrls)){
                for (String url : checkServerUrls.split(",")) {
                    if(checkServerStatus(url)){
                        sendEmail();
                        laseSendDateTime = getStartTimeOfDay(localDateTime);
                    }
                }
            }
    }

    private Boolean checkServerStatus(String url) {
        HttpRequestParam httpClientParam  = new HttpRequestParam(url);
        httpClientParam.setHeaders("Host","enterbj.zhongchebaolian.com");
        httpClientParam.setHeaders("Accept","*/*");
        httpClientParam.setHeaders("Accept-Encoding","gzip, deflate, br");
        httpClientParam.setHeaders("Accept-Language","en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4,zh-TW;q=0.2");
        httpClientParam.setHeaders("Connection","keep-alive");
        httpClientParam.setHeaders("User-Agent","bjsgecl/201712141613 CFNetwork/811.4.18 Darwin/16.5.0");
        httpClientParam.setHeaders("X-Requested-With","XMLHttpRequest");
        httpClientParam.setHeaders("Content-Type","application/x-www-form-urlencoded");
        HttpResponse httpResponse = HttpClientUtils.httpGet(httpClientParam);
        if(httpResponse == null){
            log.info("错误请求的url: " + url);
            return false;
        }else if(httpResponse.getStatusCode().equals(404)){
            log.info("************************************************************************************");
            log.info("成功请求的url: " + url);
            log.info("************************************************************************************");
            return true;
        }
            log.info("当前服务不可用");
//        else {
//            System.out.println("url:"+url);
//            System.out.println("statusCode:"+httpResponse.getStatusCode());
//            Document document = Jsoup.parse(httpResponse.getResponseStr());
//            Elements ma = document.getElementsByClass("ma");
//            if(ma != null && ma.size()>0){
//                for (Element element : ma) {
//                   if("由于办理电子进京证申请排队人数过多，请您耐心等待。给您带来的不便请谅解。".equals(element.text())){
//                       System.out.println("当前不可用");
//                       return false;
//                   }
//                }
//            }
//        }
        return false;
    }

    private void sendEmail(){
        MyEmail email = new MyEmail();
        String mailTo = pareSendMail(sendMailTo);
        if(StringUtils.isEmpty(mailTo)){
            return;
        }
        email.setReceiver(mailTo);
        email.setContent("进京证服务器状态");
        email.setSubject("现在可以办啦,抓紧时间");
        emailConfig.sendSimpleMail(email);
        log.info("成功发送邮件"+email.getReceiver());
    }


    private long getStartTimeOfDay(long now) {
        TimeZone curTimeZone = TimeZone.getTimeZone("GMT+8");
        Calendar calendar = Calendar.getInstance(curTimeZone);
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    private Integer getWeekForTime(Date now){
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        return cal.get(Calendar.DAY_OF_WEEK) - 1 < 0 ? 7 : cal.get(Calendar.DAY_OF_WEEK)- 1;
    }

    private String pareSendMail(String sendMailTo){
        StringBuilder sb = new StringBuilder();
        for (Object o : JSON.parseArray(sendMailTo)) {
            JSONObject jo = (JSONObject)o;
            Integer week = jo.getInteger("week");
            if(week.equals(getWeekForTime(new Date())) || week.equals(EVERY_DAY)){
                sb.append(jo.getString("email")).append(";");
            }
        }

        return sb.toString();
    }
}
