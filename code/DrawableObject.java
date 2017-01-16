package com.company;

public class DrawableObject extends BaseObject implements IDrawable {

    // Object properties
    protected int width;
    protected int height;
    protected int angle;
    protected int position_x;
    protected int position_y;
    protected int opacity;
    protected boolean isInvisible;

    // Implementation of getters
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getAngle() { return angle; }
    public int getPositionX() { return position_x; }
    public int getPositionY() { return position_y; }
    public int getOpacity() { return opacity; }
    public boolean getIsInvisible() { return isInvisible; }

    // Implementation of setters
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public void setAngle(int angle) { this.angle = angle; }
    public void setPositionX(int position_x) { this.position_x = position_x; }
    public void setPositionY(int position_y) { this.position_y = position_y; }
    public void setOpacity(int opacity) { this.opacity = opacity; }
    public void setIsInvisible(boolean isInvisible) { this.isInvisible = isInvisible; }

    // Implementation of draw method
    public void draw() {

    }
}
