package me.theclashfruit.ddg.builtin.components;

import me.theclashfruit.ddg.lib.attribute.AttributeParser;
import me.theclashfruit.ddg.lib.component.Component;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import org.w3c.dom.Node;

import java.util.List;
import java.util.Map;

public class HLayoutComponent extends Component {
    public HLayoutComponent(Node element) {
        super(element);

        this.widget = new DirectionalLayoutWidget(0, 0, DirectionalLayoutWidget.DisplayAxis.HORIZONTAL)
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
