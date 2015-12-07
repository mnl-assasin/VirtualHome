package com.olfu.virtualhomeinteriordesigning.model;

/**
 * Created by mykelneds on 10/17/15.
 */
public class ImageItem {

    String name;
    String image;

    public ImageItem(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}