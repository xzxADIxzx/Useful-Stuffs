package useful.menu.view;

import arc.func.Cons;
import mindustry.gen.Call;
import mindustry.gen.Player;
import useful.menu.view.MenuInterface.MenuView;
import useful.menu.view.State.StateKey;

@FunctionalInterface
public interface Action<V> extends Cons<V> {

    static <V extends MenuView> Action<V> none() {
        return view -> {};
    }

    static <V extends MenuView> Action<V> run(Runnable runnable) {
        return view -> runnable.run();
    }

    static <V extends MenuView> Action<V> player(Action<Player> action) {
        return view -> action.get(view.player);
    }

    static <V extends MenuView> Action<V> open() {
        return view -> view.getInterface().open(view.player, view.state);
    }

    static <V extends MenuView, T> Action<V> openWith(StateKey<T> key, T value) {
        return view ->
                view.getInterface().open(view.player, view.state.with(key, value));
    }

    static <V extends MenuView, T> Action<V> openWithout(StateKey<T> key) {
        return view ->
                view.getInterface().open(view.player, view.state.remove(key));
    }

    static <V extends MenuView> Action<V> uri(String uri) {
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