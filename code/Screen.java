package com.gamedev.dreamteam.scriptureengine;

/**
 * Класс, отвечающий за отображаемую область на экране, при рендере
 */
public class Screen {
    private final int position_x;       // Позиция верхнего правого угла по Х, в глобальных координатах в пикселях
    private final int position_y;       // Позиция верхнего правого угла по Y, в глобальных координатах в пикселях
    private final int width;            // Ширина в пикселях
    private final int height;           // Высота в пикселях

    /**
     * Конструктор объекта тип Screen
     * @param pos_x - Позиция верхнего правого угла по Х, в глобальных координатах в пикселях
     * @param pos_y - Позиция верхнего правого угла по Y, в глобальных координатах в пикселях
     * @param width - Ширина в пикселях
     * @param height - Высота в пикселях
     */
    Screen(int pos_x, int pos_y, int width, int height){
        this.position_x = pos_x;
        this.position_y = pos_y;
        this.width = width;
        this.height = height;
    }

    /**
     * @return  положение по оси Х
     */
    public int getPosition_x() {
        return position_x;
    }

    /**
     * @return  положение по оси Y
     */
    public int getPosition_y() {
        return position_y;
    }

    /**
     * @return  ширина области
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return  высота области
     */
    public int getWidth() {
        return width;
    }
}
