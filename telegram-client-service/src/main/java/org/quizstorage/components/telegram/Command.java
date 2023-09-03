package org.quizstorage.components.telegram;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
@Getter
public enum Command {

    START("start", "start", false),
    HELP("help", "help"),
    NEW_GAME("newgame", "new_game");

    private final String command;

    private final String descriptionCode;

    private final boolean showInMenu;

    Command(String command, String descriptionCode) {
        this.command = command;
        this.descriptionCode = descriptionCode;
        this.showInMenu = true;
    }

    public boolean is(String command) {
        if (command == null) {
            return false;
        }
        String actualCommand = "/" + getCommand();
        return Objects.equals(actualCommand, command.trim().toLowerCase());
    }

}
