package useful.menu.impl;

import arc.func.Func;
import arc.func.Prov;
import arc.struct.Seq;
import useful.Action2;
import useful.menu.Menu;
import useful.menu.MenuOption;

public class SelectMenu<T> extends Menu {
    public Prov<Seq<T>> content;
    public int options;

    public Func<T, String> button;
    public Action2<MenuView, T> action;

    public SelectMenu<T> content(Prov<Seq<T>> content) {
        this.content = content;
        return this;
    }

    public SelectMenu<T> options(int options) {
        this.options = options;
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

    {
        transform(menu -> menu.options(options, content.get().map(value -> new MenuOption(button.get(value), view -> action.get(view, value))).<MenuOption>toArray(MenuOption.class)));
    }
}