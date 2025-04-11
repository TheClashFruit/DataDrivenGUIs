package me.theclashfruit.ddg.lib;

import me.theclashfruit.ddg.lib.components.Button;
import me.theclashfruit.ddg.lib.components.Component;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static me.theclashfruit.ddg.DataDrivenGUIs.LOGGER;

public class CustomScreen extends Screen {
    private Screen parent = null;

    private Identifier id;

    private final ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
    private Map<String, Component> components = Map.of(
        "Button", new Button()
    );

    // Constructors
    public CustomScreen(Identifier screenId, Text title, Screen parent) {
        super(title);

        this.parent = parent;
        this.id = screenId;
    }

    public CustomScreen(Identifier screenId, Text title) {
        super(title);

        this.id = screenId;
    }

    @Override
    public void init() {
        try {
            if (resourceManager.getResource(id).isPresent()) {
                Resource resource = resourceManager.getResource(id).orElseThrow();

                try (InputStream input = resource.getInputStream()) {
                    Document doc = DocumentBuilderFactory
                        .newInstance()
                        .newDocumentBuilder()
                        .parse(input);

                    Element root = doc.getDocumentElement();

                    LOGGER.info("Root Element: {}", root.getNodeName());
                }
            } else {
                LOGGER.error("Resource Not Found: {}", id);
            }
        } catch (Exception e) {
            LOGGER.error("Error Creating Screen", e);
        }
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
