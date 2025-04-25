package me.theclashfruit.ddg.util;

import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataCache {
    private static Map<Identifier, Resource> screenCache = Map.of();
    private static Map<Identifier, Resource> actionCache = Map.of();

    public static Map<Identifier, String> getData() {
        return screenCache.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                try (InputStream input = entry.getValue().getInputStream()) {
                    StringBuilder textBuilder = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(input, StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            textBuilder.append(line).append("\n");
                        }
                    }
                    return textBuilder.toString();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
    }

    public static void setCache(Map<Identifier, Resource> screenCache, Map<Identifier, Resource> actionCache) {
        DataCache.screenCache = screenCache;
        DataCache.actionCache = actionCache;
    }

    public static List<Identifier> getAllScreenIdentifiers() {
        return List.copyOf(screenCache.keySet());
    }

    public static List<Identifier> getAllActionIdentifiers() {
        return List.copyOf(screenCache.keySet());
    }

    public static void clearCache() {
        screenCache = Map.of();
        actionCache = Map.of();
    }
}
