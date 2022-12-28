package useful.menu.view;

import mindustry.gen.Player;
import useful.menu.MenuFormatter;

public record MenuOption(String button, Action action) {
    public static MenuOption empty() {
        return new MenuOption("", Action.none());
    }

    public static MenuOption of(String button, Player player, Action action, Object... values) {
        return new MenuOption(MenuFormatter.format(button, player, values), action);
    }

    public static MenuOption of(String button, Action action) {
        return new MenuOption(button, action);
    }

    public static MenuOption of(char icon, Action action) {
        return new MenuOption(String.valueOf(icon), action);
    }

    public static MenuOption disabled(boolean disabled, String button, Action action) {
        return disabled ?
                new MenuOption("[gray]" + button, Action.none()) :
                new MenuOption(button, action);
    }
}