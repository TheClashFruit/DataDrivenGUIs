package me.theclashfruit.ddg.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.Map;

public record ListActionsPayload(Map<Identifier, String> actions) implements CustomPayload {
    public static final Id<ListActionsPayload> ID = new Id<>(Packets.LIST_ACTIONS);

    public static final PacketCodec<RegistryByteBuf, ListActionsPayload> CODEC = PacketCodec.tuple(
        Packets.MAP_CODEC,
        ListActionsPayload::actions,
        ListActionsPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
