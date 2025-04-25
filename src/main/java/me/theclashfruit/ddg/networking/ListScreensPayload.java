package me.theclashfruit.ddg.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public record ListScreensPayload(Map<Identifier, String> screens) implements CustomPayload {
    public static final CustomPayload.Id<ListScreensPayload> ID = new CustomPayload.Id<>(Packets.LIST_SCREENS);

    public static final PacketCodec<RegistryByteBuf, ListScreensPayload> CODEC = PacketCodec.tuple(
        Packets.MAP_CODEC,
        ListScreensPayload::screens,
        ListScreensPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
