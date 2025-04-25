package me.theclashfruit.ddg.util;

import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ClientCache {
    public static Map<Identifier, String> screenCache = Map.of();

    public static void setCache(Map<Identifier, String> screenCache) {
        ClientCache.screenCache = screenCache;
    }
}
