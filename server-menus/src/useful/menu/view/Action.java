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

    static Action view(Action action) {
        return action; // For chaining
    }

    static Action run(Runnable runnable) {
        return view -> runnable.run();
    }

    static Action player(Cons<Player> action) {
        return view -> action.get(view.player);
    }

    static Action show() {
        return view -> view.getMenu().show(view.player, view.state, view.transformer);
    }

    static Action showOther(Menu menu) {
        return view -> menu.show(view.player, view.state);
    }

    static <T> Action showWith(StateKey<T> key, T value) {
        return view ->
                view.getMenu().show(view.player, view.state.put(key, value), view.transformer);
    }

    static <T> Action showWithout(StateKey<T> key) {
        return view ->
                view.getMenu().show(view.player, view.state.remove(key), view.transformer);
    }

    static <T> Action showConsume(StateKey<T> key, Cons<T> cons) {
        return view -> {
            var value = view.state.get(key);
            cons.get(value);

            view.getMenu().show(view.player, view.state.put(key, value), view.transformer);
        };
    }

    static <T> Action showGet(StateKey<T> key, Func<T, T> func) {
        return view ->
                view.getMenu().show(view.player, view.state.put(key, func.get(view.state.get(key))), view.transformer);
    }

    static Action uri(String uri) {
        return view -> Call.openURI(view.player.con, uri);
    }

    static Action connect(String ip, int port) {
        return view -> Call.connect(view.player.con, ip, port);
    }

    default Action then(Action after) {
        return view -> {
            get(view);
            after.get(view);
        };
    }
}