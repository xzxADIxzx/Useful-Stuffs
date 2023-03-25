package useful;

import arc.func.Cons;
import arc.func.Func;
import mindustry.gen.Call;
import useful.Interface.View;
import useful.State.StateKey;

@FunctionalInterface
public interface Action extends Cons<View> {

    static Action none() {
        return view -> {};
    }

    static Action run(Runnable runnable) {
        return view -> runnable.run();
    }

    static Action open(Interface<?> menu) {
        return view -> {
            var open = menu.show(view.player, view.state);
            open.previous = view;
        };
    }

    static Action back() {
        return view -> {
            if (view.previous == null) return;

            var open = view.previous.parent().show(view.player, view.state);
            open.previous = view.previous.previous;
        };
    }

    static Action show() {
        return view -> view.parent().show(view.player, view.state, view.previous);
    }

    static <T> Action showWith(StateKey<T> key, T value) {
        return view -> view.parent().show(view.player, view.state.put(key, value), view.previous);
    }

    static <T> Action showWithout(StateKey<T> key) {
        return view -> view.parent().show(view.player, view.state.remove(key), view.previous);
    }

    static <T> Action showConsume(StateKey<T> key, Cons<T> cons) {
        return view -> {
            var value = view.state.get(key);
            cons.get(value);

            view.parent().show(view.player, view.state.put(key, value), view.previous);
        };
    }

    static <T> Action showGet(StateKey<T> key, Func<T, T> func) {
        return view -> view.parent().show(view.player, view.state.put(key, func.get(view.state.get(key))), view.previous);
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

    static Action then(Action first, Action second, Action third) {
        return view -> {
            first.get(view);
            second.get(view);
            third.get(view);
        };
    }

    default Action after(Action second) {
        return view -> {
            get(view);
            second.get(view);
        };
    }

    default Action after(Action second, Action third) {
        return view -> {
            get(view);
            second.get(view);
            third.get(view);
        };
    }
}