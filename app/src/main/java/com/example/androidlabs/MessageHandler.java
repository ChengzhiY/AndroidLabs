package com.example.androidlabs;

public class MessageHandler {
    private String message;
    private boolean isSend;
    private long id;

    public MessageHandler(String message, boolean isSend, long id){
        this.message = message;
        this.isSend = isSend;
        this.id = id;
    }
    public MessageHandler(String message, long id){
      this(message, true, id);
    }

    public void setId(long id) { this.id = id; }

    public long getId() { return id; }
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

