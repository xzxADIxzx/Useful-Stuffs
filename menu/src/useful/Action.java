package useful;

import arc.func.Cons;
import arc.func.Func;
import mindustry.gen.Call;
import useful.Interface.View;
import useful.State.StateKey;

@FunctionalInterface
@SuppressWarnings("unchecked")
public interface Action<V extends View> extends Cons<V> {

    static <V extends View> Action<V> none() {
        return view -> {};
    }

    static <V extends View> Action<V> run(Runnable runnable) {
        return view -> runnable.run();
    }

    static <V extends View> Action<V> open(Interface<?> next) {
        return view -> {
            var open = next.show(view.player, view.state);
            open.previous = view;
        };
    }

    static <V extends View> Action<V> back() {
        return view -> {
            if (view.previous == null) return;

            var open = view.previous.parent().show(view.player, view.state);
            open.previous = view.previous.previous;
        };
    }

    static <V extends View> Action<V> show() {
        return view -> view.parent().show(view.player, view.state, view.previous);
    }

    static <V extends View, T> Action<V> showWith(StateKey<T> key, T value) {
        return view -> view.parent().show(view.player, view.state.put(key, value), view.previous);
    }

    static <V extends View, T> Action<V> showWithout(StateKey<T> key) {
        return view -> view.parent().show(view.player, view.state.remove(key), view.previous);
    }

    static <V extends View, T> Action<V> showConsume(StateKey<T> key, Cons<T> cons) {
        return view -> {
            var value = view.state.get(key);
            cons.get(value);

            view.parent().show(view.player, view.state.put(key, value), view.previous);
        };
    }

    static <V extends View, T> Action<V> showGet(StateKey<T> key, Func<T, T> func) {
        return view -> view.parent().show(view.player, view.state.put(key, func.get(view.state.get(key))), view.previous);
    }

    static <V extends View> Action<V> uri(String uri) {
        return view -> Call.openURI(view.player.con, uri);
    }

    static <V extends View> Action<V> connect(String ip, int port) {
        return view -> Call.connect(view.player.con, ip, port);
    }

    static <V extends View> Action<V> then(Action<? super View> first, Action<? super View> second) {
        return view -> {
            first.get(view);
            second.get(view);
        };
    }

    static <V extends View> Action<V> then(Action<? super View> first, Action<? super View> second, Action<? super View> third) {
        return view -> {
            first.get(view);
            second.get(view);
            third.get(view);
        };
    }

    default Action<V> after(Action<? super View> second) {
        return view -> {
            get(view);
            second.get(view);
        };
    }

    default Action<V> after(Action<? super View> second, Action<? super View> third) {
        return view -> {
            get(view);
            second.get(view);
            third.get(view);
        };
    }
}