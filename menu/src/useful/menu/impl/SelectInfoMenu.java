package useful.menu.impl;

import arc.func.Cons;
import arc.func.Cons2;
import arc.func.Func;
import arc.func.Prov;
import mindustry.gen.Player;
import useful.Action2;
import useful.State.StateKey;
import useful.menu.Menu;

public class SelectInfoMenu<T> extends Menu {

    public final StateKey<T> CONTENT = new StateKey<>();
    public final Menu info = new Menu();

    public Func<Player, Iterable<T>> content;
    public Func<T, String> button;

    public SelectInfoMenu<T> content(Iterable<T> content) {
        return content(player -> content);
    }

    public SelectInfoMenu<T> content(Prov<Iterable<T>> content) {
        return content(player -> content.get());
    }

    public SelectInfoMenu<T> content(Func<Player, Iterable<T>> content) {
        this.content = content;
        return this;
    }

    public SelectInfoMenu<T> button(Func<T, String> button) {
        this.button = button;
        return this;
    }

    public SelectInfoMenu<T> info(Cons<MenuView> transformer) {
        return info((menu, value) -> transformer.get(menu));
    }

    public SelectInfoMenu<T> info(Cons2<MenuView, T> transformer) {
        this.info.transform(CONTENT, transformer);
        return this;
    }

    public SelectInfoMenu() {
        this("ui.button.close");
    }

    public SelectInfoMenu(String close) {
        this.transform(menu -> {
            menu.options(1, content.get(menu.player), button, Action2.openWith(info, CONTENT)).row();
            menu.option(close);
        });
    }
}