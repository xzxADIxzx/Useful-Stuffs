package useful.menu;

public record MenuOption(String content, Action<MenuInterface.MenuView> action) {
    public static final MenuOption EMPTY = new MenuOption("", Action.none());

    public static MenuOption empty() {
        return EMPTY;
    }

    public static MenuOption of(String content, Action<MenuInterface.MenuView> action) {
        return new MenuOption(content, action);
    }

    public static MenuOption of(char icon, Action<MenuInterface.MenuView> action) {
        return new MenuOption(String.valueOf(icon), action);
    }
}