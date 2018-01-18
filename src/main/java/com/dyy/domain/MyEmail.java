package com.dyy.domain;

/**
 * 邮件实体类
 *
 * @author 74716
 * @create 2017-12-13 10:26
 **/
public class MyEmail {

    private String receiver;
    private String subject;
    private String text;
    private String content;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}