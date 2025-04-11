package me.theclashfruit.ddg.lib.components;

import me.theclashfruit.ddg.lib.PositionAttr;

import java.util.HashMap;
import java.util.Map;

public interface Component {
    default Boolean canHaveChildren() { return false; }
    default Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("position", PositionAttr.class);

        return attributes;
    }
}
