package me.theclashfruit.ddg.lib;

import me.theclashfruit.ddg.lib.action.ActionModel;
import me.theclashfruit.ddg.lib.action.Side;
import org.jetbrains.annotations.ApiStatus;

public interface IAction {
    @ApiStatus.Experimental
    Side getSide();
    Class<?> getActionModel();
    <T> boolean execute(ActionModel<T> model);
}
