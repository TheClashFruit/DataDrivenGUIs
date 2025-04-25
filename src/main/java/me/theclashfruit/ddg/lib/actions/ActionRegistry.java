package me.theclashfruit.ddg.lib.actions;

import me.theclashfruit.ddg.lib.component.Component;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class ActionRegistry {
    public static HashMap<Identifier, Class<? extends Action>> actions = new HashMap<>();

    public static void register(Identifier id, Class<? extends Action> action) {
        actions.put(id, action);
    }
}
