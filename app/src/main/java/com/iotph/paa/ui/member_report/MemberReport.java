package com.iotph.paa.ui.member_report;

public class MemberReport {
    String DESCRIPTION;
    String address;
    Long datetime;
    String filename;
    String title;

    public MemberReport() {

    }

    public MemberReport(String DESCRIPTION, String address, long datetime, String filename, String title) {
        this.title = title;
        this.address = address;
        this.datetime = datetime;
        this.filename = filename;
        this.DESCRIPTION = DESCRIPTION;

    }
    public String getDescription() {
        return DESCRIPTION;
    }
    public void setDescription(String DESCRIPTION) { this.DESCRIPTION = DESCRIPTION; }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) { this.address = address; }

    public Long getDatetime() { return datetime;   }
    public void setDatetime(Long datetime) { this.datetime = datetime;    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }


}
