package me.theclashfruit.ddg.lib.attributes;

@FunctionalInterface
public interface AttributeParser<T> {
    T parse(String value);
}