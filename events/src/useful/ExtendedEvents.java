package useful;

import arc.Events;
import arc.func.Cons;
import arc.struct.*;
import arc.util.Reflect;

@SuppressWarnings("unchecked")
public class ExtendedEvents {

    public static final ObjectMap<Object, Seq<Cons<?>>> events = Reflect.get(Events.class, "events");

    public static <T> Cons<T> get(Class<T> type, int index){
        return (Cons<T>) events.get(type, Seq::new).get(index);
    }

    public static <T> void set(Class<T> type, int index, Cons<T> listener){
        events.get(type, Seq::new).set(index, listener);
    }

    public static <T> void insert(Class<T> type, int index, Cons<T> listener){
        events.get(type, Seq::new).insert(index, listener);
    }

    public static <T> Cons<T> remove(Class<T> type, int index){
        return (Cons<T>) events.get(type, Seq::new).remove(index);
    }
}