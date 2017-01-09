package com.company;

public class Text extends DrawableObject {

    // Text properties
    protected TextStorage text;
    private String text_content;
    private String font;
    private byte size;
    private byte style;
    private byte alignment_v;
    private byte alignment_h;

    // Implementation of getters
    public TextStorage getText() { return text; }
    public String getTextContent() { return text_content; }
    public String getFont() { return font; }
    public byte getSize() { return size; }
    public byte getStyle() { return style; }
    public byte getAlignmentV() { return alignment_v; }
    public byte getAlignmentH() { return alignment_h; }

    // Implementation of setters
    public void setText(TextStorage text) { this.text = text; }
    public void setTextContent(String text) { this.text_content = text; }
    public void setFont(String font) { this.font = font; }
    public void setSize(byte size) { this.size = size; }
    public void setStyle(byte style) { this.style = style; }
    public void setAlignmentV(byte alignment_v) { this.alignment_v = alignment_v; }
    public void setAlignmentH(byte alignment_h) { this.alignment_h = alignment_h; }
}
