package me.theclashfruit.ddg.client;

import me.theclashfruit.ddg.lib.CustomScreen;
import me.theclashfruit.ddg.networking.ListScreensPayload;
import me.theclashfruit.ddg.networking.OpenCustomScreenPayload;
import me.theclashfruit.ddg.util.ClientCache;
import me.theclashfruit.ddg.util.DataCache;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Map;

import static me.theclashfruit.ddg.DataDrivenGUIs.LOGGER;

public class DataDrivenGUIsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(OpenCustomScreenPayload.ID, (payload, context) -> {
            Identifier screenId = payload.screenId();
            String title = payload.title();

            @SuppressWarnings("resource")
            MinecraftClient client = context.client();
            client.execute(() -> {
                client.setScreen(new CustomScreen(screenId, Text.translatable(title)));
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(ListScreensPayload.ID, (payload, context) -> {
            ClientCache.setCache(payload.screens());
        });
    }
}
