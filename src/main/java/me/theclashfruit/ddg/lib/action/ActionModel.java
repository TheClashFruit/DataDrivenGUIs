package me.theclashfruit.ddg.lib.action;

import net.minecraft.util.Identifier;

public class ActionModel<T> {
    public Identifier type;
    public T action;

    public ActionModel(Identifier type, T action) {
        this.type = type;
        this.action = action;
    }

    public Identifier getType() {
        return type;
    }

    public T getAction() {
        return action;
    }

    public void setType(Identifier type) {
        this.type = type;
    }

    public void setAction(T action) {
        this.action = action;
    }
}
