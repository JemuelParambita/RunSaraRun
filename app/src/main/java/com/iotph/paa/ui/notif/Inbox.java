package com.iotph.paa.ui.notif;


public class Inbox {
    String title;
    String type;
    Long timestamp;

    public Inbox() {
    }

    public Inbox(String title, String type, long timestamp) {
        this.title = title;
        this.type = type;
        this.timestamp = timestamp;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTimestamp() { return timestamp;   }

    public void setTimestamp(Long timestamp) { this.timestamp = timestamp;    }

    }
