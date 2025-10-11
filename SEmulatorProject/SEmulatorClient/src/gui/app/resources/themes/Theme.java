package gui.app.resources.themes;

public enum Theme {
    CLASSIC("Classic", "/gui/app/resources/themes/classic-theme.css"),
    LIGHT("Light", "/gui/app/resources/themes/light-theme.css"),
    MATRIX("Matrix", "/gui/app/resources/themes/matrix-theme.css");

    private final String displayName;
    private final String cssFilePath;

    Theme(String displayName, String cssFilePath) {
        this.displayName = displayName;
        this.cssFilePath = cssFilePath;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCssFilePath() {
        return cssFilePath;
    }
}
