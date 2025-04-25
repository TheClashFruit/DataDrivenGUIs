package me.theclashfruit.ddg.builtin.actions;

import me.theclashfruit.ddg.lib.CustomScreen;
import me.theclashfruit.ddg.lib.IAction;
import me.theclashfruit.ddg.lib.action.ActionModel;
import me.theclashfruit.ddg.lib.action.Side;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class OpenScreenAction implements IAction {
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
    }
}
