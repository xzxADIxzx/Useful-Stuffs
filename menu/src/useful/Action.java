package useful;

import arc.func.Cons;
import mindustry.gen.Call;
import useful.Interface.View;
import useful.State.StateKey;

import java.util.Optional;

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
        return view -> next.show(view.player, view.state, view);
    }

    static <V extends View, T> Action<V> openWith(Interface<?> next, StateKey<T> key, T value) {
        return view -> next.show(view.player, view.state.put(key, value), view);
    }

    static <V extends View, T> Action<V> openWithout(Interface<?> next, StateKey<T> key) {
        return view -> next.show(view.player, view.state.remove(key), view);
    }

    static <V extends View> Action<V> back() {
        return view -> Optional.ofNullable(view.parent).ifPresent(parent -> parent.getInterface().show(view.player, view.state, parent.parent));
    }

    static <V extends View> Action<V> show() {
        return view -> view.getInterface().show(view.player, view.state, view.parent);
    }

    static <V extends View, T> Action<V> showWith(StateKey<T> key, T value) {
        return view -> view.getInterface().show(view.player, view.state.put(key, value), view.parent);
    }

    static <V extends View, T> Action<V> showWithout(StateKey<T> key) {
        return view -> view.getInterface().show(view.player, view.state.remove(key), view.parent);
    }

    static <V extends View> Action<V> hide() {
        return view -> Call.hideFollowUpMenu(view.player.con, view.getInterface().id);
    }

    static <V extends View> Action<V> uri(String uri) {
        return view -> Call.openURI(view.player.con, uri);
    }

    static <V extends View> Action<V> connect(String ip, int port) {
        return view -> Call.connect(view.player.con, ip, port);
    }

    static <V extends View> Action<V> then(Action<V> first, Action<V> second) {
        return view -> {
            first.get(view);
            second.get(view);
        };
    }

    static <V extends View> Action<V> then(Action<V> first, Action<V> second, Action<V> third) {
        return view -> {
            first.get(view);
            second.get(view);
            third.get(view);
        };
    }

    default Action<V> after(Action<V> second) {
        return view -> {
            get(view);
            second.get(view);
        };
    }

    default Action<V> after(Action<V> second, Action<V> third) {
        return view -> {
            get(view);
            second.get(view);
            third.get(view);
        };
    }
}