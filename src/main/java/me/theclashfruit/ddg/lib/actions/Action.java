package me.theclashfruit.ddg.lib.actions;

import com.google.gson.Gson;
import me.theclashfruit.ddg.DataDrivenGUIs;
import me.theclashfruit.ddg.lib.actions.objects.ActionObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;

import static me.theclashfruit.ddg.DataDrivenGUIs.LOGGER;

public class Action {
    private final Gson gson = new Gson();

    private ActionObject actionObject = null;

    public Action(Identifier id) {
        try {
            ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();

            if (resourceManager.getResource(id).isPresent()) {
                Resource resource = resourceManager.getResource(id).orElseThrow();

                try (InputStream input = resource.getInputStream()) {
                    this.actionObject = gson.fromJson(new InputStreamReader(input), ActionObject.class);
                } catch (Exception e) {
                    LOGGER.error("Error reading resource: {}", id, e);
                }
            } else {
                LOGGER.error("Resource not found: {}", id);
            }
        } catch (Exception e) {
            LOGGER.error("Error parsing JSON file: {}", id, e);
        }
    }

    public void execute() {
        this.actionObject.getAction().execute();
    }
}
