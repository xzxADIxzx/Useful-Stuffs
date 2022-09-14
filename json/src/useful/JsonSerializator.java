package useful;

import useful.Json.JsonSerializer;

import java.util.ArrayList;

/** Converts objects to {@link Json} using registered {@link JsonSerializer}s or reflection. */
@SuppressWarnings("unchecked")
public class JsonSerializator {

    private static ArrayList<Class<?>> unserializable = new ArrayList<>() {{ // ._.
        add(Object.class); add(Byte.class); add(Short.class); add(Integer.class); add(Long.class);
        add(Character.class); add(Boolean.class); add(Float.class); add(Double.class);
    }};

    private ArrayList<ClassSerializerPair<?>> pairs = new ArrayList<>();

    public Json serialize(Object object) {
        ClassSerializerPair<Object> pair = getSerializer(object);

        if (pair != null) return pair.serializer.write(object);
        else return new Json(object.getClass()); // TODO serialize through public fields and etc.
    }

    public Object deserialize(Json json) {
        String className = json.getAs("class");
        if (className == null) return json;

        Class<?> referenced = null;
        try {
            referenced = Class.forName(className);
        } catch (ClassNotFoundException ignored) {
            referenced = null; // TODO search in TagMap<String, Class<?>>
        }

        if (referenced == null) return json;
        else return null; // TODO deserialize through public fields and class
    }

    public boolean serializable(Object object) {
        return !object.getClass().isPrimitive() && !unserializable.contains(object.getClass());
    }

    public <T> void addSerializer(Class<T> referenced, JsonSerializer<T> serializer) {
        if (unserializable.contains(referenced)) return; // pls, don't
        pairs.add(new ClassSerializerPair<T>(referenced, serializer));
    }

    public <T> ClassSerializerPair<T> getSerializer(T referenced) {
        for (ClassSerializerPair<?> pair : pairs) // class cast exception not possible
            if (pair.referenced.isInstance(referenced)) return (ClassSerializerPair<T>) pair;
        return null; // no serializer found so we will serialize via reflection
    }

    public record ClassSerializerPair<T>(Class<T> referenced, JsonSerializer<T> serializer) {}
}
