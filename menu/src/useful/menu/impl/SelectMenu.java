package useful.menu.impl;

import arc.func.Func;
import arc.func.Prov;
import arc.struct.Seq;
import useful.Action2;
import useful.menu.Menu;
import useful.menu.MenuOption;

public class SelectMenu<T> extends Menu {
    public Prov<Seq<T>> content;
    public int optionsPerRow;

    public Func<T, String> button;
    public Action2<MenuView, T> action;

    public void content(Prov<Seq<T>> content) {
        this.content = content;
    }

    public void optionsPerRow(int optionsPerRow) {
        this.optionsPerRow = optionsPerRow;
    }

    public void button(Func<T, String> button) {
        this.button = button;
    }

    public void action(Action2<MenuView, T> action) {
        this.action = action;
    }

    {
        transform(menu -> menu.options(optionsPerRow, content.get().map(value -> new MenuOption(button.get(value), view -> action.get(view, value))).<MenuOption>toArray(MenuOption.class)));
    }
}