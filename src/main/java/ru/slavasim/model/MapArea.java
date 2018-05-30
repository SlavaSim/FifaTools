package ru.slavasim.model;

import lombok.Data;

import java.awt.*;

@Data
public class MapArea {
    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public Rectangle asRect() {
        return new Rectangle(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
    }
}
