package useful.menu.impl;

import arc.func.*;
import arc.struct.Seq;
import mindustry.entities.EntityGroup;
import mindustry.gen.Player;
import useful.*;
import useful.State.StateKey;
import useful.menu.Menu;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ListMenu extends Menu {
    public static final StateKey<Integer>
            PAGE = new StateKey<>("page"),
            PAGES = new StateKey<>("pages");

    public static final StateKey<Func>
            TITLE = new StateKey<>("title"),
            CONTENT = new StateKey<>("content");

    public ListMenu() {
        this("ui.button.left", "ui.button.right", "ui.button.page", "ui.button.close");
    }

    public ListMenu(String left, String right, String page, String close) {
        this.transform(menu -> {
            menu.title((String) menu.state.get(TITLE).get(menu.state.get(PAGE)));
            menu.content((String) menu.state.get(CONTENT).get(menu.state.get(PAGE)));

            menu.option(left, Action.showWith(PAGE, Math.max(1, menu.state.get(PAGE) - 1)));
            menu.option(page, Action.show(), menu.state.get(PAGE), menu.state.get(PAGES));
            menu.option(right, Action.showWith(PAGE, Math.min(menu.state.get(PAGE) + 1, menu.state.get(PAGES))));

            menu.row();

            menu.option(close);
        }).followUp(true);
    }

    // region content values

    public <T> MenuView show(Player player, int page, int pages, int valuesPerPage, String title, Iterable<T> values, Cons3<StringBuilder, Integer, T> cons) {
        return show(player, page, pages, title, newPage -> format(wrap(values), newPage, valuesPerPage, cons));
    }

    public <T> MenuView show(Player player, int page, int pages, int valuesPerPage, Func<Integer, String> title, Iterable<T> values, Cons3<StringBuilder, Integer, T> cons) {
        return show(player, page, pages, title, newPage -> format(wrap(values), newPage, valuesPerPage, cons));
    }

    public <T> MenuView show(Player player, MenuView parent, int page, int pages, int valuesPerPage, String title, Iterable<T> values, Cons3<StringBuilder, Integer, T> cons) {
        return show(player, parent, page, pages, title, newPage -> format(wrap(values), newPage, valuesPerPage, cons));
    }

    public <T> MenuView show(Player player, MenuView parent, int page, int pages, int valuesPerPage, Func<Integer, String> title, Iterable<T> values, Cons3<StringBuilder, Integer, T> cons) {
        return show(player, parent, page, pages, title, newPage -> format(wrap(values), newPage, valuesPerPage, cons));
    }

    public <T> MenuView show(Player player, int page, int pages, int valuesPerPage, String title, Prov<Iterable<T>> values, Cons3<StringBuilder, Integer, T> cons) {
        return show(player, page, pages, title, newPage -> format(wrap(values.get()), newPage, valuesPerPage, cons));
    }

    public <T> MenuView show(Player player, int page, int pages, int valuesPerPage, Func<Integer, String> title, Prov<Iterable<T>> values, Cons3<StringBuilder, Integer, T> cons) {
        return show(player, page, pages, title, newPage -> format(wrap(values.get()), newPage, valuesPerPage, cons));
    }

    public <T> MenuView show(Player player, MenuView parent, int page, int pages, int valuesPerPage, String title, Prov<Iterable<T>> values, Cons3<StringBuilder, Integer, T> cons) {
        return show(player, parent, page, pages, title, newPage -> format(wrap(values.get()), newPage, valuesPerPage, cons));
    }

    public <T> MenuView show(Player player, MenuView parent, int page, int pages, int valuesPerPage, Func<Integer, String> title, Prov<Iterable<T>> values, Cons3<StringBuilder, Integer, T> cons) {
        return show(player, parent, page, pages, title, newPage -> format(wrap(values.get()), newPage, valuesPerPage, cons));
    }

    // endregion
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
    // region utils

    private static <T> Seq<T> wrap(Iterable<T> values) {
        if (values instanceof Seq<T> seq)
            return seq;

        return Seq.with(values);
    }

    private static <T> String format(Seq<T> values, int page, int valuesPerPage, Cons3<StringBuilder, Integer, T> cons) {
        var builder = new StringBuilder();

        for (int i = valuesPerPage * (page - 1); i < Math.min(valuesPerPage * page, values.size); i++) {
            if (!builder.isEmpty()) builder.append("\n\n");
            cons.get(builder, i + 1, values.get(i));
        }

        return builder.toString();
    }

    // endregion
}