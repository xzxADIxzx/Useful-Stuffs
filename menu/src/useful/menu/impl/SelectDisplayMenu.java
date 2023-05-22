package useful.menu.impl;

import arc.func.*;
import mindustry.gen.Player;
import useful.*;
import useful.menu.Menu;
import useful.State.StateKey;

public class SelectDisplayMenu<T> extends SelectMenu<T> {

    public final StateKey<T> CONTENT = new StateKey<>();
    public final Menu display = new Menu();

    public SelectDisplayMenu() {
        this("ui.button.back", "ui.button.close");
    }

    public SelectDisplayMenu(String back, String close) {
        super(close);

        this.action(Action2.openWith(display, CONTENT));
        this.display(menu -> {
            menu.option(back, Action.back());
            menu.option(close);
        });
    }

    @Override
    public SelectDisplayMenu<T> transform(Cons<MenuView> transformer) {
        return (SelectDisplayMenu<T>) super.transform(transformer);
    }

    @Override
    public <T1> SelectDisplayMenu<T> transform(StateKey<T1> key, Cons2<MenuView, T1> transformer) {
        return (SelectDisplayMenu<T>) super.transform(view -> transformer.get(view, view.state.get(key)));
    }

    @Override
    public <T1, T2> SelectDisplayMenu<T> transform(StateKey<T1> key1, StateKey<T2> key2, Cons3<MenuView, T1, T2> transformer) {
        return (SelectDisplayMenu<T>) super.transform(view -> transformer.get(view, view.state.get(key1), view.state.get(key2)));
    }

    @Override
    public SelectDisplayMenu<T> followUp(boolean followUp) {
        return (SelectDisplayMenu<T>) super.followUp(followUp);
    }

    public SelectDisplayMenu<T> content(Iterable<T> content) {
        return (SelectDisplayMenu<T>) super.content(player -> content);
    }

    public SelectDisplayMenu<T> content(Prov<Iterable<T>> content) {
        return (SelectDisplayMenu<T>) super.content(player -> content.get());
    }

    public SelectDisplayMenu<T> content(Func<Player, Iterable<T>> content) {
        return (SelectDisplayMenu<T>) super.content(content);
    }

    public SelectDisplayMenu<T> options(int optionsPerRow) {
        return (SelectDisplayMenu<T>) super.options(optionsPerRow);
    }

    public SelectDisplayMenu<T> button(Func<T, String> button) {
        return (SelectDisplayMenu<T>) super.button(button);
    }

    public SelectDisplayMenu<T> display(Cons<MenuView> transformer) {
        this.display.transform(transformer);
        return this;
    }

    public SelectDisplayMenu<T> display(Cons2<MenuView, T> transformer) {
        this.display.transform(CONTENT, transformer);
        return this;
    }
}