package me.theclashfruit.ddg.lib;

import me.theclashfruit.ddg.lib.component.Component;
import me.theclashfruit.ddg.util.ClientCache;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.ThreePartsLayoutWidget;
import net.minecraft.resource.ResourceManager;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static me.theclashfruit.ddg.DataDrivenGUIs.LOGGER;
import static me.theclashfruit.ddg.lib.component.ComponentRegistry.components;

public class CustomScreen extends Screen {
    private Screen parent = null;

    private final Identifier id;
    private final ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();

    private ThreePartsLayoutWidget layout;

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
            if(!ClientCache.screenCache.containsKey(id))
                throw new RuntimeException("Screen not found in cache: " + id);

            try (InputStream input = new ByteArrayInputStream(ClientCache.screenCache.get(id).getBytes())) {
                Document doc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(input);

                Element root = doc.getDocumentElement();

                // filter components to that can be root
                List<String> rootComponents = new ArrayList<>();
                components.forEach((id, component) -> {
                    // get static method canBeRoot
                    try {
                        Method method = component.getMethod("canBeRoot");
                        boolean canBeRoot = (boolean) method.invoke(null);
                        if (canBeRoot) rootComponents.add(id);
                        LOGGER.info("{} - canBeRoot: {}", id, canBeRoot);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

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
                } else if (rootComponents.contains(root.getNodeName())) {
                    if (root.getNodeName().equals("TabLayout")) return;

                    this.layout = new ThreePartsLayoutWidget(this);
                    this.layout.addHeader(this.title, this.textRenderer);

                    Class<? extends Component> componentClass = components.get(root.getNodeName());
                    if (componentClass != null) {
                        Component component = componentClass
                            .getConstructor(Node.class)
                            .newInstance(root);

                        this.layout.addBody(component.widget);
                    }

                    DirectionalLayoutWidget footer = this.layout.addFooter(DirectionalLayoutWidget.horizontal().spacing(8));
                    footer.add(ButtonWidget.builder(ScreenTexts.DONE, button -> this.close()).size(200, 20).build());

                    this.layout.forEachChild(this::addDrawableChild);
                    //#if MC < 1.20.2
                    //$$ this.initTabNavigation();
                    //#else
                    this.layout.refreshPositions();
                    //#endif
                } else throw new RuntimeException("Invalid Root Node: " + root.getNodeName());
            }
        } catch (Exception e) {
            LOGGER.error("Error Creating Screen", e);

            this.close();
            if (parent != null) MinecraftClient.getInstance().setScreen(parent);
        }
    }

    //#if MC < 1.20.2
    //$$ @Override
    //$$ protected void initTabNavigation() {
    //$$     if (this.layout != null)
    //$$         this.layout.refreshPositions();
    //$$ }
    //#endif

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
