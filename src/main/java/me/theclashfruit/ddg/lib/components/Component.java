package me.theclashfruit.ddg.lib.components;

import me.theclashfruit.ddg.lib.attributes.AttributeError;
import me.theclashfruit.ddg.lib.attributes.Position;
import me.theclashfruit.ddg.lib.attributes.AttributeParser;
import net.minecraft.client.gui.Drawable;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

public class Component {
    public String text = "";
    public HashMap<String, Object> attributes = new HashMap<>();

    public Drawable drawable;

    public Component(Node element) {
        this.text = element.getTextContent();
        for (int i = 0; i < element.getAttributes().getLength(); i++) {
            String name = element.getAttributes().item(i).getNodeName();
            String value = element.getAttributes().item(i).getNodeValue();

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

    private Object parseAttribute(String name, String value) {
        if(this.getAttributeTypes().containsKey(name)) {
            AttributeParser<?> parser = this.getAttributeTypes().get(name);
            return parser.parse(value);
        }

        return value;
    }
}
