package com.company;

import java.util.Map;

public class Layout implements IDrawable {

    // Layout properties
    // Key = DrawableObject.id
    Map<Integer, DrawableObject> hashmap;
    private int opacity;
    private int angle;
    private int paralax_x;
    private int paralax_y;

    // Implementation of getters
    public int getOpacity() { return opacity; }
    public int getAngle() { return angle; }
    public int getParalaxX() { return paralax_x; }
    public int getParalaxY() { return paralax_y; }

    // Implementation of setters
    public void setOpacity(int opacity) { this.opacity = opacity; }
    public void setAngle(int angle) { this.angle = angle; }
    public void setParalaxX(int paralax_x) { this.paralax_x = paralax_x; }
    public void setParalaxY(int paralax_y) { this.paralax_y = paralax_y; }

    // Implementation of draw method
    public void draw() {

    }

}
