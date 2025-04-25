package me.theclashfruit.ddg.builtin.attributes;

import org.intellij.lang.annotations.RegExp;

public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Position of(int x, int y) {
        return new Position(x, y);
    }

    @RegExp
    public static final String REGEX = "\\(\\d+;\\s?\\d+\\)";
}
