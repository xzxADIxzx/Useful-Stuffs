package useful.menu.view;

import arc.func.Cons;
import mindustry.gen.Call;
import mindustry.gen.Player;
import useful.menu.view.Menu.MenuView;
import useful.menu.view.State.StateKey;

@FunctionalInterface
public interface Action extends Cons<MenuView> {

    static Action none() {
        return view -> {};
    }

    static Action run(Runnable runnable) {
        return view -> runnable.run();
    }

    static Action player(Cons<Player> action) {
        return view -> action.get(view.player);
    }

    static Action open() {
        return view -> view.getInterface().open(view.player, view.state);
    }

    static <T> Action openWith(StateKey<T> key, T value) {
        return view ->
                view.getInterface().open(view.player, view.state.with(key, value));
    }

    static <T> Action openWithout(StateKey<T> key) {
        return view ->
                view.getInterface().open(view.player, view.state.remove(key));
    }

    static Action uri(String uri) {
        return view -> Call.openURI(view.player.con, uri);
    }

    default Action andThen(Action after) {
        return view -> {
            get(view);
            after.get(view);
        };
    }
}