package me.theclashfruit.ddg;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.theclashfruit.networking.OpenCustomScreenPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.util.Objects;

public class DataDrivenGUIs implements ModInitializer {
    public static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger("DataDrivenGUIs");

    @Override
    public void onInitialize() {
        LOGGER.info("DataDrivenGUIs is initializing...");

        PayloadTypeRegistry.playS2C().register(OpenCustomScreenPayload.ID, OpenCustomScreenPayload.CODEC);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                CommandManager.literal("ddg")
                    .then(CommandManager.literal("open")
                        .then(CommandManager.argument("screen", StringArgumentType.string())
                            .then(CommandManager.argument("title", StringArgumentType.string())
                                .then(CommandManager.argument("player", EntityArgumentType.player())
                                    .requires(source -> source.hasPermissionLevel(2))
                                    .executes(context -> executeOpenMenu(context, true))
                                )
                                .requires(ServerCommandSource::isExecutedByPlayer)
                                .executes(context -> executeOpenMenu(context, false))
                            )
                        )
                    )
            );
        });
    }

    private int executeOpenMenu(CommandContext<ServerCommandSource> context, boolean hasPlayerArgument) throws CommandSyntaxException {
        String screen = StringArgumentType.getString(context, "screen");
        String title = StringArgumentType.getString(context, "title");
        ServerPlayerEntity player = hasPlayerArgument
            ? EntityArgumentType.getPlayer(context, "player")
            : Objects.requireNonNull(context.getSource().getPlayer());

        ServerPlayNetworking.send(player, new OpenCustomScreenPayload(Identifier.of(screen), title));
        return 1;
    }
}
