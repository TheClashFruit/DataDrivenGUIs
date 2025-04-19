package me.theclashfruit.ddg.lib.components;

import me.theclashfruit.ddg.lib.attributes.AttributeParser;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Map;

public class VLayoutComponent extends Component {
    public VLayoutComponent(Node element) {
        super(element);

        this.widget = new DirectionalLayoutWidget(0, 0, DirectionalLayoutWidget.DisplayAxis.VERTICAL)
            .spacing(attributes.get("gap") != null ? (int) attributes.get("gap") : 0);

        List<Component> children = this.getChildren();
        children.forEach((child) -> {
            DirectionalLayoutWidget w = (DirectionalLayoutWidget) this.widget;

            if (child.widget != null)
                w.add(child.widget);

            this.widget = w;
        });
    }

    @Override
    public Map<String, AttributeParser<?>> getAttributeTypes() {
        Map<String, AttributeParser<?>> attributes = super.getAttributeTypes();
        attributes.put("gap", Integer::parseInt);

        return attributes;
    }

    public static boolean canBeRoot() {
        return true;
    }
}
