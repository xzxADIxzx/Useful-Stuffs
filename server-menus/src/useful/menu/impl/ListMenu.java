package useful.menu.impl;

import arc.func.Func;
import mindustry.gen.Player;
import useful.menu.view.Action;
import useful.menu.view.Menu;
import useful.menu.view.State.StateKey;

public class ListMenu extends Menu {
    public static final StateKey<Integer>
            PAGE = new StateKey<>("page", Integer.class),
            PAGES = new StateKey<>("pages", Integer.class);

    public String left, right, page, close;

    public ListMenu set(String left, String right, String page, String close) {
        this.left = left;
        this.right = right;
        this.page = page;
        this.close = close;

        return this;
    }

    public MenuView show(Player player, int _page, int _pages, Func<Integer, String> title, Func<Integer, String> content) {
        return showWith(player, PAGE, _page, PAGES, _pages, menu -> {
            menu.title(title.get(menu.get(PAGE)));
            menu.content(content.get(menu.get(PAGE)));

            menu.addOption(left, Action.showGet(PAGE, page -> Math.max(1, page - 1)));
            menu.addOption(page, Action.show(), menu.get(PAGE), menu.get(PAGES));
            menu.addOption(right, Action.showGet(PAGE, page -> Math.min(page + 1, menu.get(PAGES))));

            menu.row();

            menu.addOption(close);
        });
    }
}