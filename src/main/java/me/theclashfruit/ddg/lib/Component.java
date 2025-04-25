package me.theclashfruit.ddg.lib;

import me.theclashfruit.ddg.lib.attribute.AttributeError;
import me.theclashfruit.ddg.builtin.attributes.Position;
import me.theclashfruit.ddg.lib.attribute.AttributeParser;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.Widget;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.theclashfruit.ddg.DataDrivenGUIs.LOGGER;
import static me.theclashfruit.ddg.lib.registry.ComponentRegistry.components;

public class Component {
    public String text = "";
    public HashMap<String, Object> attributes = new HashMap<>();

    public Drawable drawable;
    public Widget widget;

    private final Node element;

    public Component(Node element) {
        this.element = element;
        this.text = this.element.getTextContent();

        for (int i = 0; i < this.element.getAttributes().getLength(); i++) {
            String name = this.element.getAttributes().item(i).getNodeName();
            String value = this.element.getAttributes().item(i).getNodeValue();

            attributes.put(name, parseAttribute(name, value));
        }
    }

    public Map<String, AttributeParser<?>> getAttributeTypes() {
        Map<String, AttributeParser<?>> attributes = new HashMap<>();
        attributes.put("position", value -> {
            if (value.matches(Position.REGEX)) {
                String[] parts = value.substring(1, value.length() - 1).split(";");

                return Position.of(
                    Integer.parseInt(parts[0].trim()),
                    Integer.parseInt(parts[1].trim())
                );
            }

            throw new AttributeError("position", value);
        });

        return attributes;
    }

    public static boolean canBeRoot() {
        return false;
    }

    protected List<Component> getChildren() {
        NodeList nl = this.element.getChildNodes();
        ArrayList<Component> cl = new ArrayList<>();

        try {
            for (int i = 0; i < nl.getLength(); i++) {
                Class<? extends Component> componentClass = components.get(nl.item(i).getNodeName());
                if (componentClass != null) {
                    Component component = componentClass
                        .getConstructor(Node.class)
                        .newInstance(nl.item(i));

                    cl.add(component);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to get children", e);
        }

        return cl;
    }

    private Object parseAttribute(String name, String value) {
        if(this.getAttributeTypes().containsKey(name)) {
            AttributeParser<?> parser = this.getAttributeTypes().get(name);
            return parser.parse(value);
        }

        return value;
    }
}
