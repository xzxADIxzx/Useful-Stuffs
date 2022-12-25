package useful.menu;

import arc.func.Cons;
import mindustry.gen.Call;

@FunctionalInterface
public interface Action<V> extends Cons<V> {

    static <V extends MenuInterface.MenuView> Action<V> none() {
        return view -> {};
    }

    static <V extends MenuInterface.MenuView> Action<V> open() {
        return view -> view.getInterface().open(view.player, view.state);
    }

    static <V extends MenuInterface.MenuView, T> Action<V> openWith(State.StateKey<T> key, T value) {
        return view ->
                view.getInterface().open(view.player, view.state.with(key, value));
    }

    static <V extends MenuInterface.MenuView, T> Action<V> openWithout(State.StateKey<T> key) {
        return view ->
                view.getInterface().open(view.player, view.state.remove(key));
    }

    static <V extends MenuInterface.MenuView> Action<V> uri(String uri) {
        return view -> Call.openURI(view.player.con, uri);
    }

    @Override
    void get(V view);

    default Action<V> andThen(Action<? super V> after) {
        return (V v) -> {
            get(v);
            after.get(v);
        };
    }
}