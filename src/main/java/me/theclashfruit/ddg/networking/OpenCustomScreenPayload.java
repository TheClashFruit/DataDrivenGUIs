package me.theclashfruit.ddg.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record OpenCustomScreenPayload(Identifier screenId, String title) implements CustomPayload {
    public static final CustomPayload.Id<OpenCustomScreenPayload> ID = new CustomPayload.Id<>(Packets.OPEN_CUSTOM_SCREEN);

    public static final PacketCodec<RegistryByteBuf, OpenCustomScreenPayload> CODEC = PacketCodec.tuple(
        Identifier.PACKET_CODEC, OpenCustomScreenPayload::screenId,
        PacketCodecs.STRING, OpenCustomScreenPayload::title,
        OpenCustomScreenPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
