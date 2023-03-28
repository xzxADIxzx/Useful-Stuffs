package useful;

import arc.Events;
import arc.func.Cons;
import arc.func.Cons2;
import arc.func.Cons3;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import mindustry.game.EventType.PlayerLeave;
import mindustry.gen.Player;
import useful.Interface.View;
import useful.State.StateKey;

public abstract class Interface<V extends View> {
    public final ObjectMap<Player, V> views = new ObjectMap<>();
    public final Seq<Cons<V>> transformers = new Seq<>();

    public final int id = register();

    {
        Events.on(PlayerLeave.class, event -> views.remove(event.player));
    }

    // region abstract

    public abstract int register();
    public abstract V show(Player player, State state, View previous);

    // endregion
    // region show

    public V show(Player player, State state) {
        return show(player, state, null);
    }

    public V show(Player player) {
        return show(player, State.create());
    }

    public <T> V show(Player player, StateKey<T> key, T value) {
        return show(player, State.create(key, value));
    }

    public <T1, T2> V show(Player player, StateKey<T1> key1, T1 value1, StateKey<T2> key2, T2 value2) {
        return show(player, State.create(key1, value1).put(key2, value2));
    }

    public <T1, T2, T3> V show(Player player, StateKey<T1> key1, T1 value1, StateKey<T2> key2, T2 value2, StateKey<T3> key3, T3 value3) {
        return show(player, State.create(key1, value1).put(key2, value2).put(key3, value3));
    }

    // region transform

    public Interface<V> transform(Cons<V> transformer) {
        this.transformers.add(transformer);
        return this;
    }

    public <T> Interface<V> transform(StateKey<T> key, Cons2<V, T> transformer) {
        return transform(view -> transformer.get(view, view.state.get(key)));
    }

    public <T1, T2> Interface<V> transform(StateKey<T1> key1, StateKey<T2> key2, Cons3<V, T1, T2> transformer) {
        return transform(view -> transformer.get(view, view.state.get(key1), view.state.get(key2)));
    }

    // endregion

    public abstract class View {
        public final Player player;
        public final State state;
        public final View parent;

        public View(Player player, State state, View parent) {
            this.player = player;
            this.state = state;
            this.parent = parent;
        }

        public Interface<V> getInterface() {
            return Interface.this;
        }
    }
}