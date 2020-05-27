package com.example.aftermealdiary.item;

import android.graphics.drawable.Drawable;

public class MenuData {

    Drawable menuIcon;
    String pickerInfo;
    String menuName;
    String additionalInfo;


    public MenuData(Drawable menuIcon, String pickerInfo, String menuName, String additionalInfo) {
        this.menuIcon = menuIcon;
        this.pickerInfo = pickerInfo;
        this.menuName = menuName;
        this.additionalInfo = additionalInfo;
    }

    public Drawable getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(Drawable menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getPickerInfo() {
        return pickerInfo;
    }

    public void setPickerInfo(String pickerInfo) {
        this.pickerInfo = pickerInfo;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
