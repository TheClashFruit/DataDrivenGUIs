package me.theclashfruit.ddg.lib.actions.objects;

import net.minecraft.util.Identifier;

public class OpenScreenAction implements IAction {
    private String screen;

    @Override
    public void execute() {
        Identifier id = Identifier.of(screen);
    }
}
