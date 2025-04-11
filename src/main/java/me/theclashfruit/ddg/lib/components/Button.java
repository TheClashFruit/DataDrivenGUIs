package me.theclashfruit.ddg.lib.components;

import java.util.Map;

public class Button implements Component {
    @Override
    public Map<String, Object> getAttributes() {
        Map<String, Object> attributes = Component.super.getAttributes();
        attributes.put("action", String.class);
        attributes.put("width", Integer.class);
        attributes.put("height", Integer.class);

        return attributes;
    }
}
