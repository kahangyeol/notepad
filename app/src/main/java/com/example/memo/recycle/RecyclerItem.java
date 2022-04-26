package com.example.memo.recycle;

public class RecyclerItem {
    private int imageResId;

    private String strName;

    public RecyclerItem(int a_resId, String a_strName) {
        imageResId = a_resId;
        strName = a_strName;
    }

    public int getImageResId() {
        return imageResId;
    }


}
