package me.theclashfruit.ddg.lib.components;

import me.theclashfruit.ddg.lib.attributes.AttributeParser;
import me.theclashfruit.ddg.lib.attributes.Position;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.w3c.dom.Node;

import java.util.Map;

import static me.theclashfruit.ddg.DataDrivenGUIs.LOGGER;

public class ButtonComponent extends Component {
    public ButtonComponent(Node element) {
        super(element);

        ButtonWidget.Builder btn = ButtonWidget
            .builder(Text.translatable(this.text), (buttonWidget) -> {
                LOGGER.info((String) this.attributes.get("action"));
            })
            .size(
                this.attributes.get("width") != null ? (int) this.attributes.get("width") : 200,
                this.attributes.get("height") != null ? (int) this.attributes.get("height") : 20
            );

        Position pos = (Position) this.attributes.get("position");
        if (pos != null)
            btn.position(pos.x, pos.y);
        else
            btn.position(0, 0);

        this.drawable = btn.build();
        this.widget = btn.build();
    }

    @Override
    public Map<String, AttributeParser<?>> getAttributeTypes() {
        Map<String, AttributeParser<?>> attributes = super.getAttributeTypes();
        attributes.put("action", value -> value);
        attributes.put("width", Integer::parseInt);
        attributes.put("height", Integer::parseInt);

        return attributes;
    }
}
