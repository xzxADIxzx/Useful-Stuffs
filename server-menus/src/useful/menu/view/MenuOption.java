package useful.menu.view;

import arc.func.Cons;
import mindustry.gen.Player;
import useful.menu.MenuFormatter;

public record MenuOption(String button, Action action) {
    public static MenuOption empty() {
        return new MenuOption("", Action.none());
    }

    public static MenuOption none(String button, Player player, Object... values) {
        return new MenuOption(MenuFormatter.format(button, player, values), Action.none());
    }

    public static MenuOption none(String button) {
        return new MenuOption(button, Action.none());
    }

    public static MenuOption none(char icon) {
        return new MenuOption(String.valueOf(icon), Action.none());
    }

    public static MenuOption player(String button, Player player, Cons<Player> cons, Object... values) {
        return new MenuOption(MenuFormatter.format(button, player, values), view -> cons.get(view.player));
    }

    public static MenuOption player(String button, Cons<Player> cons) {
        return new MenuOption(button, view -> cons.get(view.player));
    }

    public static MenuOption player(char icon, Cons<Player> cons) {
        return new MenuOption(String.valueOf(icon), view -> cons.get(view.player));
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
}