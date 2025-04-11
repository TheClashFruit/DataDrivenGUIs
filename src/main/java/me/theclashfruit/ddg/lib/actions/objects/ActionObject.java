package me.theclashfruit.ddg.lib.actions.objects;

import me.theclashfruit.ddg.lib.actions.enums.ActionType;

public class ActionObject {
    private ActionType type;
    private IAction action;

    public ActionType getType() {
        return this.type;
    }
    public IAction getAction() {
        return this.action;
    }

    public void setType(ActionType type) {
        this.type = type;
    }
    public void setAction(IAction action) {
        this.action = action;
    }
}
