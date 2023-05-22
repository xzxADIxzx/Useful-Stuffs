package useful.menu.impl;

import arc.func.Func;
import arc.func.Prov;
import mindustry.gen.Player;
import useful.Action2;
import useful.menu.Menu;

public class SelectMenu<T> extends Menu {

    public Func<Player, Iterable<T>> content;
    public Func<T, String> button;

    public Action2<MenuView, T> action;

    public SelectMenu<T> content(Iterable<T> content) {
        return content(player -> content);
    }

    public SelectMenu<T> content(Prov<Iterable<T>> content) {
        return content(player -> content.get());
    }

    public SelectMenu<T> content(Func<Player, Iterable<T>> content) {
        this.content = content;
        return this;
    }

    public SelectMenu<T> button(Func<T, String> button) {
        this.button = button;
        return this;
    }

    public SelectMenu<T> action(Action2<MenuView, T> action) {
        this.action = action;
        return this;
    }

    public SelectMenu() {
        this("ui.button.close");
    }

    public SelectMenu(String close) {
        this.transform(menu -> {
            menu.options(1, content.get(menu.player), button, action).row();
            menu.option(close);
        });
    }
}