package com.example.aftermealdiary.item;

public class BannerData {

    int backgroundColor;
    int circleColor;
    String title;
    String info;


    public BannerData(int backgroundColor, int circleColor, String title, String info){
        this.backgroundColor = backgroundColor;
        this.circleColor = circleColor;
        this.title = title;
        this.info = info;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String text) {
        this.info = text;
    }
}
