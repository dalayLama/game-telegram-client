package org.quizstorage.components.telegram;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum Command {

    START("/start", "start", false),
    HELP("/help", "help"),
    NEW_GAME("/newgame", "new_game");

    private final String command;

    private final String descriptionCode;

    private final boolean showInMenu;

    Command(String command, String descriptionCode) {
        this(command, descriptionCode, true);
    }

    Command(String command, String descriptionCode, boolean showInMenu) {
        this.command = command.startsWith("/") ? command : "/" + command;
        this.descriptionCode = descriptionCode;
        this.showInMenu = showInMenu;
    }

    public String getName() {
        return getCommand().substring(1);
    }

    public boolean is(String command) {
        if (command == null) {
            return false;
        }
        String actualCommand = "/" + getCommand();
        return Objects.equals(actualCommand, command.trim().toLowerCase());
    }

}
