package useful;

import arc.func.Cons;
import arc.func.Cons2;
import mindustry.gen.Call;
import useful.Interface.View;
import useful.State.StateKey;

import java.util.Optional;

@FunctionalInterface
@SuppressWarnings("unchecked")
public interface Action2<V extends View, T> extends Cons2<V, T> {

    static <V extends View, T> Action2<V, T> none() {
        return (view, value) -> {};
    }

    static <V extends View, T> Action2<V, T> run(Runnable runnable) {
        return (view, value) -> runnable.run();
    }

    static <V extends View, T> Action2<V, T> get(Cons<T> cons) {
        return (view, value) -> cons.get(value);
    }

    static <V extends View, T> Action2<V, T> open(Interface<?> next) {
        return (view, value) -> next.show(view.player, view.state, view);
    }

    static <V extends View, T> Action2<V, T> openWith(Interface<?> next, StateKey<T> key) {
        return (view, value) -> next.show(view.player, view.state.put(key, value), view.parent);
    }

    static <V extends View, T> Action2<V, T> openWithout(Interface<?> next, StateKey<T> key) {
        return (view, value) -> next.show(view.player, view.state.remove(key), view.parent);
    }

    static <V extends View, T> Action2<V, T> back() {
        return (view, value) -> Optional.ofNullable(view.parent).ifPresent(parent -> parent.getInterface().show(view.player, view.state, parent.parent));
    }

    static <V extends View, T> Action2<V, T> show() {
        return (view, value) -> view.getInterface().show(view.player, view.state, view.parent);
    }

    static <V extends View, T> Action2<V, T> showWith(StateKey<T> key) {
        return (view, value) -> view.getInterface().show(view.player, view.state.put(key, value), view.parent);
    }

    static <V extends View, T> Action2<V, T> showWithout(StateKey<T> key) {
        return (view, value) -> view.getInterface().show(view.player, view.state.remove(key), view.parent);
    }

    static <V extends View, T> Action2<V, T> uri(String uri) {
        return (view, value) -> Call.openURI(view.player.con, uri);
    }

    static <V extends View, T> Action2<V, T> connect(String ip, int port) {
        return (view, value) -> Call.connect(view.player.con, ip, port);
    }

    static <V extends View, T> Action2<V, T> then(Action2<? super View, T> first, Action2<? super View, T> second) {
        return (view, value) -> {
            first.get(view, value);
            second.get(view, value);
        };
    }

    static <V extends View, T> Action2<V, T> then(Action2<? super View, T> first, Action2<? super View, T> second, Action2<? super View, T> third) {
        return (view, value) -> {
            first.get(view, value);
            second.get(view, value);
            third.get(view, value);
        };
    }

    default Action2<V, T> after(Action2<? super View, T> second) {
        return (view, value) -> {
            get(view, value);
            second.get(view, value);
        };
    }

    default Action2<V, T> after(Action2<? super View, T> second, Action2<? super View, T> third) {
        return (view, value) -> {
            get(view, value);
            second.get(view, value);
            third.get(view, value);
        };
    }
}