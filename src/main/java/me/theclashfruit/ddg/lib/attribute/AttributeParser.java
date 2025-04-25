package me.theclashfruit.ddg.lib.attribute;

@FunctionalInterface
public interface AttributeParser<T> {
    T parse(String value);
}