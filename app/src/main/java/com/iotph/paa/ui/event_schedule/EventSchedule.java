package com.iotph.paa.ui.event_schedule;

public class EventSchedule {
    String event_date;
    String event_time;
    String event_title;
    Long timestamp;

    public EventSchedule() {
    }

    public EventSchedule(String event_date, String event_time, String event_title) {
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_title = event_title;

    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_time() { return event_time;   }

    public void setEvent_time(String event_time) { this.event_time = event_time;    }

    public Long getTimestamp() { return timestamp;   }

    public void setTimestamp(Long timestamp) { this.timestamp = timestamp;    }

}
