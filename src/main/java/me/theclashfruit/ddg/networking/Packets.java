package me.theclashfruit.ddg.networking;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class Packets {
    public static final Identifier OPEN_CUSTOM_SCREEN = Identifier.of("ddg", "open_custom_screen");

    public static final Identifier LIST_SCREENS = Identifier.of("ddg", "list_screens");
    public static final Identifier LIST_ACTIONS = Identifier.of("ddg", "list_actions");

    public static final PacketCodec<RegistryByteBuf, Map<Identifier, String>> MAP_CODEC = new PacketCodec<>() {
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
}
