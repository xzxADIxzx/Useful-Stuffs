package useful.menu.impl;

import arc.func.*;
import mindustry.gen.Player;
import useful.Action2;
import useful.State.StateKey;
import useful.menu.Menu;

@SuppressWarnings("unchecked")
public class SelectMenu<T> extends Menu {

    public Func<Player, Iterable<T>> content;
    public int optionsPerRow = 1;

    public Func<T, String> button;
    public Action2<MenuView, T> action;

    public SelectMenu() {
        this("ui.button.close");
    }

    public SelectMenu(String close) {
        this.transform(menu -> {
            menu.options(optionsPerRow, content.get(menu.player), button, action).row();
            menu.option(close);
        });
    }

    @Override
    public SelectMenu<T> transform(Cons<MenuView> transformer) {
        return (SelectMenu<T>) super.transform(transformer);
    }

    @Override
    public <T1> SelectMenu<T> transform(StateKey<T1> key, Cons2<MenuView, T1> transformer) {
        return (SelectMenu<T>) super.transform(view -> transformer.get(view, view.state.get(key)));
    }

    @Override
    public <T1, T2> SelectMenu<T> transform(StateKey<T1> key1, StateKey<T2> key2, Cons3<MenuView, T1, T2> transformer) {
        return (SelectMenu<T>) super.transform(view -> transformer.get(view, view.state.get(key1), view.state.get(key2)));
    }

    @Override
    public SelectMenu<T> followUp(boolean followUp) {
        return (SelectMenu<T>) super.followUp(followUp);
    }

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

    public SelectMenu<T> options(int optionsPerRow) {
        this.optionsPerRow = optionsPerRow;
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
}