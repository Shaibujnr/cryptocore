package com.devlab.cryptocore.models;

/**
 * Created by shaibu on 10/24/17.
 */

public class SpinnerItem {
    private String item_text;
    private int item_image;

    public SpinnerItem(String item_text, int item_image) {
        this.item_text = item_text;
        this.item_image = item_image;
    }

    public String getItem_text() {
        return item_text;
    }

    public void setItem_text(String item_text) {
        this.item_text = item_text;
    }

    public int getItem_image() {
        return item_image;
    }

    public void setItem_image(int item_image) {
        this.item_image = item_image;
    }
}
