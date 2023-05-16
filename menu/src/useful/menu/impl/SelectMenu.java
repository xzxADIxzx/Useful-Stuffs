package useful.menu.impl;

import arc.func.Func;
import arc.func.Prov;
import mindustry.gen.Player;
import useful.Action2;
import useful.State;
import useful.State.StateKey;
import useful.menu.Menu;

@SuppressWarnings({"unchecked", "rawtypes"})
public class SelectMenu extends Menu {

    public static final StateKey<Func> BUTTON = new StateKey<>(Func.class);
    public static final StateKey<Action2> ACTION = new StateKey<>(Action2.class);

    // region button and action provided via a key

    public <T> SelectMenu(Prov<Iterable<T>> content) {
        this(1, content);
    }

    public <T> SelectMenu(Func<Player, Iterable<T>> content) {
        this(1, content);
    }

    public <T> SelectMenu(String close, Prov<Iterable<T>> content) {
        this(close, 1, content);
    }

    public <T> SelectMenu(String close, Func<Player, Iterable<T>> content) {
        this(close, 1, content);
    }

    public <T> SelectMenu(int optionsPerRow, Prov<Iterable<T>> content) {
        this("ui.button.close", optionsPerRow, content);
    }

    public <T> SelectMenu(int optionsPerRow, Func<Player, Iterable<T>> content) {
        this("ui.button.close", optionsPerRow, content);
    }

    public <T> SelectMenu(String close, int optionsPerRow, Prov<Iterable<T>> content) {
        this.transform(menu -> {
            menu.options(optionsPerRow, content.get(), menu.state.get(BUTTON), menu.state.get(ACTION)).row();
            menu.option(close);
        });
    }

    public <T> SelectMenu(String close, int optionsPerRow, Func<Player, Iterable<T>> content) {
        this.transform(menu -> {
            menu.options(optionsPerRow, content.get(menu.player), menu.state.get(BUTTON), menu.state.get(ACTION)).row();
            menu.option(close);
        });
    }

    // endregion
    // region button and action provided via a constructor

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

    // endregion

    public <T> void show(Player player, Func<T, String> button, Action2<MenuView, T> action) {
        show(player, State.create(BUTTON, button).put(ACTION, action));
    }

    public <T> void show(Player player, Func<T, String> button, Action2<MenuView, T> action, MenuView parent) {
        show(player, State.create(BUTTON, button).put(ACTION, action), parent);
    }
}