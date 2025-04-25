package me.theclashfruit.ddg.lib.action;

public enum Side {
    CLIENT("client"),
    SERVER("server"),
    BOTH("*");

    private final String value;

    Side(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
