package useful;

import arc.func.Cons;
import arc.func.Func;
import mindustry.gen.Call;

@FunctionalInterface
public interface Action extends Cons<Menu.MenuView> {

    static Action none() {
        return view -> {};
    }

    static Action run(Runnable runnable) {
        return view -> runnable.run();
    }

    static Action show() {
        return view -> view.menu().show(view.player, view.state);
    }

    static <T> Action showWith(State.StateKey<T> key, T value) {
        return view -> view.menu().show(view.player, view.state.put(key, value));
    }

    static <T> Action showWithout(State.StateKey<T> key) {
        return view -> view.menu().show(view.player, view.state.remove(key));
    }

    static <T> Action showUse(State.StateKey<T> key, Cons<T> cons) {
        return view -> {
            var value = view.state.get(key);
            cons.get(value);

            view.menu().show(view.player, view.state.put(key, value));
        };
    }

    static <T> Action showGet(State.StateKey<T> key, Func<T, T> func) {
        return view -> view.menu().show(view.player, view.state.put(key, func.get(view.state.get(key))));
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