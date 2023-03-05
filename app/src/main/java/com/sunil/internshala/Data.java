package com.sunil.internshala;

public class Data {
    private String title;
    private String des;
    private String url;

    public Data(String title, String des, String url) {
        this.title = title;
        this.des = des;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDes() {
        return des;
    }

    public String getUrl() {
        return url;
    }
}
