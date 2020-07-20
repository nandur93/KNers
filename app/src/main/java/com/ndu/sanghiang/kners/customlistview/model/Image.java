package com.ndu.sanghiang.kners.customlistview.model;

public class Image {
    int image;
    String progress;
    String name;
    String status;
    String created;
    float percent;
    String pid;
    String desc;

    public Image(int image, String progress, String name, String status, String created, float percent, String pid, String desc) {
        this.image = image;
        this.progress = progress;
        this.name = name;
        this.status = status;
        this.created = created;
        this.percent = percent;
        this.pid = pid;
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public String getProgress() {
        return progress;
    }

    public String getTitle() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public  String getCreated() {
        return created;
    }

    public  float getPercent() {
        return percent;
    }

    public  String getPid() {
        return pid;
    }

    public  String getDesc() {
        return desc;
    }
}
