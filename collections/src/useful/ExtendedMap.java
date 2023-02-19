package useful;

import arc.func.*;
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

    public Entry<K, V> find(Boolf2<K, V> filter) {
        for (var entry : entries())
            if (filter.get(entry.key, entry.value))
                return entry;

        return null;
    }

    public K findKey(Boolf<K> filter) {
        for (var key : keys())
            if (filter.get(key))
                return key;

        return null;
    }

    public V findValue(Boolf<V> filter) {
        for (var value : values())
            if (filter.get(value))
                return value;

        return null;
    }
}