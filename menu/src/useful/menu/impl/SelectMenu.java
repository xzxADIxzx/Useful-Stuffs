package useful.menu.impl;

import arc.func.Func;
import arc.func.Prov;
import mindustry.gen.Player;
import useful.Action2;
import useful.menu.Menu;

public class SelectMenu extends Menu {

    public <T> SelectMenu(Prov<Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this(1, content, button, action);
    }

    public <T> SelectMenu(Func<Player, Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this(1, content, button, action);
    }

    public <T> SelectMenu(String close, Prov<Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this(close, 1, content, button, action);
    }

    public <T> SelectMenu(String close, Func<Player, Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this(close, 1, content, button, action);
    }

    public <T> SelectMenu(int optionsPerRow, Prov<Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this("ui.button.close", optionsPerRow, content, button, action);
    }

    public <T> SelectMenu(int optionsPerRow, Func<Player, Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this("ui.button.close", optionsPerRow, content, button, action);
    }

    public <T> SelectMenu(String close, int optionsPerRow, Prov<Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this.transform(menu -> {
            menu.options(optionsPerRow, content.get(), button, action).row();
            menu.option(close);
        });
    }

    public <T> SelectMenu(String close, int optionsPerRow, Func<Player, Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this.transform(menu -> {
            menu.options(optionsPerRow, content.get(menu.player), button, action).row();
            menu.option(close);
        });
    }
}