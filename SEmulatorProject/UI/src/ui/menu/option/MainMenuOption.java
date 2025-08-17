package ui.menu.option;


public enum MainMenuOption implements MenuOption {
    LOAD_PROGRAM("Load XML Program"),
    SHOW_PROGRAM("Show Loaded Program Instructions"),
    EXPAND_PROGRAM("Expand Loaded Program Instructions"),
    RUN_PROGRAM("Run Loaded Program"),
    SHOW_HISTORY("Show Loaded Program History"),
    EXIT_PROGRAM("Exit Program");

    private final String menuDisplay;

    MainMenuOption(String display) {
        this.menuDisplay = display;
    }

    public String getMenuDisplay() {
        return menuDisplay;
    }
}
