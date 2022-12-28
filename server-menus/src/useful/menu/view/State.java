package useful.menu.view;

import arc.struct.ObjectMap;

@SuppressWarnings("unchecked")
public record State(ObjectMap<String, Object> map) {

    public State() {
        this(new ObjectMap<>());
    }

    public static <T> State with(StateKey<T> key, T value) {
        return new State().put(key, value);
    }

    public <T> State put(StateKey<T> key, T value) {
        map.put(key.name(), value);
        return this;
    }

    public State remove(StateKey<?> key) {
        map.remove(key.name());
        return this;
    }

    public <T> T get(StateKey<T> key) {
        return (T) map.get(key.name());
    }

    public <T> T get(StateKey<T> key, T def) {
        return (T) map.get(key.name(), () -> def);
    }

    public boolean contains(StateKey<?> key) {
        return map.containsKey(key.name());
    }

    public record StateKey<T>(String name, Class<T> type) {}
}