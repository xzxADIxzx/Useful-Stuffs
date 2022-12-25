package useful.menu;

import arc.func.Func;
import arc.struct.*;
import mindustry.gen.Call;
import mindustry.gen.Player;
import mindustry.ui.Menus;

import java.util.Arrays;

public class MenuInterface {

    public final ObjectMap<Player, MenuView> views = new ObjectMap<>();
    public final Seq<Func<MenuView, MenuPane>> transformers = new Seq<>();
    public final int id;

    public Action<MenuView> closeAction = Action.none();

    {
        this.id = Menus.registerMenu((player, option) -> {
            var view = this.views.remove(player);
            var menuOption = view.pane.getOption(option);

            if (menuOption == null) closeAction.get(view);
            else menuOption.action().get(view);
        });
    }

    public MenuView open(Player player) {
        return open(player, new State(new OrderedMap<>()));
    }

    public MenuView open(Player player, State state) {
        if (views.containsKey(player))
            return views.get(player);

        var view = new MenuView(player, state);
        transformers.each(transformer -> view.pane = transformer.get(view));

        Call.menu(
                view.player.con(),
                id,
                view.pane.title,
                view.pane.content,
                Arrays.stream(view.pane.options)
                        .map(options -> Arrays.stream(options).map(MenuOption::content).toArray(String[]::new))
                        .toArray(String[][]::new));
        return view;
    }

    public void addTransformer(Func<MenuView, MenuPane> transformer) {
        transformers.add(transformer);
    }

    public Seq<Func<MenuView, MenuPane>> getTransformers() {
        return transformers;
    }

    public Action<MenuView> getCloseAction() {
        return closeAction;
    }

    public void setCloseAction(Action<MenuView> closeAction) {
        this.closeAction = closeAction;
    }

    public class MenuView {
        public final Player player;
        public final State state;
        public MenuPane pane = new MenuPane();

        private MenuView(Player player, State state) {
            this.player = player;
            this.state = state;
        }

        public MenuInterface getInterface() {
            return MenuInterface.this;
        }

        public boolean isViewing() {
            return views.containsKey(player);
        }
    }
}