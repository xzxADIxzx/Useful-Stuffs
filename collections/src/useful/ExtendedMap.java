package useful;

import arc.func.Boolf;
import arc.func.Cons;
import arc.struct.ObjectMap;

public class ExtendedMap<K, V> extends ObjectMap<K, V> {

    public ExtendedMap() {}

    public ExtendedMap(int initialCapacity) {
        super(initialCapacity);
    }

    public ExtendedMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public ExtendedMap(ObjectMap<? extends K, ? extends V> map) {
        super(map);
    }

    public void eachKey(Cons<K> cons) {
        for (var key : keys())
            cons.get(key);
    }

    public void eachValue(Cons<V> cons) {
        for (var value : values())
            cons.get(value);
    }

    public K findKey(Boolf<V> filter) {
        for (var entry : entries())
            if (filter.get(entry.value))
                return entry.key;

        return null;
    }

    public V findValue(Boolf<K> filter) {
        for (var entry : entries())
            if (filter.get(entry.key))
                return entry.value;

        return null;
    }
}