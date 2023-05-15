package useful.menu.impl;

import arc.func.Func;
import arc.func.Prov;
import useful.Action2;
import useful.menu.Menu;

public class SelectMenu<T> extends Menu {

    public SelectMenu(Prov<Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this(1, content, button, action);
    }

    public SelectMenu(Func<MenuView, Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this(1, content, button, action);
    }

    public SelectMenu(String close, Prov<Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this(close, 1, content, button, action);
    }

    public SelectMenu(String close, Func<MenuView, Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this(close, 1, content, button, action);
    }

    public SelectMenu(int optionsPerRow, Prov<Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this("ui.button.close", optionsPerRow, content, button, action);
    }

    public SelectMenu(int optionsPerRow, Func<MenuView, Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this("ui.button.close", optionsPerRow, content, button, action);
    }

    public SelectMenu(String close, int optionsPerRow, Prov<Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this.transform(menu -> {
            menu.options(optionsPerRow, content.get(), button, action).row();
            menu.option(close);
        });
    }

    public SelectMenu(String close, int optionsPerRow, Func<MenuView, Iterable<T>> content, Func<T, String> button, Action2<MenuView, T> action) {
        this.transform(menu -> {
            menu.options(optionsPerRow, content.get(menu), button, action).row();
            menu.option(close);
        });
    }
}