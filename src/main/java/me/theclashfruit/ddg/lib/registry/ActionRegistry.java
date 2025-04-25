package me.theclashfruit.ddg.lib.registry;

import me.theclashfruit.ddg.lib.IAction;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class ActionRegistry {
    public static HashMap<Identifier, Class<? extends IAction>> actions = new HashMap<>();

    public static void register(Identifier id, Class<? extends IAction> action) {
        actions.put(id, action);
    }
}
