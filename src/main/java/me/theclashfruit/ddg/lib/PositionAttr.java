package me.theclashfruit.ddg.lib;

import org.intellij.lang.annotations.RegExp;

public class PositionAttr {
    int x;
    int y;

    public PositionAttr(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static PositionAttr of(int x, int y) {
        return new PositionAttr(x, y);
    }

    @RegExp
    public static final String REGEX = "\\(\\d+;\\s?\\d+\\)";
}
