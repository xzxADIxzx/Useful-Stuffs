package useful.menu.view;

import arc.func.Boolp;
import mindustry.gen.Player;
import useful.menu.MenuFormatter;
import useful.menu.view.MenuInterface.MenuView;

public record MenuOption(String button, Action<MenuView> action) {
    public static MenuOption empty() {
        return new MenuOption("", Action.none());
    }

    public static MenuOption of(Player player, String button, Action<MenuView> action, Object... values) {
        return new MenuOption(MenuFormatter.format(player, button, values), action);
    }

    public static MenuOption of(String button, Action<MenuView> action) {
        return new MenuOption(button, action);
    }

    public static MenuOption of(char icon, Action<MenuView> action) {
        return new MenuOption(String.valueOf(icon), action);
    }

    public static MenuOption disabled(Boolp disabled, String button, Action<MenuView> action) {
        return disabled.get() ?
                new MenuOption("[gray]" + button, Action.none()) :
                new MenuOption(button, action);
    }
}