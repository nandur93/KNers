package com.ndu.sanghiang.kners.customlistview.model;

public class Image {
    int image;
    String progress;
    String name;
    String status;
    String created;
    float percent;
    String pid;

    public Image(int image, String progress, String name, String status, String created, float percent, String pid) {
        this.image = image;
        this.progress = progress;
        this.name = name;
        this.status = status;
        this.created = created;
        this.percent = percent;
        this.pid = pid;
    }

    public int getImage() {
        return image;
    }

    public String getProgress() {
        return progress;
    }

    public String getName() {
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
}
