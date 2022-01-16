package com.iotph.paa.ui.chat;


import java.util.Date;

public class Chat {
    String name;
    String message;
    String dateTime;
    int messagetype;

    public Chat() {
    }

    public Chat(String name, String message, int messagetype) {
        this.name = name;
        this.message = message;
        // Initialize to current time
        dateTime =  Long.toString(new Date().getTime());
        this.messagetype = messagetype;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() { return dateTime;   }

    public void setDateTime(String messageTime) { this.dateTime = messageTime;    }

    public int getMessageType() { return messagetype;   }

    public void setMessageType(int messageTime) { this.messagetype = messagetype;    }
}
