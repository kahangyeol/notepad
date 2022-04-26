package com.example.memo;

import android.graphics.drawable.Drawable;

public class FolderListItem {
    private Drawable iconDrawble;
    private String textStr;

    public Drawable getIconDrawble() {
        return iconDrawble;
    }

    public void setIconDrawble(Drawable iconDrawble) {
        this.iconDrawble = iconDrawble;
    }

    public String getTextStr() {
        return textStr;
    }

    public void setTextStr(String textStr) {
        this.textStr = textStr;
    }
}
