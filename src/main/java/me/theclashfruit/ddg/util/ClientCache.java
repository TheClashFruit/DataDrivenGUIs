package me.theclashfruit.ddg.util;

import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ClientCache {
    public static Map<Identifier, String> screenCache = Map.of();
    public static Map<Identifier, String> actionCache = Map.of();

    public static void setScreenCache(Map<Identifier, String> screenCache) {
        ClientCache.screenCache = screenCache;
    }

    public static void setActionCache(Map<Identifier, String> actionCache) {
        ClientCache.actionCache = actionCache;
    }
}
