package useful.menu;

import arc.struct.Seq;
import mindustry.gen.*;
import mindustry.ui.Menus;
import useful.*;
import useful.menu.Menu.MenuView;

/**
 * Simple menu interface for Mindustry plugins.
 *
 * @author Phinner
 * <a href="https://github.com/Xpdustry/Nucleus/blob/master/nucleus-mindustry-testing/src/main/java/fr/xpdustry/nucleus/mindustry/testing/ui/menu/MenuInterfaceImpl.java">The original version of the code</a>
 */
public class Menu extends Interface<MenuView> {

    public boolean followUp;

    @Override
    public int register() {
        return Menus.registerMenu((player, choice) -> {
            var view = views.remove(player);
            if (view == null) return;

            var option = view.option(choice);

            if (option == null) view.closed.get(view);
            else option.action().get(view);
        });
    }

    @Override
    public MenuView show(Player player, State state, View previous) {
        return views.get(player, () -> {
            var view = new MenuView(player, state, previous);
            transformers.each(transformer -> transformer.get(view));

            if (followUp) Call.followUpMenu(player.con, id, view.title, view.content, view.options.map(options -> options.map(MenuOption::button).toArray(String.class)).toArray(String[].class));
            else Call.menu(player.con, id, view.title, view.content, view.options.map(options -> options.map(MenuOption::button).toArray(String.class)).toArray(String[].class));

            return view;
        });
    }

    public Menu followUp(boolean followUp) {
        this.followUp = followUp;
        return this;
    }

    public class MenuView extends View {
        public String title = "";
        public String content = "";

        public Seq<Seq<MenuOption>> options = new Seq<>();
        public Action<MenuView> closed = Action.none();

        public MenuView(Player player, State state, View previous) {
            super(player, state, previous);
        }

        public MenuView title(String title, Object... values) {
            this.title = Formatter.format(title, player, values);
            return this;
        }

        public MenuView content(String content, Object... values) {
            this.content = Formatter.format(content, player, values);
            return this;
        }

        public MenuView row() {
            this.options.add(new Seq<MenuOption>());
            return this;
        }

        public MenuView option(String button, Object... values) {
            return option(MenuOption.of(Formatter.format(button, player, values)));
        }

        public MenuView option(String button, Action<MenuView> action, Object... values) {
            return option(MenuOption.of(Formatter.format(button, player, values), action));
        }

        public MenuView option(OptionData provider) {
            provider.option(this);
            return this;
        }

        public MenuView option(MenuOption option) {
            if (this.options.isEmpty())
                this.row();

            this.options.peek().add(option);
            return this;
        }

        public MenuView options(int maxPerRow, OptionData... datas) {
            if (this.options.isEmpty() || this.options.peek().any())
                this.row();

            for (var data : datas) {
                if (this.options.peek().size >= maxPerRow)
                    this.row();

                option(data);
            }

            return this;
        }

        public MenuView options(int maxPerRow, MenuOption... options) {
            if (this.options.isEmpty() || this.options.peek().any())
                this.row();

            for (var option : options) {
                if (this.options.peek().size >= maxPerRow)
                    this.row();

                option(option);
            }

            return this;
        }

        public MenuOption option(int id) {
            if (id < 0) return null;

            int i = 0;
            for (var row : options) {
                i += row.size;
                if (i > id)
                    return row.get(id - i + row.size);
            }

            return null;
        }

        public MenuView closed(Action<MenuView> closed) {
            this.closed = closed;
            return this;
        }

        public interface OptionData {
            // MUST add an option
            void option(MenuView menu);
        }
    }
}