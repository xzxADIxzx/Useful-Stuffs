package useful;

public record MenuOption(String button, Action action) {
    public static MenuOption empty() {
        return new MenuOption("", Action.none());
    }

    public static MenuOption of(String button) {
        return new MenuOption(button, Action.none());
    }

    public static MenuOption of(String button, Action action) {
        return new MenuOption(button, action);
    }
}