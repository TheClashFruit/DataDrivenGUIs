package me.theclashfruit.ddg.lib.actions.enums;

public enum ActionType {
    OPEN_SCREEN("ddg:open_screen"),
    CLOSE_SCREEN("ddg:close_screen"),
    OPEN_URL("ddg:open_url"),
    OPEN_FILE("ddg:open_file"),
    RUN_COMMAND("ddg:run_command"),
    RUN_SCRIPT("ddg:run_script"),
    RUN_FUNCTION("ddg:run_function"),
    RUN_KEYBIND("ddg:run_keybind"),
    RUN_KEYBIND_RELEASE("ddg:run_keybind_release"),
    RUN_KEYBIND_PRESS("ddg:run_keybind_press");

    private final String name;

    ActionType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static ActionType fromName(String name) {
        for (ActionType type : ActionType.values()) {
            if (type.toString().equals(name)) {
                return type;
            }
        }

        return null;
    }
}
