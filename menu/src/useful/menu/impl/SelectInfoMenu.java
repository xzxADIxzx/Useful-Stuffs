package useful.menu.impl;

import arc.func.Cons;
import arc.func.Cons2;
import useful.Action2;
import useful.State.StateKey;
import useful.menu.Menu;

public class SelectInfoMenu<T> extends SelectMenu<T> {

    public final StateKey<T> CONTENT = new StateKey<>();
    public final Menu info = new Menu();

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
        super(close);
        this.action(Action2.openWith(info, CONTENT));
    }
}