package useful.menu.view;

import arc.func.*;
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
        return view -> view.getMenu().open(view.player, view.state, view.transformer);
    }

    static <T> Action openWith(StateKey<T> key, T value) {
        return view ->
                view.getMenu().open(view.player, view.state.put(key, value), view.transformer);
    }

    static <T> Action openWithout(StateKey<T> key) {
        return view ->
                view.getMenu().open(view.player, view.state.remove(key), view.transformer);
    }

    static <T> Action openChange(StateKey<T> key, Cons<T> cons) {
        return view -> {
            var value = view.state.get(key);
            cons.get(value);

            view.getMenu().open(view.player, view.state.put(key, value), view.transformer);
        };
    }

    static <T> Action openChange(StateKey<T> key, Func<T, T> func) {
        return view -> {
            var value = func.get(view.state.get(key));

            view.getMenu().open(view.player, view.state.put(key, value), view.transformer);
        };
    }

    static Action uri(String uri) {
        return view -> Call.openURI(view.player.con, uri);
    }

    default Action then(Action after) {
        return view -> {
            get(view);
            after.get(view);
        };
    }
}