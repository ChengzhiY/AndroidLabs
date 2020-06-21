package com.example.androidlabs;

public class MessageHandler {
    private String message;
    private boolean isSend;

    public MessageHandler(String message, boolean isSend){
        this.message = message;
        this.isSend = isSend;
    }
    public MessageHandler(){}

    public String getMessage(){
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    }

