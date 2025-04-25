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

    private static final PacketCodec<RegistryByteBuf, Map<Identifier, String>> MAP_CODEC = new PacketCodec<>() {
        @Override
        public void encode(RegistryByteBuf buf, Map<Identifier, String> map) {
            buf.writeVarInt(map.size());
            map.forEach((id, value) -> {
                buf.writeIdentifier(id);
                buf.writeString(value);
            });
        }

        @Override
        public Map<Identifier, String> decode(RegistryByteBuf buf) {
            int size = buf.readVarInt();
            Map<Identifier, String> map = new HashMap<>(size);
            for (int i = 0; i < size; i++) {
                Identifier id = buf.readIdentifier();
                String value = buf.readString();
                map.put(id, value);
            }
            return map;
        }
    };

    public static final PacketCodec<RegistryByteBuf, ListScreensPayload> CODEC = PacketCodec.tuple(
        MAP_CODEC,
        ListScreensPayload::screens,
        ListScreensPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
