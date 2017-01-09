package com.company;

public class Sprite extends DrawableObject {
    protected String name;
    protected ImageStorage image;

    // Implementation of access methods
    public ImageStorage getImage() { return image; }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
    public void setImage(ImageStorage image) { this.image = image; }
}
