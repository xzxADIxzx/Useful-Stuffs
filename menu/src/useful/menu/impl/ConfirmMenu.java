package useful.menu.impl;

import mindustry.gen.Player;
import useful.Action;
import useful.State;
import useful.State.StateKey;
import useful.menu.Menu;

public class ConfirmMenu extends Menu {
    public static final StateKey<String>
            TITLE = new StateKey<>(),
            CONTENT = new StateKey<>();

    public static final StateKey<Object[]>
            VALUES = new StateKey<>();

    public static final StateKey<Runnable>
            CONFIRMED = new StateKey<>(),
            DENIED = new StateKey<>();

    public ConfirmMenu() {
        this("ui.button.yes", "ui.button.no");
    }

    public ConfirmMenu(String yes, String no) {
        this.transform(menu -> {
            menu.title(menu.state.get(TITLE));
            menu.content(menu.state.get(CONTENT), menu.state.get(VALUES));

            menu.option(yes, Action.run(menu.state.get(CONFIRMED)));
            menu.option(no, Action.run(menu.state.get(DENIED)));
        });
    }

    public MenuView show(Player player, String title, String content, Runnable confirmed, Object... values) {
        return show(player, title, content, confirmed, () -> {}, values);
    }

    public MenuView show(Player player, String title, String content, Runnable confirmed, Runnable denied, Object... values) {
        return show(player, State.create().put(TITLE, title).put(CONTENT, content).put(VALUES, values).put(CONFIRMED, confirmed).put(DENIED, denied));
    }

    public MenuView show(Player player, MenuView parent, String title, String content, Runnable confirmed, Object... values) {
        return show(player, parent, title, content, confirmed, () -> {}, values);
    }

    public MenuView show(Player player, MenuView parent, String title, String content, Runnable confirmed, Runnable denied, Object... values) {
        return show(player, State.create().put(TITLE, title).put(CONTENT, content).put(VALUES, values).put(CONFIRMED, confirmed).put(DENIED, denied), parent);
    }
}