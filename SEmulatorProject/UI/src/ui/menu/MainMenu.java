package ui.menu;

import ui.menu.option.MenuOption;

import java.util.List;

public class MainMenu extends AbstractMenu{

    public MainMenu(List<MenuOption> options) {
        super("S-Emulator Main Menu","*", options);
    }
}
