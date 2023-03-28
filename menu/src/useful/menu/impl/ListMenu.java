package useful.menu.impl;

import arc.func.Func;
import mindustry.gen.Player;
import useful.Action;
import useful.State;
import useful.State.StateKey;
import useful.menu.Menu;

@SuppressWarnings("unchecked")
public class ListMenu extends Menu {
    public static final StateKey<Integer>
            PAGE = new StateKey<>("page", Integer.class),
            PAGES = new StateKey<>("pages", Integer.class);

    public static final StateKey<Func>
            TITLE = new StateKey<>("title", Func.class),
            CONTENT = new StateKey<>("content", Func.class);

    public String left, right, page, close;

    public void left(String left) {
        this.left = left;
    }

    public void right(String right) {
        this.right = right;
    }

    public void page(String page) {
        this.page = page;
    }

    public void close(String close) {
        this.close = close;
    }

    {
        transform(menu -> {
            menu.title((String) menu.state.get(TITLE).get(menu.state.get(PAGE)));
            menu.content((String) menu.state.get(CONTENT).get(menu.state.get(PAGE)));

            menu.option(left, Action.showWith(PAGE, Math.max(1, menu.state.get(PAGE) - 1)));
            menu.option(page, Action.show(), menu.state.get(PAGE), menu.state.get(PAGES));
            menu.option(right, Action.showWith(PAGE, Math.min(menu.state.get(PAGE) + 1, menu.state.get(PAGES))));

            menu.row();

            menu.option(close);
        });
    }

    public MenuView show(Player player, int page, int pages, String title, Func<Integer, String> content) {
        return show(player, page, pages, newPage -> title, content);
    }

    public MenuView show(Player player, int page, int pages, Func<Integer, String> title, Func<Integer, String> content) {
        return show(player, State.create().put(PAGE, page).put(PAGES, pages).put(TITLE, title).put(CONTENT, content));
    }
}