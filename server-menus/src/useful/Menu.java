package useful;

import arc.Events;
import arc.func.*;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import mindustry.game.EventType.PlayerLeave;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;

public class Menu {
    public final ObjectMap<Player, MenuView> views = new ObjectMap<>();
    public final Seq<Cons<MenuView>> transformers = new Seq<>();

    public final int id;

    {
        this.id = Menus.registerMenu((player, choice) -> {
            var view = views.remove(player);
            if (view == null) return;

            var option = view.option(choice);
            if (option == null) return;

            option.action().get(view);
        });

        Events.on(PlayerLeave.class, event -> views.remove(event.player));
    }

    public Menu transform(Cons<MenuView> transformer) {
        this.transformers.add(transformer);
        return this;
    }

    public <T> Menu transform(State.StateKey<T> key, Cons2<MenuView, T> transformer) {
        this.transformers.add(view -> transformer.get(view, view.state.get(key)));
        return this;
    }

    public <T1, T2> Menu transform(State.StateKey<T1> key1, State.StateKey<T2> key2, Cons3<MenuView, T1, T2> transformer) {
        this.transformers.add(view -> transformer.get(view, view.state.get(key1), view.state.get(key2)));
        return this;
    }

    public MenuView show(Player player) {
        return show(player, State.create());
    }

    public MenuView show(Player player, State state) {
        return views.get(player, () -> {
            var view = new MenuView(player, state);
            transformers.each(transformer -> transformer.get(view));

            return view.show();
        });
    }

    public <T> MenuView show(Player player, State.StateKey<T> key, T value) {
        return show(player, State.create(key, value));
    }

    public <T1, T2> MenuView show(Player player, State.StateKey<T1> key1, T1 value1, State.StateKey<T2> key2, T2 value2) {
        return show(player, State.create(key1, value1).put(key2, value2));
    }

    public <T1, T2, T3> MenuView show(Player player, State.StateKey<T1> key1, T1 value1, State.StateKey<T2> key2, T2 value2, State.StateKey<T3> key3, T3 value3) {
        return show(player, State.create(key1, value1).put(key2, value2).put(key3, value3));
    }

    public class MenuView {
        public final Player player;
        public final State state;

        public String title = "";
        public String content = "";

        public Seq<Seq<MenuOption>> options = new Seq<>();

        public MenuView(Player player, State state) {
            this.player = player;
            this.state = state;
        }

        public Menu menu() {
            return Menu.this;
        }

        public MenuView show() {
            Call.menu(player.con, id, title, content, options.map(options -> options.map(MenuOption::button).toArray(String.class)).toArray(String[].class));
            return this;
        }

        public MenuView title(String title, Object... values) {
            this.title = MenuFormatter.format(title, player, values);
            return this;
        }

        public MenuView content(String content, Object... values) {
            this.content = MenuFormatter.format(content, player, values);
            return this;
        }

        public MenuView row() {
            this.options.add(new Seq<MenuOption>());
            return this;
        }

        public MenuView option(String button, Object... values) {
            return option(MenuOption.of(MenuFormatter.format(button, player, values)));
        }

        public MenuView option(String button, Action action, Object... values) {
            return option(MenuOption.of(MenuFormatter.format(button, player, values), action));
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
            if (this.options.isEmpty() || this.options.peek().isEmpty())
                this.row();

            for (var data : datas) {
                if (this.options.peek().size >= maxPerRow)
                    this.row();

                option(data);
            }

            return this;
        }

        public MenuView options(int maxPerRow, MenuOption... options) {
            if (this.options.isEmpty() || this.options.peek().isEmpty())
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

        public interface OptionData {
            void option(MenuView menu); // SHOULD add an option
        }
    }
}