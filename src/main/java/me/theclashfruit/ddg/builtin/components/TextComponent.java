package me.theclashfruit.ddg.builtin.components;

import me.theclashfruit.ddg.builtin.attributes.Position;
import me.theclashfruit.ddg.lib.component.Component;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import org.w3c.dom.Node;

public class TextComponent extends Component {
    public TextComponent(Node element) {
        super(element);

        this.drawable = new TextWidget(
            Text.translatable(this.text),
            MinecraftClient.getInstance().textRenderer
        );

        Position pos = (Position) this.attributes.get("position");
        if (pos != null) {
            ((TextWidget) this.drawable).setX(pos.x);
            ((TextWidget) this.drawable).setY(pos.y);
        }

        this.widget = (TextWidget) this.drawable;
    }
}
