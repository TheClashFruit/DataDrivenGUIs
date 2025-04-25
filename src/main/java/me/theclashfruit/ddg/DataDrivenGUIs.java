package me.theclashfruit.ddg;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.theclashfruit.ddg.builtin.actions.OpenScreenAction;
import me.theclashfruit.ddg.lib.registry.ActionRegistry;
import me.theclashfruit.ddg.lib.registry.ComponentRegistry;
import me.theclashfruit.ddg.builtin.components.ButtonComponent;
import me.theclashfruit.ddg.builtin.components.HLayoutComponent;
import me.theclashfruit.ddg.builtin.components.TextComponent;
import me.theclashfruit.ddg.builtin.components.VLayoutComponent;
import me.theclashfruit.ddg.networking.ListActionsPayload;
import me.theclashfruit.ddg.networking.ListScreensPayload;
import me.theclashfruit.ddg.networking.OpenCustomScreenPayload;
import me.theclashfruit.ddg.util.DataCache;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Objects;

public class DataDrivenGUIs implements ModInitializer {
    public static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger("DataDrivenGUIs");

    private static MinecraftServer server;

    @Override
    public void onInitialize() {
        LOGGER.info("DataDrivenGUIs is initializing...");

        // Register components for `Data Driven GUIs`.
        ComponentRegistry.register("HorizontalLayout", HLayoutComponent.class);
        ComponentRegistry.register("VerticalLayout", VLayoutComponent.class);
        ComponentRegistry.register("Button", ButtonComponent.class);
        ComponentRegistry.register("Text", TextComponent.class);

        // Register actions for `Data Driven GUIs`.
        ActionRegistry.register(Identifier.of("ddg", "open_screen"), OpenScreenAction.class);

        PayloadTypeRegistry.playS2C().register(OpenCustomScreenPayload.ID, OpenCustomScreenPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(ListScreensPayload.ID, ListScreensPayload.CODEC);

        PayloadTypeRegistry.playS2C().register(ListActionsPayload.ID, ListActionsPayload.CODEC);

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            DataDrivenGUIs.server = server;
        });
        ServerPlayConnectionEvents.JOIN.register((handler, player, server) -> {
            player.sendPacket(new ListScreensPayload(DataCache.getScreenData()));
            player.sendPacket(new ListActionsPayload(DataCache.getActionData()));
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                CommandManager.literal("ddg")
                    .then(CommandManager.literal("open")
                        .then(CommandManager.argument("screen", IdentifierArgumentType.identifier()).suggests((context, builder) -> {
                            DataCache.getAllScreenIdentifiers()
                                .forEach(identifier -> builder.suggest(identifier.toString()));
                            return builder.buildFuture();
                        })
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

        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return Identifier.of("ddg", "resources");
            }

            @Override
            public void reload(ResourceManager manager) {
                Map<Identifier, Resource> screens = manager.findResources("screen", identifier -> identifier.getPath().endsWith(".xml"));
                Map<Identifier, Resource> actions = manager.findResources("action", identifier -> identifier.getPath().endsWith(".json"));

                DataCache.clearCache();
                DataCache.setCache(screens, actions);

                ListScreensPayload screensPayload = new ListScreensPayload(DataCache.getScreenData());
                ListActionsPayload actionsPayload = new ListActionsPayload(DataCache.getActionData());

                if (server != null)
                    for (ServerPlayerEntity player : PlayerLookup.all(server)) {
                        ServerPlayNetworking.send(player, screensPayload);
                        ServerPlayNetworking.send(player, actionsPayload);
                    }
            }
        });
    }

    private int executeOpenMenu(CommandContext<ServerCommandSource> context, boolean hasPlayerArgument) throws CommandSyntaxException {
        Identifier screen = IdentifierArgumentType.getIdentifier(context, "screen");
        String title = StringArgumentType.getString(context, "title");
        ServerPlayerEntity player = hasPlayerArgument
            ? EntityArgumentType.getPlayer(context, "player")
            : Objects.requireNonNull(context.getSource().getPlayer());

        if (!DataCache.getAllScreenIdentifiers().contains(screen)) {
            context.getSource().sendError(Text.translatable("ddg.command.screen_not_found"));
            return 0;
        }

        //ServerPlayNetworking.send(player, new ListScreensPayload(DataCache.getData()));
        ServerPlayNetworking.send(player, new OpenCustomScreenPayload(screen, title));
        return 1;
    }
}
