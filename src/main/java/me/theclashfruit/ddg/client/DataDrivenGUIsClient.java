package me.theclashfruit.ddg.client;

import me.theclashfruit.ddg.Networking;
import me.theclashfruit.ddg.lib.CustomScreen;
import me.theclashfruit.networking.OpenCustomScreenPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DataDrivenGUIsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(OpenCustomScreenPayload.ID, (payload, context) -> {
            Identifier screenId = payload.screenId();
            String title = payload.title();

            context.client().execute(() -> {
                CustomScreen screen = new CustomScreen(screenId, Text.translatable(title));

                context.client().setScreen(screen);
            });
        });
    }
}
