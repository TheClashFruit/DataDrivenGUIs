package me.theclashfruit.ddg.builtin.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.theclashfruit.ddg.lib.actions.Action;
import me.theclashfruit.ddg.lib.actions.ActionModel;
import me.theclashfruit.ddg.lib.actions.ActionRegistry;
import me.theclashfruit.ddg.lib.attribute.AttributeError;
import me.theclashfruit.ddg.lib.attribute.AttributeParser;
import me.theclashfruit.ddg.builtin.attributes.Position;
import me.theclashfruit.ddg.lib.component.Component;
import me.theclashfruit.ddg.util.ClientCache;
import me.theclashfruit.ddg.util.DataCache;
import me.theclashfruit.ddg.util.IdentifierTypeAdapter;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.resource.Resource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.w3c.dom.Node;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Map;

import static me.theclashfruit.ddg.DataDrivenGUIs.LOGGER;

public class ButtonComponent extends Component {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(Identifier.class, new IdentifierTypeAdapter())
        .create();

    public ButtonComponent(Node element) {
        super(element);

        ButtonWidget.Builder btn = ButtonWidget
            .builder(Text.translatable(this.text), (buttonWidget) -> {
                JsonObject res = (JsonObject) this.attributes.get("action");
                Identifier id = Identifier.of(res.get("type").getAsString());

                Class<? extends Action> action = ActionRegistry.actions.get(id);

                if (action == null) {
                    LOGGER.error("Action not found: {}", id);
                    return;
                }

                try {
                    Constructor<? extends Action> constructor = action.getConstructor();
                    Action actionInstance = constructor.newInstance();

                    Class<?> model = actionInstance.getActionModel();
                    Object actionObj = gson.fromJson(res.get("action"), model);

                    actionInstance.execute(new ActionModel<>(id, actionObj));
                } catch (Exception e) {
                    LOGGER.error("Error executing action.", e);
                }
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
        attributes.put("action", value -> {
            Gson gson = new GsonBuilder()
                .registerTypeAdapter(Identifier.class, new IdentifierTypeAdapter())
                .create();

            for (Identifier id : ClientCache.actionCache.keySet()) {
                if (id.toString().equals(value)) {
                    return gson.fromJson(ClientCache.actionCache.get(id), JsonObject.class);
                }
            }

            throw new AttributeError("action", value);
        });
        attributes.put("width", Integer::parseInt);
        attributes.put("height", Integer::parseInt);

        return attributes;
    }
}
