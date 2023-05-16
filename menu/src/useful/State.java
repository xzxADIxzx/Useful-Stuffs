package useful;

import arc.struct.ObjectMap;

@SuppressWarnings("unchecked")
public record State(ObjectMap<StateKey<?>, Object> map) {

    public static <T> State create() {
        return new State(new ObjectMap<>());
    }

    public static <T> State create(StateKey<T> key, T value) {
        return new State(new ObjectMap<>()).put(key, value);
    }

    public <T> State put(StateKey<T> key, T value) {
        map.put(key, value);
        return this;
    }

    public State remove(StateKey<?> key) {
        map.remove(key);
        return this;
    }

    public <T> T get(StateKey<T> key) {
        return (T) map.get(key);
    }

    public <T> T get(StateKey<T> key, T def) {
        return (T) map.get(key, () -> def);
    }

    public boolean contains(StateKey<?> key) {
        return map.containsKey(key);
    }

    public record StateKey<T>(Class<T> type) {}
}