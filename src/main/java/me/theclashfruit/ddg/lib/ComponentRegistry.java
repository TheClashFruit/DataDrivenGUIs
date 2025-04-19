package me.theclashfruit.ddg.lib;

import me.theclashfruit.ddg.lib.components.Component;

import java.util.HashMap;

public class ComponentRegistry {
    public static HashMap<String, Class<? extends Component>> components = new HashMap<>();

    public static void register(String id, Class<? extends Component> component) {
        components.put(id, component);
    }
}
