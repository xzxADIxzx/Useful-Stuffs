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

    static Action open(Menu menu) {
        return view -> {
            var open = menu.show(view.player, view.state);
            open.previous = view;
        };
    }

    static Action back() {
        return view -> {
            if (view.previous == null) return;

            var open = view.previous.menu().show(view.player, view.state);
            open.previous = view.previous.previous;
        };
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

    static Action then(Action first, Action second) {
        return view -> {
            first.get(view);
            second.get(view);
        };
    }

    default Action then(Action second) {
        return view -> {
            get(view);
            second.get(view);
        };
    }
}