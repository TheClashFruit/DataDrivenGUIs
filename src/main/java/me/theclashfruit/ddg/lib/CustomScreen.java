package me.theclashfruit.ddg.lib;

import me.theclashfruit.ddg.lib.components.Component;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

import static me.theclashfruit.ddg.DataDrivenGUIs.LOGGER;
import static me.theclashfruit.ddg.lib.ComponentRegistry.components;

public class CustomScreen extends Screen {
    private Screen parent = null;

    private final Identifier id;
    private final ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();

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

                    if (root.getNodeName().equals("Root")) {
                        NodeList nl = root.getChildNodes();

                        for (int i = 0; i < nl.getLength(); i++) {
                            Class<? extends Component> componentClass = components.get(nl.item(i).getNodeName());
                            if (componentClass != null) {
                                Component component = componentClass
                                    .getConstructor(Node.class)
                                    .newInstance(nl.item(i));

                                this.addDrawable(component.drawable);
                            }
                        }
                    }
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
