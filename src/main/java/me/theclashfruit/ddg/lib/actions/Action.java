package me.theclashfruit.ddg.lib.actions;

import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;

import static me.theclashfruit.ddg.DataDrivenGUIs.LOGGER;

public interface Action {
    Identifier getType();
    Side getSide();
    Class<?> getActionModel();
    <T> boolean execute(ActionModel<T> model);
}
