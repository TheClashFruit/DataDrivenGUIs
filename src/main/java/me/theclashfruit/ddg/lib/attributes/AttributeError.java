package me.theclashfruit.ddg.lib.attributes;

public class AttributeError extends RuntimeException {
    public AttributeError(String attribute, String value) {
        super("Invalid value for " + attribute + "; Value was `" + value + "`");
    }
}
