package useful.menu.impl;

import arc.func.Func;
import mindustry.gen.Player;
import useful.*;
import useful.State.StateKey;
import useful.menu.Menu;

@SuppressWarnings({"unchecked", "rawtypes"})
public class PageMenu extends Menu {
    public static final StateKey<Integer>
            PAGE = new StateKey<>("page"),
            PAGES = new StateKey<>("pages");

    public static final StateKey<Func>
            TITLE = new StateKey<>("title"),
            CONTENT = new StateKey<>("content");

    public PageMenu() {
        this("ui.button.left", "ui.button.right", "ui.button.page", "ui.button.close");
    }

    public PageMenu(String leftButton, String rightButton, String pageButton, String closeButton) {
        this.transform(menu -> {
            menu.title((String) menu.state.get(TITLE).get(menu.state.get(PAGE)));
            menu.content((String) menu.state.get(CONTENT).get(menu.state.get(PAGE)));

            menu.option(leftButton, Action.showWith(PAGE, Math.max(1, menu.state.get(PAGE) - 1)));
            menu.option(pageButton, Action.show(), menu.state.get(PAGE), menu.state.get(PAGES));
            menu.option(rightButton, Action.showWith(PAGE, Math.min(menu.state.get(PAGE) + 1, menu.state.get(PAGES))));

            menu.row();

            menu.option(closeButton);
        }).followUp(true);
    }

    // region content function

    public MenuView show(Player player, int page, int pages, String title, Func<Integer, String> content) {
        return show(player, page, pages, newPage -> title, content);
    }

    public MenuView show(Player player, int page, int pages, Func<Integer, String> title, Func<Integer, String> content) {
        return show(player, State.create().put(PAGE, page).put(PAGES, pages).put(TITLE, title).put(CONTENT, content));
    }

    public MenuView show(Player player, MenuView parent, int page, int pages, String title, Func<Integer, String> content) {
        return show(player, parent, page, pages, newPage -> title, content);
    }

    public MenuView show(Player player, MenuView parent, int page, int pages, Func<Integer, String> title, Func<Integer, String> content) {
        return show(player, State.create().put(PAGE, page).put(PAGES, pages).put(TITLE, title).put(CONTENT, content), parent);
    }

    // endregion
}