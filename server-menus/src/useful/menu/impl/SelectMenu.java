package useful.menu.impl;

import arc.func.*;
import arc.struct.Seq;
import mindustry.gen.Player;
import useful.menu.view.*;

public class SelectMenu<T> extends Menu {
    public int maxPerRow;
    public Func2<MenuView, T, String> button;

    public SelectMenu<T> set(int maxPerRow, Func<T, String> button) {
        return set(maxPerRow, (menu, value) -> button.get(value));
    }

    public SelectMenu<T> set(int maxPerRow, Func2<MenuView, T, String> button) {
        this.maxPerRow = maxPerRow;
        this.button = button;

        return this;
    }

    public MenuView show(Player player, Seq<T> values, Cons<T> cons) {
        return show(player, menu -> {
            var options = values.map(value -> new MenuOption(button.get(menu, value), Action.run(() -> cons.get(value))));
            menu.addOptionsRow(maxPerRow, (MenuOption[]) options.toArray(MenuOption.class));
        });
    }
}