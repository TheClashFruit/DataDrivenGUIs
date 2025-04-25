package me.theclashfruit.ddg.builtin.actions;

import me.theclashfruit.ddg.lib.CustomScreen;
import me.theclashfruit.ddg.lib.actions.Action;
import me.theclashfruit.ddg.lib.actions.ActionModel;
import me.theclashfruit.ddg.lib.actions.Side;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class OpenScreenAction implements Action {
    @Override
    public Identifier getType() {
        return null;
    }

    @Override
    public Side getSide() {
        return Side.CLIENT;
    }

    @Override
    public Class<?> getActionModel() {
        return OpenScreenActionModel.class;
    }

    @Override
    public <T> boolean execute(ActionModel<T> model) {
        ActionModel<OpenScreenActionModel> openScreenModel = (ActionModel<OpenScreenActionModel>) model;
        MinecraftClient.getInstance().setScreen(new CustomScreen(openScreenModel.action.screen, Text.literal(openScreenModel.action.title)));
        return true;
    }

    public static class OpenScreenActionModel {
        public Identifier screen;
        public String title;

        public OpenScreenActionModel(String namespace, String id, String title) {
            this.screen = Identifier.of(namespace, id);
            this.title = title;
        }
        public OpenScreenActionModel(String id, String title) {
            this.screen = Identifier.of(id);
            this.title = title;
        }
        public OpenScreenActionModel(Identifier id, String title) {
            this.screen = id;
            this.title = title;
        }
    }
}
