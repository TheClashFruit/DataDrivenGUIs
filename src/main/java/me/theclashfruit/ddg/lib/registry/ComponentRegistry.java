package me.theclashfruit.ddg.lib.registry;

import me.theclashfruit.ddg.lib.Component;

import java.util.HashMap;

public class ComponentRegistry {
    public static HashMap<String, Class<? extends Component>> components = new HashMap<>();

    public static void register(String id, Class<? extends Component> component) {
        components.put(id, component);
    }
}
