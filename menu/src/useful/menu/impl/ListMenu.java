package useful.menu.impl;

import arc.func.*;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.gen.Player;
import useful.*;
import useful.State.StateKey;
import useful.menu.Menu;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ListMenu extends Menu {
    public static final StateKey<Integer>
            PAGE = new StateKey<>("page");

    public static final StateKey<Func>
            TITLE = new StateKey<>("title"),
            CONTENT = new StateKey<>("content");

    public static final StateKey<Cons3>
            FORMATTER = new StateKey<>("formatter");

    public final int maxPerPage;

    public ListMenu(int maxPerPage) {
        this(maxPerPage, "ui.button.left", "ui.button.right", "ui.button.page", "ui.button.close");
    }

    public ListMenu(int maxPerPage, String leftButton, String rightButton, String pageButton, String closeButton) {
        this.maxPerPage = maxPerPage;
        this.transform(menu -> {
            var content = (Seq<Object>) menu.state.get(CONTENT).get(menu);
            var formatter = (Cons3<StringBuilder, Integer, Object>) menu.state.get(FORMATTER);

            int pages = Math.max(1, Mathf.ceil((float) content.size / maxPerPage));
            int page = Math.min(menu.state.get(PAGE), pages);

            menu.title((String) menu.state.get(TITLE).get(page));
            menu.content(format(content, page, formatter));

            menu.option(leftButton, Action.showWith(PAGE, Math.max(1, page - 1)));
            menu.option(pageButton, Action.show(), page, pages);
            menu.option(rightButton, Action.showWith(PAGE, Math.min(page + 1, pages)));

            menu.row();

            menu.option(closeButton);
        }).followUp(true);
    }

    // region show

    public <T> MenuView show(Player player, String title, Seq<T> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, 1, newPage -> title, menu -> content, formatter);
    }

    public <T> MenuView show(Player player, Func<Integer, String> title, Seq<T> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, 1, title, menu -> content, formatter);
    }

    public <T> MenuView show(Player player, String title, Prov<Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, 1, newPage -> title, menu -> content.get(), formatter);
    }

    public <T> MenuView show(Player player, Func<Integer, String> title, Prov<Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, 1, title, menu -> content.get(), formatter);
    }

    public <T> MenuView show(Player player, String title, Func<MenuView, Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, 1, newPage -> title, content, formatter);
    }

    public <T> MenuView show(Player player, Func<Integer, String> title, Func<MenuView, Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, 1, title, content, formatter);
    }

    public <T> MenuView show(Player player, int page, String title, Seq<T> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, page, newPage -> title, menu -> content, formatter);
    }

    public <T> MenuView show(Player player, int page, Func<Integer, String> title, Seq<T> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, page, title, menu -> content, formatter);
    }

    public <T> MenuView show(Player player, int page, String title, Prov<Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, page, newPage -> title, menu -> content.get(), formatter);
    }

    public <T> MenuView show(Player player, int page, Func<Integer, String> title, Prov<Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, page, title, menu -> content.get(), formatter);
    }

    public <T> MenuView show(Player player, int page, String title, Func<MenuView, Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, page, newPage -> title, content, formatter);
    }

    public <T> MenuView show(Player player, int page, Func<Integer, String> title, Func<MenuView, Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, State.create(PAGE, page).put(TITLE, title).put(CONTENT, content).put(FORMATTER, formatter));
    }


    public <T> MenuView show(Player player, MenuView parent, String title, Seq<T> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, parent, 1, newPage -> title, menu -> content, formatter);
    }

    public <T> MenuView show(Player player, MenuView parent, Func<Integer, String> title, Seq<T> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, parent, 1, title, menu -> content, formatter);
    }

    public <T> MenuView show(Player player, MenuView parent, String title, Prov<Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, parent, 1, newPage -> title, menu -> content.get(), formatter);
    }

    public <T> MenuView show(Player player, MenuView parent, Func<Integer, String> title, Prov<Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, parent, 1, title, menu -> content.get(), formatter);
    }

    public <T> MenuView show(Player player, MenuView parent, String title, Func<MenuView, Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, parent, 1, newPage -> title, content, formatter);
    }

    public <T> MenuView show(Player player, MenuView parent, Func<Integer, String> title, Func<MenuView, Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, parent, 1, title, content, formatter);
    }

    public <T> MenuView show(Player player, MenuView parent, int page, String title, Seq<T> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, parent, page, newPage -> title, menu -> content, formatter);
    }

    public <T> MenuView show(Player player, MenuView parent, int page, Func<Integer, String> title, Seq<T> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, parent, page, title, menu -> content, formatter);
    }

    public <T> MenuView show(Player player, MenuView parent, int page, String title, Prov<Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, parent, page, newPage -> title, menu -> content.get(), formatter);
    }

    public <T> MenuView show(Player player, MenuView parent, int page, Func<Integer, String> title, Prov<Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, parent, page, title, menu -> content.get(), formatter);
    }

    public <T> MenuView show(Player player, MenuView parent, int page, String title, Func<MenuView, Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, parent, page, newPage -> title, content, formatter);
    }

    public <T> MenuView show(Player player, MenuView parent, int page, Func<Integer, String> title, Func<MenuView, Seq<T>> content, Cons3<StringBuilder, Integer, T> formatter) {
        return show(player, State.create(PAGE, page).put(TITLE, title).put(CONTENT, content).put(FORMATTER, formatter), parent);
    }

    // endregion
    // region utils

    private String format(Seq<Object> content, int page, Cons3<StringBuilder, Integer, Object> formatter) {
        var builder = new StringBuilder();

        for (int i = maxPerPage * (page - 1); i < Math.min(maxPerPage * page, content.size); i++) {
            if (!builder.isEmpty()) builder.append("\n\n");
            formatter.get(builder, i + 1, content.get(i));
        }

        return builder.toString();
    }

    // endregion
}